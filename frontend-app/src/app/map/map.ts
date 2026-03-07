import { Component, AfterViewInit, NgZone, OnInit } from '@angular/core';
import * as L from 'leaflet';
import * as turf from '@turf/turf';
import { HttpClient } from '@angular/common/http';
import { getMarkers } from './markers';
import { RiskPopup } from '../risk-popup/risk-popup';
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
  private centroid: L.LatLngExpression = [47.023875, 21.901898]; // Setat pe Oradea

  private markersDictionary: { [id: string]: L.Marker } = {};

  // REPARATIE: Am adus inapoi buffer-ul ca sa nu pierzi markerele la incarcare
  private pendingHazards: Hazard[] = [];

  // --- VARIABILE NOI PENTRU DRONĂ ---
  private allDetectedHazards: Hazard[] = [];
  private droneMarker: L.Marker | undefined;
  private flightPathPolyline: L.Polyline | undefined;
  private animationInterval: any;

  constructor(
    private zone: NgZone,
    private hazardService: HazardService,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    // REPARATIE SINCRONIZARE ȘTERGERE: Am pus la loc ascultătorul pentru remove
    this.hazardService.hazardRemoved$.subscribe(deletedId => {
      // 1. Ștergem de pe hartă (dacă a fost deja pus)
      const markerToRemove = this.markersDictionary[deletedId];
      if (markerToRemove) {
        markerToRemove.remove();
        delete this.markersDictionary[deletedId];
      }

      // 2. Ștergem și din lista dronei (ca să nu îl mai "descopere" dacă tu l-ai șters deja din dreapta)
      this.allDetectedHazards = this.allDetectedHazards.filter(h => h.id !== deletedId);

      console.log(`Sincronizare: Marker ${deletedId} a fost șters cu succes!`);
    });

    // REPARATIE SINCRONIZARE ADĂUGARE
    this.hazardService.newHazard$.subscribe(newHazard => {
      console.log('Hazard primit de la AI:', newHazard);

      // Dacă harta e gata, îl punem instant. Dacă nu, îl băgăm în așteptare.
      if (this.map) {
        this.addMarkerForHazard(newHazard);
      } else {
        this.pendingHazards.push(newHazard);
      }

      // Îl punem și în lista dronei, conform logicii tale
      this.allDetectedHazards.push(newHazard);
    });
  }

  ngAfterViewInit(): void {
    this.initMap();

    // REPARATIE: Aici descărcăm buffer-ul după ce harta s-a încărcat complet
    this.pendingHazards.forEach(h => this.addMarkerForHazard(h));
    this.pendingHazards = [];
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: this.centroid,
      zoom: 15.4
    });

    const tiles = L.tileLayer('https://api.maptiler.com/maps/outdoor-v4/{z}/{x}/{y}.png?key=MCpv62w29YtAq27NFF3G', {
      maxZoom: 18,
      minZoom: 10,
    });

    tiles.addTo(this.map);
  }

  // FUNCȚIA TA ORIGINALĂ
  private addMarkerForHazard(hazard: Hazard): void {
    if (!this.map || !hazard.coordinates) return;

    if (this.markersDictionary[hazard.id]) return;

    const marker = new L.Marker([hazard.coordinates.lat, hazard.coordinates.lng], {
      icon: new L.Icon({
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        iconUrl: 'marker.gif',
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

  // =====================================================================
  // --- MAGIA PENTRU DRONĂ ÎNCEPE AICI ---
  // =====================================================================

  public async startAirportScan(iataCode: string): Promise<void> {
    if (!this.map || !iataCode) return;

    // MAGIA AICI: Am schimbat "icao" cu "iata" în query-ul către baza de date
    const query = `[out:json];nwr["iata"="${iataCode.toUpperCase()}"];way(around:3000)["aeroway"="runway"];out geom;`;
    const url = `https://overpass-api.de/api/interpreter?data=${encodeURIComponent(query)}`;

    this.http.get(url).subscribe({
      next: (res: any) => {
        if (!res.elements || res.elements.length === 0) {
          alert(`Pista pentru aeroportul cu codul IATA ${iataCode.toUpperCase()} nu a fost găsită pe harta OSM.`);
          return;
        }

        const runwayCoords = res.elements[0].geometry.map((p: any) => [p.lon, p.lat]);
        runwayCoords.push(runwayCoords[0]);
        const runwayPolygon = turf.polygon([runwayCoords]);

        const path = this.generateZigZagPath(runwayPolygon);
        this.runDroneAnimation(path);
      },
      error: (err) => {
        console.error("Eroare la obținerea pistei:", err);
      }
    });
  }

  private generateZigZagPath(polygon: any): L.LatLng[] {
    const bbox = turf.bbox(polygon);
    const step = 0.00015; // Lățimea rândului
    const path: L.LatLng[] = [];
    let currentLat = bbox[1];
    let reverse = false;

    while (currentLat <= bbox[3]) {
      const line = turf.lineString([[bbox[0], currentLat], [bbox[2], currentLat]]);
      const intersect = turf.lineIntersect(line, polygon);

      if (intersect.features.length >= 2) {
        let coords = intersect.features.map(f => (f.geometry as any).coordinates);
        coords.sort((a, b) => a[0] - b[0]);

        if (reverse) coords.reverse();

        const startPoint = coords[0];
        const endPoint = coords[1];

        const frames = 10;
        for (let i = 0; i <= frames; i++) {
          const lon = startPoint[0] + (endPoint[0] - startPoint[0]) * (i / frames);
          const lat = startPoint[1] + (endPoint[1] - startPoint[1]) * (i / frames);
          path.push(L.latLng(lat, lon));
        }

        reverse = !reverse;
      }
      currentLat += step;
    }
    return path;
  }

  private runDroneAnimation(path: L.LatLng[]): void {
    if (path.length === 0) return;
    if (this.animationInterval) clearInterval(this.animationInterval);
    if (this.flightPathPolyline) this.map?.removeLayer(this.flightPathPolyline);

    this.flightPathPolyline = L.polyline([], { color: '#00ff00', weight: 2, dashArray: '5, 10' }).addTo(this.map!);

    const droneIcon = L.icon({
      iconUrl: 'Drone_1.gif',
      iconSize: [30, 30],
      iconAnchor: [15, 15]
    });

    if(this.droneMarker) {
      this.map?.removeLayer(this.droneMarker);
    }
    this.droneMarker = L.marker(path[0], { icon: droneIcon, zIndexOffset: 1000 }).addTo(this.map!);
    this.map?.fitBounds(L.polyline(path).getBounds());

    let index = 0;
    this.animationInterval = setInterval(() => {
      if (index >= path.length) {
        clearInterval(this.animationInterval);
        console.log("Scanare finalizată!");
        return;
      }

      const currentPos = path[index];
      this.droneMarker?.setLatLng(currentPos);
      this.flightPathPolyline?.addLatLng(currentPos);

      this.checkHazardsProximity(currentPos);

      index++;
    }, 100);
  }

  private checkHazardsProximity(dronePos: L.LatLng): void {
    this.allDetectedHazards.forEach(hazard => {
      if (!this.markersDictionary[hazard.id] && hazard.coordinates) {
        const hazardPos = L.latLng(hazard.coordinates.lat, hazard.coordinates.lng);
        const distance = dronePos.distanceTo(hazardPos);

        if (distance < 50) {
          this.addMarkerForHazard(hazard);
        }
      }
    });
  }
}

