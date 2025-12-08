package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.SQLAuthDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler
 {

    private final ConnectionManager connections = new ConnectionManager();
    private final Map<Session, String> usernames = new ConcurrentHashMap<>();
    private final Map<Session, Integer> games = new ConcurrentHashMap<>();
     private final Map<Integer, GameData> activeGames = new ConcurrentHashMap<>();
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
         Session session = ctx.session;
         AuthData auth;

        try {
            auth = authDAO.getAuth(cmd.getAuthToken());
        } catch (DataAccessException e) {
            sendError(session, "Error: invalid auth token");
            return;
        }
        String username = auth.username();
        GameData game = activeGames.get(cmd.getGameID());
        if (game == null) {
            for (GameData g : gameDAO.listGames()) {
                if (g.gameID() == cmd.getGameID()) {
                    game = g;
                    activeGames.put(cmd.getGameID(), g);
                    break;
                }
            }

            if (game == null) {
                sendError(session, "Error: game does not exist");
                return;
            }
        }


        ChessGame chess = game.game();

        if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
            sendError(session, "Error: only players can move");
            return;
        }

        ChessGame.TeamColor playerColor;
        if (username.equals(game.whiteUsername())) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else {
            playerColor = ChessGame.TeamColor.BLACK;
        }

        if (chess.getTeamTurn() != playerColor) {
            sendError(session, "Error: not your turn");
            return;
        }

        try {
            chess.makeMove(cmd.getMove());
        } catch (InvalidMoveException e) {
            sendError(session, "Error: not a valid move");
            return;
        }

        GameData newGame = new GameData(
                game.gameID(),
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                chess
        );

        activeGames.put(game.gameID(), newGame)

        LoadGameMessage load = new LoadGameMessage(game);
        connections.broadcast(game.gameID(), new LoadGameMessage(newGame), null);


        String msg = playerColor + username + " moved" + cmd.getMove();
        NotificationMessage note = new NotificationMessage(msg);
        connections.broadcast(game.gameID(), new NotificationMessage(msg), session);
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

        GameData game;
        try {
            game = gameDAO.getGame(cmd.getGameID());
        } catch (DataAccessException e) {
            sendError(session, "Error: game does not exist");
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

     private void send(Session session, Object message) {
         try {
             String json = gson.toJson(message);
             session.getRemote().sendString(json);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


     private void sendError(Session session, String errorMessage) {
         String msg = errorMessage.toLowerCase().contains("error")
                 ? errorMessage
                 : "Error: " + errorMessage;

         NotificationMessage error = new NotificationMessage(msg);
         send(session, error);
     }



     @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
