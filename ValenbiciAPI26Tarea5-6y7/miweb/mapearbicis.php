<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mapa de Estaciones Valenbisi</title>
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
  <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
  <style>
    #map { height: 800px; width: 100%; margin-top: 0px; }
    body { margin: 0; font-family: 'Times New Roman', Times, serif; text-align: center; background-color: #eaf4fb; }
    h1 {
      color: #fff;
      background-color: #1565c0;
      padding: 20px;
      margin: 0;
      font-size: 24px;
    }
    .boton-volver {
      display: inline-block;
      margin: 20px auto;
      padding: 12px 25px;
      background-color:  #b82cd4ff;
      color: white;
      text-decoration: none;
      border-radius: 8px;
      font-size: 1em;
      font-weight: bold;
    }
    .boton-volver:hover { background-color:  #ec35ecff; }
  </style>
</head>
<body>
  <h1>Mapa de Estaciones ValenBisi</h1>
  <div id="map"></div>
  <br>
  <a href="index.php" class="boton-volver">Volver al listado</a>

  <script>
    var map = L.map('map').setView([39.47, -0.37], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    function getMarkerColor(available) {
      if (available < 5) {
        return 'red';
      } else if (available >= 5 && available < 10) {
        return 'orange';
      } else if (available >= 10 && available < 20) {
        return 'yellow';
      } else {
        return 'green';
      }
    }

    fetch('data.json')
      .then(response => {
        if (!response.ok) {
          throw new Error(`Error al cargar data.json: ${response.statusText}`);
        }
        return response.json();
      })
      .then(data => {
        Object.values(data).forEach(station => {
          const { latitude, longitude, address, available, free, total } = station;
          if (latitude && longitude) {
            L.circleMarker([latitude, longitude], {
              color: getMarkerColor(available),
              fillColor: getMarkerColor(available),
              radius: 8,
              fillOpacity: 0.8
            })
            .addTo(map)
            .bindPopup(`
              <strong>${address}</strong><br>
              <b>Disponibles:</b> ${available}<br>
              <b>Libres:</b> ${free}<br>
              <b>Total:</b> ${total}
            `);
          }
        });
      })
      .catch(error => {
        console.error('Error cargando los datos:', error);
      });
  </script>
</body>
</html>