import { Routes } from '@angular/router';
import {Login} from './login-component/login';
import {Map} from './map/map'
import {Page} from './page/page';
import { Dashboard } from './dashboard/dashboard';
import {authGuard} from './auth-guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'page',
    component: Page,
    canActivate: [authGuard] // "Lacătul" este aici
  }
];
