package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import model.AuthData;
import service.ClearService;
import service.UserService;

import java.util.HashMap;

public class Server {

    private final Javalin javalin;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserService userService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
        userService = new UserService(userDAO, authDAO);


        // Register your endpoints and exception handlers here.

        javalin.delete("/db", ctx -> {
            try {
                new ClearService(userDAO, gameDAO, authDAO).clear_all();
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
                ctx.status(401);
                ctx.result("{\"message\": \"Error: unauthorized\"}");
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
