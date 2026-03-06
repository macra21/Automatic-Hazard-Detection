import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export interface Hazard {
  id: string;
  time: string;
  description: string;
  imageUrl?: string;
}

@Injectable({
  providedIn: 'root' // This makes the service available everywhere in your app
})
export class HazardService {
  // Move your hardcoded array here!
  private hazards: Hazard[] = [
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
  ];
  private selectedHazardSource = new BehaviorSubject<Hazard | null>(null);
  public selectedHazard$ = this.selectedHazardSource.asObservable();

  private hazardRemovedSource = new Subject<string>();
  public hazardRemoved$ = this.hazardRemovedSource.asObservable();

  private hazardsUpdatedSource = new Subject<Hazard[]>();
  public hazardsUpdated$ = this.hazardsUpdatedSource.asObservable();

  private stompClient: Client;

  constructor() {
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

  private handleHazardNotification(notification: any) {
    console.log('Received hazard notification:', notification);
    const hazardData = notification.hazard;

    // Map backend hazard to frontend hazard
    // Note: Backend uses getID() (uppercase ID), but Jackson usually serializes getters to lowercase fields (id)
    // unless configured otherwise. Assuming standard serialization:
    const newHazard: Hazard = {
      id: hazardData.id ? hazardData.id.toString() : (hazardData.ID ? hazardData.ID.toString() : 'unknown'),
      time: hazardData.date ? new Date(hazardData.date).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : 'Now',
      description: hazardData.description || 'New Hazard Detected',
      imageUrl: notification.imageContent ? `data:${notification.imageType};base64,${notification.imageContent}` : undefined
    };

    // Add to the beginning of the list
    this.hazards.unshift(newHazard);

    // Notify subscribers about the update
    this.hazardsUpdatedSource.next(this.hazards);
  }

  getHazards(): Hazard[] {
    return this.hazards;
  }

  getHazardById(id: string): Hazard | undefined {
    return this.hazards.find(h => h.id === id);
  }

  removeHazard(id: string): void {
    this.hazards = this.hazards.filter(h => h.id !== id);
    this.hazardRemovedSource.next(id);
    this.hazardsUpdatedSource.next(this.hazards);
  }

  selectHazard(hazard: Hazard | null): void {
    this.selectedHazardSource.next(hazard);
  }
}
