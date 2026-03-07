import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Map} from '../map/map';
import {Dashboard} from '../dashboard/dashboard';

@Component({
  selector: 'app-page',
  imports: [CommonModule, Map,Dashboard],
  templateUrl: './operator_page.html',
  styleUrls: ['./page2.scss']
})
export class Operator_page {

  constructor() { }

}
