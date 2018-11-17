package data.loggedInRepository;

import domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HcLoggedInRepository implements LoggedInRepository {
    private static Map<String, User> loggedUsers = new HashMap<>();

    @Override
    public boolean addLoggedUser(String sessionID, User user) {
        try {
            user.setLoginDate(new Date());
            loggedUsers.put(sessionID, user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteLoggedUser(String sessionID) {
        loggedUsers.remove(sessionID);

        return false;
    }

    @Override
    public boolean isUserLogged(User user) {
        return loggedUsers.containsValue(user);
    }

    @Override
    public boolean isUserLogged(String sessionID, User user) {
        return user.equals(loggedUsers.getOrDefault(sessionID, null));
    }

    @Override
    public User getLoggedUser(String sessionID) {
        return loggedUsers.getOrDefault(sessionID, null);
    }
}
