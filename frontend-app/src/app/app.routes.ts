import { Routes } from '@angular/router';
import {Login} from './login-component/login';
import {Map} from './map/map'
import {Operator_page} from './operatorPage/operator_page';
import { Dashboard } from './dashboard/dashboard';
import {authGuard} from './auth-guard';
import {MaintenancePage} from './maintenancePage/maintenance_page';
import {AdminStatsComponent} from './adminPage/admin_page'

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
    path: 'operatorPage',
    component: Operator_page,
    canActivate: [authGuard]
  },
  {
    path:'maintenancePage',
    component:MaintenancePage,
    canActivate: [authGuard]
  },
  {
    path: 'adminPage',
    component:AdminStatsComponent,
  }
];
