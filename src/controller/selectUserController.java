package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.utenteModel;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class selectUserController {

    public List<utenteModel> getAllUsers() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utenti")) 
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<utenteModel>>(){}.getType();
                return gson.fromJson(response.body(), listType);
            }
        } catch (Exception e) {
            System.err.println("Server connection error: " + e.getMessage());
        }
        return new ArrayList<>(); 
    }
}