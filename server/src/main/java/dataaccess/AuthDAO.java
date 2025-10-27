package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDAO {
    HashMap<String, AuthData> auths = new HashMap<>();
    public void clear() {
        auths.clear();
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        if (auths.containsKey(auth.authToken())) {
            throw new DataAccessException("authToken already exists");
        } else {
            auths.put(auth.authToken(), auth);
        }

    }

    public void deleteAuth(String token) throws DataAccessException {
        if (!auths.containsKey(token)) {
            throw new DataAccessException("authToken does not exist");
        } else {
            auths.remove(token);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        if (!auths.containsKey(authToken)) {
            throw new DataAccessException("authToken does not exist");
        } else {
            return auths.get(authToken);
        }
    }
}
