package client;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static server.ServerFacade.HttpMethod.POST;



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



    //createGame
    //listgame
    //joingame
    //observegame

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

    @AfterAll
    static void stopServer() {
        server.stop();
    }

}
