import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

// Structura exactă cerută de HTML-ul tău
export interface SystemStat {
  id: number;
  date: string;
  name: string;
  value: string | number;
  description: string;
}

@Component({
  selector: 'admin-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: 'admin_page.html',
  styleUrls: ['admin_page.scss']
})
export class AdminStatsComponent implements OnInit {

  // Lista care va fi afișată în HTML
  public statsList: SystemStat[] = [];

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef // Adăugat pentru a rezolva problema cu neafișarea pe ecran
  ) {}

  ngOnInit(): void {
    const apiUrl = 'http://localhost:8080/api/responseTime/getResponseTimes';

    this.http.get<any>(apiUrl).subscribe({
      next: (dataDinDB) => {
        console.log('Date brute de la backend:', dataDinDB);

        // Extragem lista direct (dacă Spring a ascuns-o în .content sau .data)
        const listaDinBackend = dataDinDB?.content || dataDinDB?.data || dataDinDB;

        // Verificăm să fim siguri că este un array înainte să îi dăm map()
        if (Array.isArray(listaDinBackend)) {
          this.statsList = listaDinBackend.map((item: any) => {
            return {
              id: item.id || item.ID || 0,
              date: item.update_date ? new Date(item.update_date).toLocaleString('ro-RO') : 'Acum',
              name: `Hazard status actualizat`,
              value: item.response_time != null ? `${item.response_time}s` : 'N/A',
              description: `Stare: ${item.initial_status} ➔ ${item.update_status}`
            };
          });

          console.log('Lista finală trimisă către HTML:', this.statsList);
        } else {
          console.error('Nu s-a putut extrage un Array din răspunsul backend-ului:', dataDinDB);
          this.statsList = [];
        }

        // CRITIC: Spunem Angular-ului să redeseneze HTML-ul acum că avem datele
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Eroare la încărcarea statisticilor din baza de date:', err);
      }
    });
  }
}
