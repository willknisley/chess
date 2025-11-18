package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData login(String username, String password) {
    }

    public AuthData register(String username, String password, String email) {
    }
    public UserData logout(String authToken) {

    }

    public AuthData createGame(String name, String authToken) {
    }

    public Object listGames(String authToken) {
    }

    public void joinGame(int gameID, String playerColor, String authToken) {
    }

    public void observeGame(int gameID, String authToken) {
    }
}