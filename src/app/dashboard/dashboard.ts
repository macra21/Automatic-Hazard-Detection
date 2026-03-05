import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RiskPopup } from '../risk-popup/risk-popup';

export interface Hazard {
  id: string;
  time: string;
  description: string;
  imageUrl?: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RiskPopup],
  templateUrl: './dashboard.html',
  styleUrls: ['./dbc.css']
})
export class Dashboard {
  public hazardFeed: Hazard[] = [
    {id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1', imageUrl: 'assets/crack.jpg'},
    {id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)', imageUrl: 'assets/fod.jpg'},
    {id: '002', time: '10:30 AM', description: 'Fisură Mică Pistă 1'},
    {id: '001', time: '10:15 AM', description: 'Scurgere Combustibil - Sector C'},
  ];

  public selectedHazard: Hazard | null = null;

  onSelectHazard(hazard: Hazard): void {
    this.selectedHazard = hazard;
    console.log('Selectat:', hazard.id);
  }

  closePopup(): void {
    this.selectedHazard = null;
  }

  // 5. Removed the "event: Event" parameter.
  // We also call this.closePopup() to hide the modal after clicking.
  onConfirm(id: string): void {
    console.log(`Trimitem la Spring: Riscul ${id} a fost CONFIRMAT`);
    this.hazardFeed = this.hazardFeed.filter(h => h.id !== id);
    this.closePopup();
    // Aici va veni: this.http.patch(...)
  }

  onReject(id: string): void {
    console.log(`Trimitem la Spring: Riscul ${id} a fost RESPINS`);
    this.hazardFeed = this.hazardFeed.filter(h => h.id !== id);
    this.closePopup();
  }
}
