package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.List;


public class GameService {
        private final SQLUserDAO userDAO;
        private final SQLGameDAO gameDAO;
        private final SQLAuthDAO authDAO;

        public GameService(SQLUserDAO userDAO, SQLGameDAO gameDAO, SQLAuthDAO authDAO) {
            this.userDAO = userDAO;
            this.gameDAO = gameDAO;
            this.authDAO = authDAO;
        }

        public Collection<GameData> returnGames(String authToken) throws DataAccessException {
            authDAO.getAuth(authToken);
            return List.copyOf(gameDAO.listGames());
        }

        public int createGame(String authToken, String gameName) throws DataAccessException {
            AuthData auth = authDAO.getAuth(authToken);
            if (auth == null) {
                throw new DataAccessException("Unauthorized");
            }
            return gameDAO.createGame(gameName);
        }

        public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
            AuthData auth = authDAO.getAuth(authToken);
            if (auth == null) {
                throw new DataAccessException("Unauthorized");
            }
            if (playerColor.equals("")) {
                return;
            }
            String username = auth.username();
            gameDAO.joinGame(gameID, playerColor, username);

        }
    }
