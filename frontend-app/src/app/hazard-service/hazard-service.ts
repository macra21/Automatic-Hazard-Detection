import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { HttpClient } from '@angular/common/http';
import { DataService } from '../dataService';

export interface Hazard {
  id: string;
  time: string;
  description: string;
  imageUrl?: string;
  coordinates?: { lat: number; lng: number };
  status?: string;
  originalData?: any;
}

@Injectable({
  providedIn: 'root' // This makes the service available everywhere in your app
})
export class HazardService {
  // Move your hardcoded array here!
  private hazards: Hazard[] = [];
  private selectedHazardSource = new BehaviorSubject<Hazard | null>(null);
  public selectedHazard$ = this.selectedHazardSource.asObservable();

  private hazardRemovedSource = new Subject<string>();
  public hazardRemoved$ = this.hazardRemovedSource.asObservable();

  private hazardsUpdatedSource = new Subject<Hazard[]>();
  public hazardsUpdated$ = this.hazardsUpdatedSource.asObservable();

  private newHazardSource = new Subject<Hazard>();
  public newHazard$ = this.newHazardSource.asObservable();

  private stompClient: Client;
  private baseUrl = 'http://localhost:8080/api/hazards';

  constructor(private http: HttpClient, private dataService: DataService) {
    this.loadInitialHazards();

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: (str) => {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connected: ' + frame);
      this.stompClient.subscribe('/topic/hazards', (message: Message) => {
        if (message.body) {
          const hazardNotification = JSON.parse(message.body);
          this.handleHazardNotification(hazardNotification);
        }
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.stompClient.activate();
  }

  private loadInitialHazards() {
    this.http.get<any[]>(`${this.baseUrl}/getHazardListByStatus?status=DETECTED`).subscribe({
      next: (data) => {
        console.log('Loaded initial hazards:', data);
        this.hazards = data.map(item => this.mapBackendHazardToFrontend(item.hazard, item.imageContent, item.imageType));
        this.hazardsUpdatedSource.next(this.hazards);
        // Also notify about new hazards so map can render them
        this.hazards.forEach(h => this.newHazardSource.next(h));
      },
      error: (err) => console.error('Failed to load initial hazards', err)
    });
  }

  private mapBackendHazardToFrontend(hazardData: any, imageContent?: string, imageType?: string): Hazard {
     return {
      id: hazardData.id ? hazardData.id.toString() : (hazardData.ID ? hazardData.ID.toString() : 'unknown'),
      time: hazardData.date ? new Date(hazardData.date).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : 'Now',
      description: hazardData.description || 'New Hazard Detected',
      imageUrl: imageContent ? `data:${imageType};base64,${imageContent}` : undefined,
      coordinates: hazardData.location ? { lat: hazardData.location.latitude, lng: hazardData.location.longitude } : undefined,
      status: hazardData.status,
      originalData: hazardData
    };
  }

  private handleHazardNotification(notification: any) {
    console.log('Received hazard notification:', notification);
    const hazardData = notification.hazard;

    const newHazard = this.mapBackendHazardToFrontend(hazardData, notification.imageContent, notification.imageType);

    // Add to the beginning of the list
    this.hazards.unshift(newHazard);

    // Notify subscribers about the update
    this.hazardsUpdatedSource.next(this.hazards);
    this.newHazardSource.next(newHazard);
  }

  getHazards(): Hazard[] {
    return this.hazards;
  }

  getHazardById(id: string): Hazard | undefined {
    return this.hazards.find(h => h.id === id);
  }

  updateHazardStatus(id: string, newStatus: string): void {
    const hazardToUpdate = this.hazards.find(h => h.id === id);
    if (!hazardToUpdate) return;

    // Optimistic update: remove from list if it's no longer DETECTED (assuming we only show DETECTED)
    this.hazards = this.hazards.filter(h => h.id !== id);
    this.hazardRemovedSource.next(id);
    this.hazardsUpdatedSource.next(this.hazards);

    // Prepare payload for backend
    // We use the original data we stored, and update the status
    const payload = { ...hazardToUpdate.originalData };
    payload.status = newStatus;

    // Attach the current user to the payload based on their role
    const currentUser = this.dataService.getCurrentUser();
    if (currentUser) {
        const userId = currentUser.id || currentUser.ID;
        if (userId) {
            // We only send the ID to link the user, not the full object
            const userRef = { ID: userId };

            if (currentUser.type === 'ATC') {
                payload.atcUser = userRef;
            } else if (currentUser.type === 'CLEANUP') {
                payload.cleanupUser = userRef;
            }
        }
    }

    this.http.put(`${this.baseUrl}/update`, payload).subscribe({
        next: () => console.log(`Hazard ${id} status updated to ${newStatus} in backend`),
        error: (err) => {
            console.error(`Failed to update hazard ${id} status in backend`, err);
            // Ideally revert optimistic update here
        }
    });
  }

  removeHazard(id: string): void {
      this.updateHazardStatus(id, 'DISMISSED');
  }

  selectHazard(hazard: Hazard | null): void {
    this.selectedHazardSource.next(hazard);
  }
}
