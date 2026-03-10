package controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.issueModel;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class showIssueController {

    public List<issueModel> getAllIssues() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/issues"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {                
                Gson gson = new GsonBuilder()	//Reverting the stored image's bytes
                    .registerTypeAdapter(byte[].class, new JsonDeserializer<byte[]>() {
                        @Override
                        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return Base64.getDecoder().decode(json.getAsString());
                        }
                    })
                    .create();
                    
                Type listType = new TypeToken<ArrayList<issueModel>>(){}.getType();
                return gson.fromJson(response.body(), listType);
            }
        } catch (Exception e) {
            System.err.println("Server connection error: " + e.getMessage());
        }
        
        return new ArrayList<>(); 
    }
}