package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearServiceTest {
    private ClearService clearService;
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() {
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    public void clearPositiveTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        userService.register("test user", "password", "test@email.com");
        clearService.clear_all();
        var user = userDAO.getUser("test user");
        assertNull(user);
    }
    @Test
    public void clearNegativeTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        userService.register("test user", "password", "test@email.com");
        clearService.clear_all();
        clearService.clear_all();
        assertEquals(0, gameDAO.listGames().size());
    }
}
