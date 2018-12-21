package data.loggedinrepository;

import domain.User;
import org.pmw.tinylog.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of LoggedInRepository.
 */
public class HcLoggedInRepository implements LoggedInRepository {
    private static Map<String, User> loggedUsers = new HashMap<>();

    @Override
    public boolean addLoggedUser(final String sessionID, final User user) {
        try {
            if (!loggedUsers.containsValue(user)) {
                user.setLoginDate(new Date());
                loggedUsers.put(sessionID, user);
                return true;
            }
        } catch (Exception ex) {
            Logger.warn(ex.getMessage());
        }

        return false;
    }

    @Override
    public boolean deleteLoggedUser(final String sessionID) {
        loggedUsers.remove(sessionID);

        return false;
    }

    private long getCurrentTime() {
        return new Date().getTime();
    }

    @Override
    public boolean isUserLogged(final User user) {
        if (loggedUsers.containsValue(user)) {
            long passedTime = 0;
            String keyBuf = "";

            for (Map.Entry<String, User> entry : loggedUsers.entrySet()) {
                final User u = loggedUsers.get(entry.getKey());

                if (user.equals(u)) {
                    passedTime = Math.round((getCurrentTime() - u.getLoginDate().getTime()) / 1000.0);
                    keyBuf = entry.getKey();
                    break;
                }
            }

            //System.out.println("Logged for "  + Long.toString(passedTime));
            if (LoggedInRepository.EXPIRATION_TIME < passedTime) {
                loggedUsers.remove(keyBuf);
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isUserLogged(final String sessionID, final User user) {
        return user.equals(loggedUsers.getOrDefault(sessionID, null));
    }

    @Override
    public User getLoggedUser(final String sessionID) {
        return loggedUsers.getOrDefault(sessionID, null);
    }

    @Override
    public String getSessionID(final User user) {
        final String[] sessionID = {null};

        loggedUsers.forEach((k, u) -> setSessionID(k, user, sessionID));

        return sessionID[0];
    }

    private void setSessionID(final String key, final User user, String... sessionID) {
        if (loggedUsers.get(key).equals(user)) {
            sessionID[0] = key;
        }
    }

}
