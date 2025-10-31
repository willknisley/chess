package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class UserService {
    private final SQLUserDAO userDAO;
    private final SQLAuthDAO authDAO;

    public UserService(SQLUserDAO userDAO, SQLAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(String username, String password, String email) throws DataAccessException {
        String authToken = java.util.UUID.randomUUID().toString();
        UserData user = new UserData(username, password, email);
        AuthData auth = new AuthData(authToken, username);
        userDAO.createUser(user);
        authDAO.createAuth(auth);
        return auth;
    }

    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = userDAO.getUser(username);
        if (!BCrypt.checkpw(password, user.password())) {
            throw new DataAccessException("Wrong password");
        }
        String authToken = java.util.UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);
        return auth;
    }

    public void logout(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }
}
