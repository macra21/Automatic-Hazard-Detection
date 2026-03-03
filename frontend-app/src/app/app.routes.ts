import { Routes } from '@angular/router';
import {LoginComponent} from './login-component/login-component';
import {MapComponent} from './map/map.component'
export const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
    pathMatch: 'full' // Recomandat pentru ruta principală
  },
  {
    path: 'map',
    component: MapComponent
  }
  // Altele...
];
