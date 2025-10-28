package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServicetest
{
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() {
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();

        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    public void registerPositiveTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        var auth = userService.register("name", "password", "test@email.com");
        assertNotNull(auth);
    }

    @Test
    public void registerNegativeTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);userService.register("bad name", "password", "test@email.com");
        assertThrows(DataAccessException.class, () ->
                userService.register("bad name", "password", "test@email.com")
        );
    }

    @Test
    public void loginPositiveTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        var auth = userService.register("name", "password", "test@email.com");
        var user = userService.login("name", "password");
        assertNotNull(user);
    }

    @Test
    public void loginNegativeTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        assertThrows(DataAccessException.class, () ->
                userService.login("bad name", "password")
        );
    }

    @Test
    public void logoutPositiveTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        var auth = userService.register("name", "password", "test@email.com");
        String token = auth.authToken();
        userService.logout(token);
        assertThrows(DataAccessException.class, () -> authDAO.getAuth(token));
    }

    @Test
    public void logoutNegativeTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        assertThrows(DataAccessException.class, () ->
                userService.logout("bad token")
        );
    }

}
