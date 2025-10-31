package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class SQLGameDAO {
    public void clear() throws DataAccessException{
        var sqlClear = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlClear)) {
            statement.executeUpdate();
        } catch (
                SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public int createGame(String gameName) {
        int gameID = gameIdCount;
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, game);
        gameIdCount++;
        return gameID;
    }

    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException {
        GameData game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game does not exist");
        }
        GameData updatedGame;
        if(!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            throw new DataAccessException("Invalid color");
        }
        if (playerColor.equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new DataAccessException("Color already taken");
            }
            updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else if (playerColor.equals("BLACK")) {
            if (game.blackUsername() != null) {
                throw new DataAccessException("Color already taken");
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        } else {
            throw new DataAccessException("Not a color");
        }
        games.put(gameID, updatedGame);
    }
}
