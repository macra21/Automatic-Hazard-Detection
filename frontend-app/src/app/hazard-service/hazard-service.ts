import { Injectable } from '@angular/core';
import { Hazard } from '../dashboard/dashboard';
import { BehaviorSubject, Subject } from 'rxjs';
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
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'not_yet_working.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'not_yet_working.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
  ];
  private selectedHazardSource = new BehaviorSubject<Hazard | null>(null);
  public selectedHazard$ = this.selectedHazardSource.asObservable();

  private hazardRemovedSource = new Subject<string>();
  public hazardRemoved$ = this.hazardRemovedSource.asObservable();

  getHazards(): Hazard[] {
    return this.hazards;
  }

  getHazardById(id: string): Hazard | undefined {
    return this.hazards.find(h => h.id === id);
  }

  removeHazard(id: string): void {
    this.hazards = this.hazards.filter(h => h.id !== id);
    this.hazardRemovedSource.next(id);
  }

  selectHazard(hazard: Hazard | null): void {
    this.selectedHazardSource.next(hazard);
  }
}
