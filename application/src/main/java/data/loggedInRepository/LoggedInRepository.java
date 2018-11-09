package data.loggedInRepository;

import domain.User;
import io.vertx.ext.web.Session;

public interface LoggedInRepository {
    boolean addLoggedUser(String sessionID, User user);
    boolean deleteLoggedUser(String sessionID);
    boolean isUserLogged(User user);
    User getLoggedUser(String sessionID);
}
