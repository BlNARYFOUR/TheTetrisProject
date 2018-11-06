package domain.game.matchmaking;

import domain.User;
import domain.game.modes.ModeSearch;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.MatchableException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MatchHandler implements Matchmaking {
    private static MatchHandler instance = new MatchHandler();
    private static Map<ModeSearch, Set<User>> matchable = new HashMap<>();

    public static MatchHandler getInstance(){
        return instance;
    }

    private MatchHandler() {
    }

    public Map<ModeSearch, Set<User>> getMatchable() {
        return matchable;
    }

    @Override
    public void addMatchable(User user, ModeSearch modeSearch) {
        if(matchableContains(user)) {
            throw new MatchableException("User is already searching for a game!");
        }

        if(!matchable.containsKey(modeSearch)) {
            matchable.put(modeSearch, new HashSet<>());
        }
        
        matchable.get(modeSearch).add(user);
    }

    private boolean matchableContains(User user) {
        boolean found = false;

        for (Set<User> users : matchable.values()) {
            found = users.contains(user);
        }

        return found;
    }

    @Override
    public void deleteMatchable(User user) {
        try {
            matchable.forEach((modeSearch, users) -> {
                users.remove(user);
            });
        } catch (Exception e) {
            throw new MatchableException("Unable to remove user from matchable.");
        }
    }

    @Override
    public Set<Match> matchUsers() {
        throw new NotImplementedException();
    }
}
