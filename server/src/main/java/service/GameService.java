package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import jakarta.servlet.UnavailableException;
import model.AuthData;
import model.GameData;

import java.util.Collection;


public class GameService {
        private final UserDAO userDAO;
        private final GameDAO gameDAO;
        private final AuthDAO authDAO;

        public GameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
            this.userDAO = userDAO;
            this.gameDAO = gameDAO;
            this.authDAO = authDAO;
        }

        public Collection<GameData> returnGames(String authToken) throws DataAccessException {
            authDAO.getAuth(authToken);
            return gameDAO.listGames();
        }

        public int createGame(String authToken, String gameName) throws DataAccessException {
            AuthData auth = authDAO.getAuth(authToken);
            if (auth == null) {
                throw new DataAccessException("Unauthorized");
            }
            return gameDAO.createGame(gameName);
        }

    }
