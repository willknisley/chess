package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.net.URI;
import java.net.http.*;
import java.util.Collection;

import static server.ServerFacade.HttpMethod.*;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData login(String username, String password) throws Exception {
        var path = "/session";
        record LoginRequest(String username, String password) {}
        var request = new LoginRequest(username, password);
        return makeRequest(POST, path, request, AuthData.class, null);
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var path = "/user";
        record RegisterRequest(String username, String password, String email) {}
        var request = new RegisterRequest(username, password, email);
        return makeRequest(POST, path, request, AuthData.class, null);
    }
    public void logout(String authToken) throws Exception {
        var path = "/session";
        makeRequest(DELETE, path, null, null, authToken);
    }

    public GameData createGame(String name, String authToken) throws Exception {
        var path = "/game";
        record CreateGameRequest(String gameName) {}
        var request = new CreateGameRequest(name);
        return makeRequest(POST, path, request, GameData.class, authToken);
    }

    public Collection<GameData> listGames(String authToken) throws Exception {
        var path = "/game";
        record ListGamesResponse(Collection<GameData> games) {}
        ListGamesResponse response = makeRequest(GET, path, null, ListGamesResponse.class, authToken);
        assert response != null;
        return response.games();
    }

    public void joinGame(int gameID, String playerColor, String authToken) throws Exception {
        var path = "/game";
        record JoinGameRequest(String playerColor, int gameID) {}
        var request = new JoinGameRequest(playerColor, gameID);
        makeRequest(PUT, path, request, null, authToken);
    }

    public void observeGame(int gameID, String authToken) throws Exception {
        var path = "/game";
        record JoinGameRequest(String playerColor, int gameID) {}
        var request = new JoinGameRequest(null, gameID);
        makeRequest(PUT, path, request, null, authToken);
    }

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    private <T> T makeRequest(HttpMethod method, String path, Object request, Class<T> responseClass, String authToken) throws Exception {
        URI uri = URI.create(serverUrl + path);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(java.time.Duration.ofSeconds(5));

        if (authToken != null) {
            requestBuilder.header("authorization", authToken);
        }

        String jsonBody = (request != null)
                ? gson.toJson(request)
                : null;

        if (jsonBody != null) {
            requestBuilder.header("Content-Type", "application/json");
        }

        if (method == POST) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));

        } else if (method == PUT) {
            requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));

        } else if (method == HttpMethod.DELETE) {
            if (jsonBody != null) {
                requestBuilder.method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody));
            } else {
                requestBuilder.DELETE();
            }

        } else {
            requestBuilder.GET();
        }

        HttpRequest httpRequest = requestBuilder.build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new Exception("HTTP error " + response.statusCode());
        }

        if (responseClass != null && response.body() != null && !response.body().isEmpty()) {
            return gson.fromJson(response.body(), responseClass);
        }

        return null;
    }
}