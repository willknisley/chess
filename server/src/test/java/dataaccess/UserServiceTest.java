package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest
{
    private SQLUserDAO userDAO;
    private SQLGameDAO gameDAO;
    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();

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
