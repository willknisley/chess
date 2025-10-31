package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import model.AuthData;
import model.GameData;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final SQLUserDAO userDAO;
    private final SQLGameDAO gameDAO;
    private final SQLAuthDAO authDAO;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(userDAO, gameDAO, authDAO);
        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            try {
                new ClearService(userDAO, gameDAO, authDAO).clearAll();
                ctx.result("{}");
                ctx.status(200);
            } catch (Exception e) {
                ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
                ctx.status(500);
            }
        });

        javalin.post("/user", ctx -> {
            try {
                var serializer = new Gson();
                var json = ctx.body();
                var hashMap = serializer.fromJson(json, HashMap.class);

                String username = (String) hashMap.get("username");
                String password = (String) hashMap.get("password");
                String email = (String) hashMap.get("email");
                if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
                    ctx.result("{\"message\": \"Error: bad request\"}");
                    ctx.status(400);
                    return;
                }
                AuthData result = userService.register(username, password, email);
                var responseJson = serializer.toJson(result);
                ctx.result(responseJson);
                ctx.status(200);
            } catch (DataAccessException e) {
                if (e.getMessage().contains("already exists")) {
                    ctx.status(403);
                    ctx.result("{\"message\": \"Error: already taken\"}");
                } else {
                    ctx.status(500);
                    ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
                }
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
            }
        });
        javalin.post("/session", ctx -> {
            try {
                var serializer = new Gson();
                var json = ctx.body();
                var hashMap = serializer.fromJson(json, HashMap.class);

                String username = (String) hashMap.get("username");
                String password = (String) hashMap.get("password");
                if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                    ctx.result("{\"message\": \"Error: bad request\"}");
                    ctx.status(400);
                    return;
                }
                AuthData result = userService.login(username, password);
                var responseJson = serializer.toJson(result);
                ctx.result(responseJson);
                ctx.status(200);
            } catch (DataAccessException e) {
                if (e.getMessage().contains("username does not exist") || e.getMessage().contains("Wrong password") || e.getMessage().contains("unauthorized")) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                } else {
                    ctx.status(500);
                    ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
                }
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
            }
        });
        javalin.delete("/session", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                if (authToken == null || authToken.isEmpty()) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                    return;
                }
                userService.logout(authToken);
                ctx.status(200);
                ctx.result("{}");
            } catch (DataAccessException e) {
                if (e.getMessage().equals("authToken does not exist")) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                } else {
                    ctx.status(500);
                    ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
                }
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
            }
        });
        javalin.get("/game", ctx -> {
            try {
                var serializer = new Gson();
                String authToken = ctx.header("authorization");
                if (authToken == null || authToken.isEmpty()) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                    return;
                }
                Collection<GameData> games = gameService.returnGames(authToken);
                Map<String, Object> mapFix = Map.of("games", games);
                ctx.status(200);
                ctx.result(serializer.toJson(mapFix));
            } catch (DataAccessException e) {
                if (e.getMessage().equals("authToken does not exist")) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                } else {
                    ctx.status(500);
                    ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
                }
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
             }
        });
        javalin.post("/game", ctx -> {
            try {
                var serializer = new Gson();
                String authToken = ctx.header("authorization");

                if (authToken == null || authToken.isEmpty()) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                    return;
                }
                var json = ctx.body();
                var hashMap = serializer.fromJson(json, HashMap.class);
                String gameName = (String) hashMap.get("gameName");
                if (gameName == null || gameName.isEmpty()) {
                    ctx.status(400);
                    ctx.result("{\"message\": \"Error: bad request\"}");
                    return;
                }
                int gameID = gameService.createGame(authToken, gameName);
                ctx.status(200);
                ctx.result("{\"gameID\": " + gameID + "}");
            } catch (DataAccessException e) {
                if (e.getMessage().equals("authToken does not exist")) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                } else {
                    ctx.status(500);
                    ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
                }
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
            }
        });
        javalin.put("/game", ctx -> {
            try {
                var serializer = new Gson();
                String authToken = ctx.header("authorization");
                if (authToken == null || authToken.isEmpty()) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                    return;
                }
                var json = ctx.body();
                var hashMap = serializer.fromJson(json, HashMap.class);
                String playerColor = (String) hashMap.get("playerColor");
                Number gameIDNum = (Number) hashMap.get("gameID");
                if (playerColor == null || playerColor.isEmpty() || (hashMap.get("gameID") == null)) {
                    ctx.status(400);
                    ctx.result("{\"message\": \"Error: bad request\"}");
                    return;
                }
                int gameID = gameIDNum.intValue();
                gameService.joinGame(authToken, gameID, playerColor);
                ctx.status(200);
                ctx.result("{}");
            } catch (DataAccessException e) {
                if (e.getMessage().contains("taken")) {
                    ctx.status(403);
                    ctx.result("{\"message\": \"Error: already taken\"}");
                } else if (e.getMessage().contains("Invalid color") || e.getMessage().contains("Game does not exist")) {
                    ctx.status(400);
                    ctx.result("{\"message\": \"Error: bad request\"}");
                } else if (e.getMessage().equals("authToken does not exist")) {
                    ctx.status(401);
                    ctx.result("{\"message\": \"Error: unauthorized\"}");
                } else {
                    ctx.status(500);
                    ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
                }
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("{\"message\": \"Error: " + e.getMessage() + "\"}");
            }
        });
    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }
}
