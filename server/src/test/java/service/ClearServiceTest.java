package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private ClearService clearService;
    private SQLUserDAO userDAO;
    private GameDAO gameDAO;
    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        gameDAO = new GameDAO();
        authDAO = new SQLAuthDAO();
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
        assertThrows(DataAccessException.class, () -> userDAO.getUser("test user"));
    }
    @Test
    public void clearNegativeTest() throws DataAccessException {
        clearService.clearAll();
        assertDoesNotThrow(() -> clearService.clearAll());
        assertEquals(0, gameDAO.listGames().size());
        assertThrows(DataAccessException.class, () -> userDAO.getUser("doesn't exist"));
    }
}
