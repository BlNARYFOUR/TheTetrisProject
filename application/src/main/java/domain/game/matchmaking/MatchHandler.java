package domain.game.matchmaking;

import domain.User;
import domain.game.modes.GameMode;
import util.MatchableException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * MatchHandler for random games.
 */
public final class MatchHandler implements Matchmaking {
    private static final int MAX_USERS_PER_MATCH = 5;
    private static MatchHandler instance = new MatchHandler();
    private static Map<GameMode, Set<User>> matchable = new HashMap<>();

    private MatchHandler() {
    }

    public static MatchHandler getInstance() {
        return instance;
    }

    private static void clearMatchable() {
        MatchHandler.matchable = new HashMap<>();
    }

    public Map<GameMode, Set<User>> getMatchable() {
        return matchable;
    }

    @Override
    public void addMatchable(final User user, final GameMode modeSearch) {
        if (matchableContains(user)) {
            throw new MatchableException("User is already searching for a game!");
        }

        if (!matchable.containsKey(modeSearch)) {
            matchable.put(modeSearch, new HashSet<>());
        }

        matchable.get(modeSearch).add(user);
    }

    private boolean matchableContains(final User user) {
        boolean found = false;

        for (Set<User> users : matchable.values()) {
            found = users.contains(user);
        }

        return found;
    }

    @Override
    public void deleteMatchable(final User user) {
        try {
            matchable.forEach((modeSearch, users) -> {
                users.remove(user);
            });
        } catch (Exception e) {
            throw new MatchableException("Unable to remove user from matchable.", e);
        }
    }

    private Set<Match> createNewMatchSet() {
        return new HashSet<>();
    }

    private Set<User> createNewUserSet() {
        return new HashSet<>();
    }

    private void removeUsers(final Set<User> usersToRemove, final GameMode gameMode) {
        usersToRemove.forEach(user -> {
            matchable.get(gameMode).remove(user);
        });
    }

    private void addMatch(final Set<Match> matches, final Match matchTry, final Set<User> usersToRemove) {
        matches.add(matchTry);
        usersToRemove.addAll(matchTry.getUsers());
    }

    @Override
    public Set<Match> matchUsers() {
        // todo: based on game ranking

        final Set<Match> matches = createNewMatchSet();

        for (Map.Entry<GameMode, Set<User>> entry : matchable.entrySet()) {
            final Set<User> usersToRemove = createNewUserSet();

            Match matchTry = createNewMatch(entry.getKey());
            boolean oneLeft = false;
            int usersToAdd = Math.round((MAX_USERS_PER_MATCH + 1) / 2);

            for (User user : matchable.get(entry.getKey())) {
                //System.out.println(matchable.get(gameMode).size() - usersToRemove.size());

                if (matchable.get(entry.getKey()).size() - usersToRemove.size() == MAX_USERS_PER_MATCH + 1) {
                    //System.out.println("one left");
                    oneLeft = true;
                }

                if (!matchTry.addUser(user)) {
                    addMatch(matches, matchTry, usersToRemove);

                    matchTry = createNewMatch(entry.getKey());
                    matchTry.addUser(user);
                }

                if (oneLeft) {
                    usersToAdd--;

                    if (usersToAdd == 0) {
                        addMatch(matches, matchTry, usersToRemove);

                        matchTry = createNewMatch(entry.getKey());
                        usersToAdd = matchable.get(entry.getKey()).size() - usersToRemove.size();
                    }
                }

                //System.out.println("u to add: " + usersToAdd);
            }

            final int two = 2;
            if (two <= matchTry.getUsers().size()) {
                matches.add(matchTry);
                usersToRemove.addAll(matchTry.getUsers());
            }

            removeUsers(usersToRemove, entry.getKey());
        }

        return matches;
    }

    private Match createNewMatch(final GameMode gameMode) {
        return new Match(gameMode, MAX_USERS_PER_MATCH);
    }

    @Override
    public void resetMatchable() {
        clearMatchable();
    }
}
