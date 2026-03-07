package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.utenteModel;
import controller.passwordHashingUtils; 

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class loginController {

    public String[] tryLogin(String email, String passwordInserita) {
        try {
            Gson gson = new Gson();

            JsonObject jsonParams = new JsonObject();
            jsonParams.addProperty("email", email);
            String jsonPayload = gson.toJson(jsonParams);

            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utenti/findByEmail")) 
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 && !response.body().isEmpty()) {
                
                utenteModel userFound = gson.fromJson(response.body(), utenteModel.class);
                
                String databaseHash = userFound.getPassword();
                
                boolean resultAccess = passwordHashingUtils.verifyPassword(passwordInserita, databaseHash);
                
                if (resultAccess) {
                    return new String[]{userFound.getRuolo(), userFound.getNome(), userFound.getEmail()};
                } else {
                    return null; 
                }
            } else {
                System.out.println("User not found.");
            }
            
        } catch (Exception e) {
            System.err.println("Server connection error: " + e.getMessage());
        }
        
        return null; 
    }
}