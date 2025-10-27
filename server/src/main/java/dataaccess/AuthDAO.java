package dataaccess;

import model.AuthData;
import model.UserData;
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
}
