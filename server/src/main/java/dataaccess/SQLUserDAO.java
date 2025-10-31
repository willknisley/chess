package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserDAO {

public void clear() throws DataAccessException {
    var sqlClear = "DELETE FROM users";
    try (var conn = DatabaseManager.getConnection();
         var statement = conn.prepareStatement(sqlClear)) {
        statement.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Error clearing users", e);
    }
}

    public void createUser(UserData user) throws DataAccessException {
        var sqlCreateUser = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlCreateUser)) {
            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            statement.setString(1, user.username());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
    var sqlGetUser = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sqlGetUser)) {
            statement.setString(1, username);
            var rs = statement.executeQuery();

            if (rs.next()) {
                String newUsername = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                return new UserData(newUsername, password, email);
            } else {
                throw new DataAccessException("Error: (username does not exist)");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users", e);
        }
    }
}

