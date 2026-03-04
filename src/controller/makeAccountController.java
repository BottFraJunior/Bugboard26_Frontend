package controller;

import com.google.gson.Gson;
import model.utenteModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class makeAccountController {

    public boolean registerNewUser(utenteModel newUser) {
        try {
        	
            Gson gson = new Gson();
            String jsonPayload = gson.toJson(newUser);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utenti"))
                    .header("Content-Type", "application/json") 
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {		//200 OK or 201 Created
                return true;
            } else {
                System.err.println("Errore dal server: " + response.body());
            }

        } catch (Exception e) {
            System.err.println("Errore di connessione: " + e.getMessage());
        }
        return false;
    }
}