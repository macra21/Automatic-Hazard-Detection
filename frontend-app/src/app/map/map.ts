import {Component, AfterViewInit, NgZone, OnInit} from '@angular/core';
import * as L from 'leaflet';
import { HazardService, Hazard } from '../hazard-service/hazard-service';

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
  private pendingHazards: Hazard[] = []; // Buffer for hazards received before map init

  constructor(private zone: NgZone, private hazardService: HazardService) { }

  ngOnInit(): void {
    // Listen for removed hazards to remove markers
    this.hazardService.hazardRemoved$.subscribe(deletedId => {
      const markerToRemove = this.markersDictionary[deletedId];
      if (markerToRemove) {
        markerToRemove.remove();
        delete this.markersDictionary[deletedId];
        console.log(`Marker ${deletedId} erased from the map!`);
      }
    });

    // Listen for new hazards to add markers
    this.hazardService.newHazard$.subscribe(newHazard => {
      console.log('Map received new hazard', newHazard);
      if (this.map) {
        this.addMarkerForHazard(newHazard);
      } else {
        console.log('Map not ready, buffering hazard', newHazard.id);
        this.pendingHazards.push(newHazard);
      }
    });
  }

  ngAfterViewInit(): void {
    this.initMap();

    // Process any buffered hazards
    this.pendingHazards.forEach(h => this.addMarkerForHazard(h));
    this.pendingHazards = [];

    // Also load any existing hazards from the service (in case we missed the initial load)
    const existingHazards = this.hazardService.getHazards();
    existingHazards.forEach(h => this.addMarkerForHazard(h));
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

  private addMarkerForHazard(hazard: Hazard): void {
    if (!this.map || !hazard.coordinates) return;

    // Check if marker already exists to avoid duplicates
    if (this.markersDictionary[hazard.id]) {
        return;
    }

    const marker = L.marker([hazard.coordinates.lat, hazard.coordinates.lng], {
      icon: L.icon({
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        iconUrl: 'marker.gif', // Ensure this asset exists
      }),
      title: hazard.description
    });

    (marker as any).hazardId = hazard.id;
    marker.addTo(this.map!);
    this.markersDictionary[hazard.id] = marker;

    marker.on('click', () => {
      this.zone.run(() => {
        this.hazardService.selectHazard(hazard);
      });
    });
  }
}
