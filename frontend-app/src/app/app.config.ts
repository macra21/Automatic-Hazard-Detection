// app.config.ts
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes'; // Asigură-te că importi fișierul de rute

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes) // Aceasta activează [router-outlet]
  ]
};
