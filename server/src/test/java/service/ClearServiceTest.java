package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    public void clearPositiveTest() throws DataAccessException {
        UserService userService = new UserService(userDAO, authDAO);
        userService.register("test user", "password", "test@email.com");
        clearService.clearAll();
        var user = userDAO.getUser("test user");
        assertNull(user);
    }
    @Test
    public void clearNegativeTest() throws DataAccessException {
        clearService.clearAll();
        assertDoesNotThrow(() -> clearService.clearAll());
        assertEquals(0, gameDAO.listGames().size());
        assertNull(userDAO.getUser("doesn't exist"));
    }
}
