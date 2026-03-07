import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Map} from '../map/map';
import {Dashboard} from '../dashboard/dashboard';

@Component({
  selector: 'app-maintenance-page',
  imports: [CommonModule, Map,Dashboard],
  templateUrl: './maintenance-page.html',
  styleUrls: ['./maintenance-page.scss']
})
export class MaintenancePage {

  constructor() { }

}
