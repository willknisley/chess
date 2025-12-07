package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.SQLAuthDAO;
import exception.ResponseException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.Action;
import webSocketMessages.Notification;
import websocket.commands.UserGameCommand;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WebSocketHandler<LoadGameMessage> implements WsConnectHandler, WsMessageHandler, WsCloseHandler
 {

    private final ConnectionManager connections = new ConnectionManager();
    Map<Session, String> usernames = new ConcurrentHashMap<>();
    Map<Session, Integer> games = new ConcurrentHashMap<>();
    Map<Integer, List<Session>> sessionsByGame = new ConcurrentHashMap<>();
     private final Gson gson = new Gson();
     private final SQLAuthDAO authDAO;
     private final GameDAO gameDAO;

     public WebSocketHandler(SQLAuthDAO authDAO, GameDAO gameDAO) {
         this.authDAO = authDAO;
         this.gameDAO = gameDAO;
     }

     @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand cmd = gson.fromJson(ctx.message(), UserGameCommand.class);
            switch (cmd.getCommandType()) {
                case CONNECT -> handleConnectCommand(cmd, ctx);
                case MAKE_MOVE -> handleMakeMove(cmd, ctx);
                case LEAVE -> handleLeave(cmd, ctx);
                case RESIGN -> handleResign(cmd, ctx);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleMakeMove(UserGameCommand cmd, WsMessageContext ctx) {
    }

    private void handleLeave(UserGameCommand cmd, WsMessageContext ctx) {
    }

    private void handleResign(UserGameCommand cmd, WsMessageContext ctx) {
    }

    private void handleConnectCommand(UserGameCommand cmd, WsMessageContext ctx) {
        Session session = ctx.session;
        AuthData auth;

        try {
            auth = authDAO.getAuth(cmd.getAuthToken());
        } catch (DataAccessException e) {
            sendError(session, "Error: invalid auth token");
            return;
        }
        String username = auth.username();

        GameData game = gameDAO.games.get(cmd.getGameID());
        if (game == null) {
            sendError(session, "Error: Game does not exist");
            return;
        }

        usernames.put(session, username);
        games.put(session, cmd.getGameID());
        connections.addToGame(cmd.getGameID(), session);

        LoadGameMessage load = new LoadGameMessage(game);
        send(session, load);

        String msg = username + " connected to the game.";
        NotificationMessage note = new NotificationMessage(msg);
        connections.broadcast(cmd.getGameID(), note, session);

    }

    private void send(Session session, LoadGameMessage loadGameMessage) {
    }

    private void sendError(Session session, String s) {
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void enter(String visitorName, Session session) throws IOException {
        connections.add(session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(session, notification);
    }

    private void exit(String visitorName, Session session) throws IOException {
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }

    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast(null, notification);
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
}
