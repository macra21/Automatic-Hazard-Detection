import { Component, AfterViewInit } from '@angular/core';
import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'] // Am folosit .css în loc de .scss
})
export class MapComponent implements AfterViewInit {

  private map: L.Map | undefined;

  // Coordonatele centrale și nivelul de zoom din exemplu (Aeroport Oradea)
  private centroid: L.LatLngExpression = [47.026739, 21.901495];

  private initMap(): void {
    // Inițializează harta pe elementul cu ID-ul 'map'
    this.map = L.map('map', {
      center: this.centroid,
      zoom: 15.2
    });

    // Adaugă stratul de hărți (tile layer) de la OpenStreetMap
    const tiles = L.tileLayer('https://api.maptiler.com/maps/hybrid-v4/{z}/{x}/{y}.jpg?key=MCpv62w29YtAq27NFF3G', {
      maxZoom: 18,
      minZoom: 10,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);


  }

  constructor() { }

  ngAfterViewInit(): void {
    this.initMap();
  }
}
