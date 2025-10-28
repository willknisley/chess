package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO {
    int gameID_count;
    HashMap<Integer, GameData> games = new HashMap<>();
    public void clear() {
        games.clear();
        gameID_count = 1;
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public int createGame(String gameName) {
        int gameID = gameID_count;
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, game);
        gameID_count++;
        return gameID;
    }

}
