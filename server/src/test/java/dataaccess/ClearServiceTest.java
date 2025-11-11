package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private ClearService clearService;
    private SQLUserDAOTest userDAO;
    private SQLGameDAOTest gameDAO;
    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAOTest();
        gameDAO = new SQLGameDAOTest();
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
