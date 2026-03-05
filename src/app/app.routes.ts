import { Routes } from '@angular/router';
import {Login} from './login-component/login';
import {Map} from './map/map'
import {Page} from './page/page';
import { Dashboard } from './dashboard/dashboard';

export const routes: Routes = [
  {
    path: '',
    component: Login,
    pathMatch: 'full' // Recomandat pentru ruta principală
  },
  {
    path: 'page',
    component: Page
  },
  // Altele...
];
