package controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class loginController {

    public boolean tryLogin(String email, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utenti"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonAnswr = response.body();
                
                if (jsonAnswr.contains("\"email\":\"" + email + "\"") && 
                		jsonAnswr.contains("\"password\":\"" + password + "\"")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Errore di connessione al server: " + e.getMessage());
        }
        
        return false; 
    }
}