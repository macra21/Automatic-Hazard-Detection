import {Component, AfterViewInit, NgZone} from '@angular/core';
import * as L from 'leaflet';
import { getMarkers } from './markers';
import { RiskPopup } from '../risk-popup/risk-popup';
import { Hazard } from '../dashboard/dashboard';
import { HazardService } from '../hazard-service/hazard-service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [RiskPopup],
  templateUrl: './map.html',
  styleUrls: ['./map.css']
})
export class Map implements AfterViewInit {

  private map: L.Map | undefined;
  public selectedHazard: Hazard | null = null;
  private centroid: L.LatLngExpression = [47.023333, 21.901944];

  constructor(private zone: NgZone, private hazardService: HazardService) { }
  ngAfterViewInit(): void {
    this.initMap();
    this.renderHardcodedMarkers();
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: this.centroid,
      zoom: 15.48888888 // Zoom-ul ușor mărit pentru a vedea mai bine pista
    });

    const tiles = L.tileLayer('https://api.maptiler.com/maps/hybrid-v4/{z}/{x}/{y}.jpg?key=MCpv62w29YtAq27NFF3G', {
      maxZoom: 18,
      minZoom: 10,
    });

    tiles.addTo(this.map);
  }

  private renderHardcodedMarkers(): void {
    if (!this.map) return;
    const markers = getMarkers();

    markers.forEach(marker => {
      marker.addTo(this.map!);

      marker.on('click', () => {
        this.zone.run(() => {
          const id = (marker as any).hazardId;
          console.log(id)
          const foundHazard = this.hazardService.getHazardById(id);

          if (foundHazard) {
            // Tell the service to open the popup globally!
            console.log('found');
            this.hazardService.selectHazard(foundHazard);
          }
        });
      });
    });
  }

  }
