package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
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

        public Collection<GameData> return_games(String authToken) throws DataAccessException {
            authDAO.getAuth(authToken);
            return gameDAO.listGames();
        }

    }
