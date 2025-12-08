package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;



public class ConnectionManager {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Session, Session>> games = new ConcurrentHashMap<>();


    public void add(Session session) {
        connections.put(session, session);
    }

    public void remove(Session session) {
        connections.remove(session);

        for (var entry : games.values()) {
            entry.remove(session);
        }
    }

    public void addToGame(Integer gameID, Session session) {
        games.computeIfAbsent(gameID, id -> new ConcurrentHashMap<>())
                .put(session, session);
        connections.put(session, session);
    }

    public void broadcast(Integer gameID, Object message, Session exclude) {
            var gameSessions = games.get(gameID);
            if (gameSessions == null) return;

            String json = new Gson().toJson(message);

            for (Session s : gameSessions.values()) {
                if (s.isOpen() && !s.equals(exclude)) {
                    try {
                        s.getRemote().sendString(json);
                    } catch (Exception ignored) {}
                }
            }
    }
}
