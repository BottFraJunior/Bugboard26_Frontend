package controller;

import com.google.gson.Gson;
import model.utenteModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class loginController {

    public String[] tryLogin(String email, String password) {
        try {
            String jsonPayload = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);

            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utenti/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 && !response.body().isEmpty()) {
                
                Gson gson = new Gson();
                utenteModel userFound = gson.fromJson(response.body(), utenteModel.class);
                
                return new String[]{userFound.getRuolo(), userFound.getNome()};
            }
        } catch (Exception e) {
            System.err.println("Errore di connessione al server: " + e.getMessage());
        }
        
        return null; 
    }
}