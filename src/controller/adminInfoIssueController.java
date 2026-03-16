package controller;

import com.google.gson.Gson;
import model.issueModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class adminInfoIssueController {

    public boolean updateIssue(issueModel issue) {
        try {
            Gson gson = new Gson();
            String jsonPayload = gson.toJson(issue);

            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/issues/" + issue.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            System.err.println("Update issue error: " + e.getMessage());
            return false;
        }
    }
}