import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { DataService } from './dataService'; // Verifică path-ul corect către fișierul tău

export const authGuard: CanActivateFn = (route, state) => {
  const dataService = inject(DataService);
  const router = inject(Router);

  if (dataService.isLoggedIn()) {
    return true; // Permite accesul la hartă/dronă
  } else {
    // Dacă userul nu e logat, îl trimitem forțat la pagina de login
    return router.parseUrl('/login');
  }
};
