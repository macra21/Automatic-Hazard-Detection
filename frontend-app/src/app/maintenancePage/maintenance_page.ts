import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Map} from '../map/map';
import {Dashboard} from '../dashboard/dashboard';

@Component({
  selector: 'app-maintenance-page',
  imports: [CommonModule, Map,Dashboard],
  templateUrl: './maintenance-page.html',
  styleUrls: ['./maintenance-page.scss']
})
export class MaintenancePage implements OnInit, OnDestroy {
  currentTime: string = '';
  currentDate: string = '';
  private timeInterval: any;

  constructor() { }

  ngOnInit() {
    this.updateRomaniaTime();
    this.timeInterval = setInterval(() => {
      this.updateRomaniaTime();
    }, 1000);
  }

  ngOnDestroy() {
    if (this.timeInterval) {
      clearInterval(this.timeInterval);
    }
  }

  private updateRomaniaTime() {
    const now = new Date();
    const romaniaTime = new Date(now.toLocaleString('en-US', { timeZone: 'Europe/Bucharest' }));

    // Format time as HH:MM:SS AM/PM
    this.currentTime = romaniaTime.toLocaleString('en-US', {
      hour: 'numeric',
      minute: '2-digit',
      second: '2-digit',
      hour12: true
    });

    // Format date as M/D/YYYY
    this.currentDate = romaniaTime.toLocaleString('en-US', {
      month: 'numeric',
      day: 'numeric',
      year: 'numeric'
    });
  }
}
