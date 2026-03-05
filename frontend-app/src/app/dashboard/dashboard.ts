import { Component } from '@angular/core';
import { NgClass } from '@angular/common'; // Am adăugat NgFor

interface Hazard {
  id: string;
  time: string;
  description: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
  ],
  templateUrl: './dashboard.html',
  styleUrls: ['./dbc.css']
})
export class Dashboard {
  public hazardFeed: Hazard[] = [
    { id: '004', time: '10:42 AM', description: 'Fisură Severă Pistă 1' },
    { id: '003', time: '10:35 AM', description: 'FOD Pistă B (Obiect Străin)' },
    { id: '002',  time: '10:30 AM', description: 'Fisură Mică Pistă 1' },
    { id: '001',  time: '10:15 AM', description: 'Scurgere Combustibil - Sector C' }
  ];

  public selectedId: string | null = null;

  onSelectHazard(id: string): void {
    this.selectedId = id;
    console.log("Risc selectat local:", id);
  }
}
