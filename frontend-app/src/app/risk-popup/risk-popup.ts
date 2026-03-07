import { Component, Input, Output, EventEmitter } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { Hazard } from '../hazard-service/hazard-service';

@Component({
  selector: 'app-risk-popup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './risk-popup.html', // Changed from 'template'
  styleUrls: ['./risk-popup2.scss']
})
export class RiskPopup {
  // Input: Gets the current hazard from the dashboard
  @Input() hazard!: Hazard;

  // Outputs: Send events back to the dashboard
  @Output() confirm = new EventEmitter<string>();
  @Output() reject = new EventEmitter<string>();
  @Output() close = new EventEmitter<void>();

  onConfirm() {
    this.confirm.emit(this.hazard.id);
  }

  onReject() {
    this.reject.emit(this.hazard.id);
  }

  onClose() {
    this.close.emit();
  }
}
