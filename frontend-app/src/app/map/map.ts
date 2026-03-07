import {Component, AfterViewInit, NgZone, OnInit} from '@angular/core';
import * as L from 'leaflet';
import { getMarkers } from './markers';
import { RiskPopup } from '../risk-popup/risk-popup';
import { Hazard } from '../dashboard/dashboard';
import { HazardService } from '../hazard-service/hazard-service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.html',
  styleUrls: ['./map1.scss']
})
export class Map implements AfterViewInit, OnInit {

  private map: L.Map | undefined;
  public selectedHazard: Hazard | null = null;
  private centroid: L.LatLngExpression = [47.023333, 21.901944];

  private markersDictionary: { [id: string]: L.Marker } = {};

  constructor(private zone: NgZone, private hazardService: HazardService) { }

  ngOnInit(): void {
    this.hazardService.hazardRemoved$.subscribe(deletedId => {
      // 1. Find the marker in our dictionary
      const markerToRemove = this.markersDictionary[deletedId];

      if (markerToRemove) {
        // 2. Tell Leaflet to remove it from the map visually
        markerToRemove.remove();

        // 3. Clean it out of our dictionary
        delete this.markersDictionary[deletedId];
        console.log(`Marker ${deletedId} erased from the map!`);
      }
    });
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.renderHardcodedMarkers();
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: this.centroid,
      zoom: 15.4 // Zoom-ul ușor mărit pentru a vedea mai bine pista
    });

    const tiles = L.tileLayer('https://api.maptiler.com/maps/outdoor-v4/{z}/{x}/{y}.png?key=MCpv62w29YtAq27NFF3G', {
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

      const id = (marker as any).hazardId;
      if (id) {
        this.markersDictionary[id] = marker;
      }

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
