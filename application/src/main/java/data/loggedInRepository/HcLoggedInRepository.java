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
            if (!loggedUsers.containsValue(user)) {
                user.setLoginDate(new Date());
                loggedUsers.put(sessionID, user);
                return true;
            } else {
                return false;
            }
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
        if (loggedUsers.containsValue(user)) {
            long passedTime = 0;
            String keyBuf = "";

            for (String key : loggedUsers.keySet()) {
                final User u = loggedUsers.get(key);

                if (user.equals(u)) {
                    passedTime = Math.round((new Date().getTime() - u.getLoginDate().getTime()) / 1000);
                    keyBuf = key;
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
    public boolean isUserLogged(String sessionID, User user) {
        return user.equals(loggedUsers.getOrDefault(sessionID, null));
    }

    @Override
    public User getLoggedUser(String sessionID) {
        return loggedUsers.getOrDefault(sessionID, null);
    }

    @Override
    public String getSessionID(User user) {
        final String[] sessionID = {null};

        loggedUsers.forEach((k, u) -> setSessionID(k, user, sessionID));

        return sessionID[0];
    }

    private void setSessionID(String key, User user, String[] sessionID) {
        if (loggedUsers.get(key).equals(user)) {
            sessionID[0] = key;
        }
    }

}
