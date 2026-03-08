import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// Definim structura datelor exact cum o așteaptă HTML-ul tău
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
  templateUrl: 'admin_page.html',
  styleUrls: ['admin_page.scss']
})
export class AdminStatsComponent implements OnInit {

  // Inițializăm lista goală. Se va umple automat când vin datele din DB.
  public statsList: SystemStat[] = [];

  // Injectăm HttpClient direct în componentă ca să putem vorbi cu backend-ul
  constructor(private http: HttpClient) {}

  // Această funcție rulează automat când se deschide pagina
  ngOnInit(): void {
    // AICI: Pune URL-ul real de la backend-ul tău pentru tabela responseTime
    const apiUrl = 'http://localhost:8080/api/responseTime';

    // Facem cererea către baza de date
    this.http.get<any[]>(apiUrl).subscribe({
      next: (dataDinDB) => {
        // Transformăm ce vine din backend în formatul așteptat de lista noastră
        this.statsList = dataDinDB.map(item => ({
          // Adaptează 'item.id', 'item.timestamp' etc. cu numele exacte ale coloanelor din baza de date
          id: item.id || item.ID || 0,
          date: item.timestamp ? new Date(item.timestamp).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : 'Acum',
          name: item.endpointName || item.name || 'Timp Răspuns',
          value: item.duration ? `${item.duration} ms` : 'N/A', // presupunem că ai o coloană 'duration'
          description: `Status: ${item.status || 'OK'}` // o mică descriere bazată pe date
        }));
      },
      error: (err) => {
        console.error('Eroare la încărcarea statisticilor din baza de date:', err);
      }
    });
  }
}
