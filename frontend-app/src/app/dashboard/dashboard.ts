import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RiskPopup } from '../risk-popup/risk-popup';
import { HazardService, Hazard } from '../hazard-service/hazard-service'; //

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RiskPopup],
  templateUrl: './dashboard.html',
  styleUrls: ['./dbc2.scss']
})
export class Dashboard implements OnInit{
  public hazardFeed: Hazard[] = [];
  public selectedHazard: Hazard | null = null;

  constructor(private hazardService: HazardService, private cdr: ChangeDetectorRef){}

  ngOnInit() {
    // 1. Load the initial list
    this.hazardFeed = this.hazardService.getHazards();

    // 2. LISTEN to the global messenger. If the Map clicks a marker, this fires!
    this.hazardService.selectedHazard$.subscribe(hazard => {
      console.log('Dashboard received the hazard!', hazard);
      this.selectedHazard = hazard;
      this.cdr.detectChanges();
    });

    // 3. Listen for updates to the hazard list (e.g. from WebSocket)
    this.hazardService.hazardsUpdated$.subscribe(hazards => {
      console.log('Dashboard received updated hazards list', hazards);
      this.hazardFeed = [...hazards]; // Create a new array reference to trigger change detection
      this.cdr.detectChanges();
    });
  }

  onSelectHazard(hazard: Hazard): void {
    this.hazardService.selectHazard(hazard);
    console.log('Selectat:', hazard.id);
  }

  closePopup(): void {
    this.hazardService.selectHazard(null);  }

  onConfirm(id: string): void {
    this.hazardService.updateHazardStatus(id, 'CONFIRMED');
    this.hazardFeed = this.hazardService.getHazards();
    this.closePopup();
  }

  onReject(id: string): void {
    this.hazardService.updateHazardStatus(id, 'DISMISSED');
    this.hazardFeed = this.hazardService.getHazards();
    this.closePopup();
  }
}
