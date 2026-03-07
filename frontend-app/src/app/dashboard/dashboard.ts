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
  public userRole: string | null = null;

  constructor(private hazardService: HazardService, private cdr: ChangeDetectorRef){}

  ngOnInit() {
    console.log('===== DASHBOARD NGOINIT CALLED =====');

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

    // Refresh hazards based on current user role (handles page navigation)
    // This is called AFTER subscribing so we don't miss the first emission
    console.log('Dashboard calling refreshHazardsBasedOnRole()');
    this.hazardService.refreshHazardsBasedOnRole();
    this.userRole = this.hazardService.getCurrentUserRole();
    console.log('Dashboard finished calling refreshHazardsBasedOnRole(), userRole:', this.userRole);
  }

  onSelectHazard(hazard: Hazard): void {
    this.hazardService.selectHazard(hazard);
    console.log('Selectat:', hazard.id);
  }

  closePopup(): void {
    this.hazardService.selectHazard(null);  }

  onConfirm(id: string): void {
    // Determine what status to set based on current user role
    const currentUser = this.hazardService.getCurrentUserRole();
    let newStatus = 'CONFIRMED';

    if (currentUser === 'ATC') {
      // Operator confirms the hazard exists and is dangerous
      newStatus = 'CONFIRMED';
    } else if (currentUser === 'CLEANUP') {
      // Maintenance marks the hazard as cleared/solved
      newStatus = 'CLEARED';
    }

    console.log('onConfirm called for role:', currentUser, 'setting status to:', newStatus);
    this.hazardService.updateHazardStatus(id, newStatus);
    this.hazardFeed = this.hazardService.getHazards();
    this.closePopup();
  }

  onReject(id: string): void {
    // Determine what status to set based on current user role
    const currentUser = this.hazardService.getCurrentUserRole();
    let newStatus = 'DISMISSED';

    if (currentUser === 'ATC') {
      // Operator rejects the hazard (false alarm)
      newStatus = 'DISMISSED';
    } else if (currentUser === 'CLEANUP') {
      // Maintenance marks as false alarm
      newStatus = 'FALSE_ALARM';
    }

    console.log('onReject called for role:', currentUser, 'setting status to:', newStatus);
    this.hazardService.updateHazardStatus(id, newStatus);
    this.hazardFeed = this.hazardService.getHazards();
    this.closePopup();
  }
}
