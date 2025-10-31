package dataaccess;

import model.AuthData;
import java.sql.SQLException;

public class SQLAuthDAO {

    public void clear () throws DataAccessException {
        var sqlClear = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlClear)) {
            statement.executeUpdate();
        } catch (
                SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        var sqlCreateAuth = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlCreateAuth)) {
            statement.setString(1, auth.authToken());
            statement.setString(2, auth.username());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

        public void deleteAuth(String token) throws DataAccessException {
            var sqlDeleteCheck = "SELECT * FROM auth WHERE authToken = ?";
            try (var conn = DatabaseManager.getConnection();
                 var firstStatement = conn.prepareStatement(sqlDeleteCheck)) {

                firstStatement.setString(1, token);
                var rs = firstStatement.executeQuery();

                if (!rs.next()) {
                    throw new DataAccessException("authToken does not exist");
                }

                var sqlDelete = "DELETE FROM auth WHERE authToken = ?";
                try (var secondStatement = conn.prepareStatement(sqlDelete)) {
                    secondStatement.setString(1, token);
                    secondStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException("Database error", e);
            }
        }

        public AuthData getAuth(String authToken) throws DataAccessException {
            var sqlGetAuth = "SELECT * FROM auth WHERE authToken = ?";
            try (var conn = DatabaseManager.getConnection();
                 var statement = conn.prepareStatement(sqlGetAuth)) {
                statement.setString(1, authToken);
                var rs = statement.executeQuery();

                if (rs.next()) {
                    String newToken = rs.getString("authToken");
                    String username = rs.getString("username");
                    return new AuthData(newToken, username);
                } else {
                    throw new DataAccessException("authToken does not exist");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Database error", e);
            }
        }
    }
