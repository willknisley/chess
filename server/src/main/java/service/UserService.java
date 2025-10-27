package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
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
        if (!Objects.equals(password, user.password())) {
            throw new DataAccessException("Wrong password");
        }
        String authToken = java.util.UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);
        return auth;
    }
}
