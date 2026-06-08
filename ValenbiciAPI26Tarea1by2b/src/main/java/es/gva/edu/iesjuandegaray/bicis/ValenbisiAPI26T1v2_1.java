package es.gva.edu.iesjuandegaray.bicis;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ValenbisiAPI26T1v2_1 {

	// https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query?where=1=1&outFields=*&f=json
	 private static final String API_URL =
	            "https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query"
	            + "?where=1%3D1"
	            + "&outFields=*"
	            + "&returnGeometry=true"
	            + "&f=json";

	 public static void main(String[] args) {

	        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

	            HttpGet request = new HttpGet(API_URL);
	            HttpResponse response = httpClient.execute(request);

	            HttpEntity entity = response.getEntity();

	            if (entity != null) {

	                String result = EntityUtils.toString(entity);

	                JSONObject jsonObject = new JSONObject(result);
	                JSONArray features = jsonObject.getJSONArray("features");

	                System.out.println("Número de estaciones: " + features.length());
	                System.out.println();

	                for (int i = 0; i < features.length(); i++) {

	                    JSONObject feature = features.getJSONObject(i);
	                    JSONObject attributes = feature.getJSONObject("attributes");

	                    JSONObject geometryJson = feature.getJSONObject("geometry");
	                    Geometry geometry = new Geometry();
	                    geometry.x = geometryJson.getDouble("x");
	                    geometry.y = geometryJson.getDouble("y");

	                    System.out.println("--- Estación " + (i + 1) + " ---");
	                    System.out.println("ID: " + attributes.opt("gid"));
	                    System.out.println("Nombre: " + attributes.opt("name"));
	                    System.out.println("Número: " + attributes.opt("number_"));
	                    System.out.println("Dirección: " + attributes.opt("address"));
	                    System.out.println("Bicis disponibles: " + attributes.opt("available"));
	                    System.out.println("Espacios disponibles: " + attributes.opt("free"));
	                    System.out.println("Total anclajes: " + attributes.opt("total"));
	                    System.out.println("Estado: " + attributes.opt("open"));
	                    System.out.println("Coordenada X: " + geometry.x);
	                    System.out.println("Coordenada Y: " + geometry.y);
	                    System.out.println();
	                }
	            }

	        } catch (IOException e) {
	            System.out.println("Error en la petición HTTP:");
	            e.printStackTrace();
	        } catch (Exception e) {
	            System.out.println("Error procesando JSON:");
	            e.printStackTrace();
	        }
	    }
	}
