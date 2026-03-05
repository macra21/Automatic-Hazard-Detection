import { Component, AfterViewInit } from '@angular/core';
import * as L from 'leaflet';
import { getMarkers } from './markers';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.html',
  styleUrls: ['./map.css']
})
export class Map implements AfterViewInit {

  private map: L.Map | undefined;

  private centroid: L.LatLngExpression = [47.023333, 21.901944];

  constructor() { }

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

    // Aplicăm logica din proiectul GitHub: preluăm array-ul și îl parcurgem
    const markers = getMarkers();

    markers.forEach(marker => {
      // Adăugăm markerul pe hartă
      marker.addTo(this.map!);

      // Opțional: Deschidem popup-ul automat pentru primul marker critic

        marker.bindPopup(`<div style="text-align: center">
            <img src="drum_test.jpg" alt="marker" style="text-align:center;width: 130px">
            <p>latitudine:47.029032 longitudine:21.903523</p></p>
            <br>
            </div>`).openPopup();

    });
  }
}
