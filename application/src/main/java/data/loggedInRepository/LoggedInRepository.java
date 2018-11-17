package data.loggedInRepository;

import domain.User;
import io.vertx.ext.web.Session;

public interface LoggedInRepository {
    boolean addLoggedUser(String sessionID, User user);
    boolean deleteLoggedUser(String sessionID);
    boolean isUserLogged(User user);
    boolean isUserLogged(String sessionID, User user);
    User getLoggedUser(String sessionID);

    int EXPIRATION_TIME = 31536000;        // 1 year (60s*60m*24h*356d)
}
