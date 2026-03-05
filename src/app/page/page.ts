import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Map} from '../map/map';
import {Dashboard} from '../dashboard/dashboard';

@Component({
  selector: 'app-page',
  imports: [CommonModule, Map,Dashboard],
  templateUrl: './page.html',
  styleUrls: ['./page.css']
})
export class Page {

  constructor() { }

}
