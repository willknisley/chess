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
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM game");
            statement.executeUpdate("ALTER TABLE game AUTO_INCREMENT = 1");
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing games", e);
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
        if (playerColor == null) {
            throw new DataAccessException("Invalid color");
        }

        playerColor = playerColor.trim().toUpperCase();

        var sqlJoinGame = "SELECT * FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlJoinGame)) {

            statement.setInt(1, gameID);
            var rs = statement.executeQuery();
            if (!rs.next()) {
                throw new DataAccessException("Game does not exist");
            }

            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername");

            if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
                throw new DataAccessException("Invalid color");
            }

            if (playerColor.equals("WHITE") && whiteUsername != null) {
                throw new DataAccessException("already taken");
            }
            if (playerColor.equals("BLACK") && blackUsername != null) {
                throw new DataAccessException("already taken");
            }

            String updateSql = playerColor.equals("WHITE")
                    ? "UPDATE game SET whiteUsername = ? WHERE gameID = ?"
                    : "UPDATE game SET blackUsername = ? WHERE gameID = ?";

            try (var updateStatement = conn.prepareStatement(updateSql)) {
                updateStatement.setString(1, username);
                updateStatement.setInt(2, gameID);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error joining game", e);
        }
    }



    public void updateGame(GameData game) throws DataAccessException {
        var sql = "UPDATE game SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {

            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, new Gson().toJson(game.game()));
            statement.setInt(4, game.gameID());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game", e);
        }
    }


}
