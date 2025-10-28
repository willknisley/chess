package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

        @BeforeEach
        public void setup() {
            userDAO = new UserDAO();
            gameDAO = new GameDAO();
            authDAO = new AuthDAO();
        }

        @Test
        public void createGamePositiveTest() throws DataAccessException {
            UserService userService = new UserService(userDAO, authDAO);
            var register = userService.register("test user", "password", "test@email.com");
            String token = register.authToken();
            GameService gameService = new GameService(userDAO, gameDAO, authDAO);
            gameService.createGame(token, "game name");
            var games = gameDAO.listGames();
            assertEquals(1, games.size());
            var game = games.iterator().next();
            assertEquals("game name", game.gameName());
        }

        @Test
        public void createGameNegativeTest() throws DataAccessException {
            UserService userService = new UserService(userDAO, authDAO);
            var register = userService.register("test user", "password", "test@email.com");
            String token = register.authToken();
            GameService gameService = new GameService(userDAO, gameDAO, authDAO);
            assertThrows(DataAccessException.class, () -> gameService.createGame("bad token","game name"));
            var games = gameDAO.listGames();
            assertEquals(0, games.size());
        }

        @Test
        public void returnGamesPositiveTest() throws DataAccessException {
            UserService userService = new UserService(userDAO, authDAO);
            var register = userService.register("test user", "password", "test@email.com");
            String token = register.authToken();
            GameService gameService = new GameService(userDAO, gameDAO, authDAO);
            gameService.createGame(token, "game name");
            var returns = gameService.returnGames(token);
            assertEquals(1, returns.size());
        }

        @Test
        public void returnGamesNegativeTest() throws DataAccessException {
            UserService userService = new UserService(userDAO, authDAO);
            var register = userService.register("test user", "password", "test@email.com");
            String token = register.authToken();
            GameService gameService = new GameService(userDAO, gameDAO, authDAO);
            gameService.createGame(token, "game name");
            assertThrows(DataAccessException.class, () -> gameService.returnGames("bad token"));
        }

        @Test
        public void joinGamesPositiveTest() throws DataAccessException {
            UserService userService = new UserService(userDAO, authDAO);
            var register = userService.register("test user", "password", "test@email.com");
            String token = register.authToken();
            GameService gameService = new GameService(userDAO, gameDAO, authDAO);
            int ID = gameService.createGame(token, "game name");
            gameService.joinGame(token, ID, "BLACK");
            var game = gameDAO.listGames().iterator().next();
            assertEquals("test user", game.whiteUsername());
        }

        @Test
        public void joinGamesNegativeTest() throws DataAccessException {
            UserService userService = new UserService(userDAO, authDAO);
            var register = userService.register("test user", "password", "test@email.com");
            String token = register.authToken();
            GameService gameService = new GameService(userDAO, gameDAO, authDAO);
            int ID = gameService.createGame("bad token", "game name");
            assertThrows(DataAccessException.class, () -> gameService.joinGame("bad token", ID, "BLACK"));
        }

}
