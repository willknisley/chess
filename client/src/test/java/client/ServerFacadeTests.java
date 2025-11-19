package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @Test
    public void loginPositiveTest() throws Exception {
        facade.register("username", "password", "test@email.com");
        AuthData authData = facade.login("username", "password");
        assertEquals("testUser", authData.username());
    }

    @Test
    public void loginNegativeTest() throws Exception {
        facade.register("username", "password", "test@email.com");
        AuthData authData = facade.login("username", "password");
        assertThrows(Exception.class, () -> {
            facade.login("testUser", "wrongPassword");
        });
    }

    @Test
    public void registerPositiveTest() throws Exception {
        AuthData authData =  facade.register("username", "password", "test@email.com");
        assertEquals("username", authData.username());
    }

    @Test
    public void registerNegativeTest() throws Exception {
        AuthData authData =  facade.register("username", "password", "test@email.com");
        assertThrows(Exception.class, () -> {
            facade.register("username", "newPassword", "newtest@email.com");
        });
    }

    @Test
    public void logoutPositiveTest() throws Exception {
        AuthData authData = facade.register("username", "password", "test@email.com");
        String authToken = authData.authToken();
        assertEquals("username", authData.username());
        assertThrows(Exception.class, () -> {
            facade.logout(authToken);
        });
    }

    @Test
    public void logoutNegativeTest() throws Exception {
        String badAuthToken = "badToken";

        assertThrows(Exception.class, () -> {
            facade.logout(badAuthToken);
        });
    }

    @Test
    public void createGamePositiveTest() throws Exception {
        AuthData authData = facade.register("username", "password", "test@email.com");
        String authToken = authData.authToken();
        GameData gameData =  facade.createGame("game", authToken);
        assertTrue(gameData.gameID() > 0);
    }

    @Test
    public void createGameNegativeTest() throws Exception {
        String badToken = "badToken";
        assertThrows(Exception.class, () -> {
            facade.createGame("game", badToken);
        });
    }

    @Test
    public void listGamesPositiveTest() throws Exception {
        AuthData authData = facade.register("username", "password", "test@email.com");
        String authToken = authData.authToken();
        facade.createGame("game", authToken);
        Collection<GameData> games = facade.listGames(authToken);
        assertEquals(1, games.size());
    }

    @Test
    public void listGamesNegativeTest() throws Exception {
        String badToken = "badToken";
        assertThrows(Exception.class, () -> {
            facade.listGames(badToken);
        });
    }

    @Test
    public void joinGamePositiveTest() throws Exception {
        AuthData authData = facade.register("username", "password", "test@email.com");
        String authToken = authData.authToken();
        GameData gameData = facade.createGame("game", authToken);
        int gameID = gameData.gameID();
        assertDoesNotThrow(() -> {
            facade.joinGame(gameID, "WHITE", authToken);
        });
    }

    @Test
    public void joinGameNegativeTest() throws Exception {
        String badToken = "badToken";
        int fakeID = 1;
        assertThrows(Exception.class, () -> {
            facade.joinGame(fakeID, "WHITE", badToken);
        });
    }

    @Test
    public void observeGamePositiveTest() throws Exception {
        AuthData authData = facade.register("username", "password", "test@email.com");
        String authToken = authData.authToken();
        GameData gameData = facade.createGame("game", authToken);
        int gameID = gameData.gameID();
        assertDoesNotThrow(() -> {
            facade.observeGame(gameID, authToken);
        });
    }

    @Test
    public void observeGameNegativeTest() throws Exception {
        String badToken = "badToken";
        int fakeID = 1;
        assertThrows(Exception.class, () -> {
            facade.observeGame(fakeID, badToken);
        });
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

}
