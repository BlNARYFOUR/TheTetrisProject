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
    private static final int MAX_USERS_PER_MATCH = 5;
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
        // todo: based on game ranking

        Set<Match> matches = new HashSet<>();

        for (ModeSearch modeSearch : matchable.keySet()) {
            Match matchTry = new Match(modeSearch, MAX_USERS_PER_MATCH);
            boolean oneLeft = false;
            int usersToAdd = Math.round((MAX_USERS_PER_MATCH + 1) / 2);
            for (User user : matchable.get(modeSearch)) {
                if(matchable.get(modeSearch).size() == MAX_USERS_PER_MATCH + 1) {
                    oneLeft = true;
                }

                if(!matchTry.addUser(user)) {
                    matches.add(matchTry);

                    matchTry.getUsers().forEach(u -> {
                        matchable.remove(modeSearch, u);
                    });

                    matchTry = new Match(modeSearch, MAX_USERS_PER_MATCH);
                    matchTry.addUser(user);
                }

                if(oneLeft) {
                    usersToAdd--;

                    if(usersToAdd == 0) {
                        matches.add(matchTry);

                        matchTry.getUsers().forEach(u -> {
                            matchable.remove(modeSearch, u);
                        });

                        matchTry = new Match(modeSearch, MAX_USERS_PER_MATCH);
                        usersToAdd = matchable.get(modeSearch).size();
                    }
                }
            }

            if(2 <= matchTry.getUsers().size()) {
                matches.add(matchTry);
            }
        }

        return matches;
    }

    @Override
    public void resetMatchable() {
        matchable = new HashMap<>();
    }
}
