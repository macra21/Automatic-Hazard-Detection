import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { HttpClient } from '@angular/common/http';

export interface Hazard {
  id: string;
  time: string;
  description: string;
  imageUrl?: string;
  coordinates?: { lat: number; lng: number };
  status?: string;
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

  constructor(private http: HttpClient) {
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
        this.hazards = data.map(h => this.mapBackendHazardToFrontend(h));
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
      status: hazardData.status
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

  removeHazard(id: string): void {
    // Optimistic update
    const hazardToRemove = this.hazards.find(h => h.id === id);
    if (!hazardToRemove) return;

    this.hazards = this.hazards.filter(h => h.id !== id);
    this.hazardRemovedSource.next(id);
    this.hazardsUpdatedSource.next(this.hazards);

    // Send update to backend
    // We need to send the full hazard object with updated status
    // But we only have the frontend representation. Ideally we should fetch the full object or have a specific endpoint for status update.
    // The controller expects a full Hazard object.
    // Let's try to construct a minimal object that the backend might accept, or we need to change the backend to accept just ID and status.
    // Based on the controller: updateHazard(@RequestBody Hazard hazard)

    // Construct a payload that matches the backend Hazard structure as much as possible
    const payload = {
        ID: parseInt(id),
        status: 'RESOLVED', // Or whatever status means "removed" / "processed"
        // We might need other fields if the backend validation is strict
        description: hazardToRemove.description,
        // coordinates: ...
    };

    this.http.put(`${this.baseUrl}/update`, payload).subscribe({
        next: () => console.log(`Hazard ${id} updated successfully in backend`),
        error: (err) => {
            console.error(`Failed to update hazard ${id} in backend`, err);
            // Revert optimistic update if needed
        }
    });
  }

  selectHazard(hazard: Hazard | null): void {
    this.selectedHazardSource.next(hazard);
  }
}
