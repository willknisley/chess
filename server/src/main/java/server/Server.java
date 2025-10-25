package server;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import service.ClearService;

public class Server {

    private final Javalin javalin;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();


        // Register your endpoints and exception handlers here.

        javalin.delete("/db", ctx -> {
            new ClearService(userDAO, gameDAO, authDAO).clear_all();
            ctx.result("{}");
            ctx.status(200);
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
