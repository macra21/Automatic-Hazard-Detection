import * as L from 'leaflet';

export const getMarkers = (): L.Marker[] => {
  return [
    // Marker pentru o problemă critică (Roșu)
    new L.Marker(new L.LatLng(47.022594, 21.901626), {
      icon: new L.Icon({
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        iconUrl: 'marker.gif', // Asigură-te că ai fișierul în assets
      }),
      title: 'Fisură Detectată - Secțiunea A1'
    }),
    // Marker pentru un obiect străin (Albastru/Galben)
    new L.Marker(new L.LatLng(47.029032, 21.903523), {
      icon: new L.Icon({
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        iconUrl: 'marker.gif',
      }),
      title: 'Obiect Străin (FOD) - Centru Pistă'
    })
  ];
};
