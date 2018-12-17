package data.loggedInRepository;

import domain.User;

/**
 * Contract for LoggedInRepository.
 */
public interface LoggedInRepository {
    // 1 year (60s*60m*24h*356d) in seconds
    int EXPIRATION_TIME = 31536000;
    boolean addLoggedUser(String sessionID, User user);
    boolean deleteLoggedUser(String sessionID);
    boolean isUserLogged(User user);
    boolean isUserLogged(String sessionID, User user);
    User getLoggedUser(String sessionID);
    String getSessionID(User user);
}
