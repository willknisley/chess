package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import java.util.HashMap;

public class UserDAO {
    HashMap<String, UserData> users = new HashMap<>();
    public void clear() {
        users.clear();
    }
    public void createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.username())) {
            throw new DataAccessException("Error: (username already exists)");
        } else {
            users.put(user.username(), user);
        }

    }
    //public UserData getUser(String username) throws DataAccessException {
    //}
    //public GameData createGame(String gameName, ChessGame game) throws DataAccessException {
    //}
}
