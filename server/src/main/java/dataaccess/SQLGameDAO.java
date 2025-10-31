package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SQLGameDAO {
    public void clear() throws DataAccessException{
        var sqlClear = "DELETE FROM game";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlClear)) {
            statement.executeUpdate();
        } catch (
                SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        var sqlListGames = "SELECT * FROM game";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlListGames)) {
            var rs = statement.executeQuery();
            ArrayList<GameData> gamesList = new ArrayList<>();

            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String gameJson = rs.getString("game");

                ChessGame chessGame = new Gson().fromJson(gameJson, ChessGame.class);
                GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                gamesList.add(gameData);
            }
            return gamesList;
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

    public int createGame(String gameName) throws DataAccessException {
        var sqlCreateGame = "INSERT INTO game (gameName, game) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlCreateGame, Statement.RETURN_GENERATED_KEYS)) {
            ChessGame newGame = new ChessGame();
            String gameJson = new Gson().toJson(newGame);

            statement.setString(1, gameName);
            statement.setString(2, gameJson);

            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new DataAccessException("Failed to get gameID");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException {
        var sqlJoinGame = "SELECT * FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlJoinGame)) {
            statement.setInt(1, gameID);
            var rs = statement.executeQuery();
            if (!rs.next()) {
                throw new DataAccessException(("Game doesn't exist"));
            }

            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername");

            if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
                throw new DataAccessException("Wrong player color");
            } else if (playerColor.equals("WHITE") && whiteUsername != null) {
                throw new DataAccessException("already taken");
            } else if (playerColor.equals("BLACK") && blackUsername != null) {
                throw new DataAccessException("already taken");
            }
            String newSql;
            if (playerColor.equals("WHITE")) {
            newSql = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
            } else {
                newSql = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
            }

            var updateStatement = conn.prepareStatement(newSql);
            updateStatement.setString(1, username);
            updateStatement.setInt(2, gameID);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

}
