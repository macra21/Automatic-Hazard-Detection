import * as L from 'leaflet';

export const getMarkers = (): L.Marker[] => {

    // Marker pentru o problemă critică (Roșu)
    const marker1 = new L.Marker(new L.LatLng(47.022594, 21.901626), {
      icon: new L.Icon({
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        iconUrl: 'marker.gif', // Asigură-te că ai fișierul în assets
      }),
      title: 'Fisură Detectată - Secțiunea A1'
    });
    // Marker pentru un obiect străin (Albastru/Galben)
    const marker2 = new L.Marker(new L.LatLng(47.029032, 21.903523), {
      icon: new L.Icon({
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        iconUrl: 'marker.gif',
      }),
      title: 'Obiect Străin (FOD) - Centru Pistă'
    });
    (marker1 as any).hazardId = '004';
    (marker2 as any).hazardId = '003';

    return [marker1, marker2];

};
