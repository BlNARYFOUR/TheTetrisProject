package domain.game.matchmaking;

import domain.User;
import domain.game.modes.GameMode;
import util.MatchableException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MatchHandler implements Matchmaking {
    private static final int MAX_USERS_PER_MATCH = 5;
    private static MatchHandler instance = new MatchHandler();
    private static Map<GameMode, Set<User>> matchable = new HashMap<>();

    public static MatchHandler getInstance(){
        return instance;
    }

    private MatchHandler() {
    }

    public Map<GameMode, Set<User>> getMatchable() {
        return matchable;
    }

    @Override
    public void addMatchable(User user, GameMode modeSearch) {
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

        for (GameMode gameMode : matchable.keySet()) {
            Set<User> usersToRemove = new HashSet<>();

            Match matchTry = new Match(gameMode, MAX_USERS_PER_MATCH);
            boolean oneLeft = false;
            int usersToAdd = Math.round((MAX_USERS_PER_MATCH + 1) / 2);
            for (User user : matchable.get(gameMode)) {
                System.out.println(matchable.get(gameMode).size() - usersToRemove.size());

                if(matchable.get(gameMode).size() - usersToRemove.size() == MAX_USERS_PER_MATCH + 1) {
                    System.out.println("one left");
                    oneLeft = true;
                }

                if(!matchTry.addUser(user)) {
                    matches.add(matchTry);

                    usersToRemove.addAll(matchTry.getUsers());

                    matchTry = new Match(gameMode, MAX_USERS_PER_MATCH);
                    matchTry.addUser(user);
                }

                if(oneLeft) {
                    usersToAdd--;

                    if(usersToAdd == 0) {
                        matches.add(matchTry);

                        usersToRemove.addAll(matchTry.getUsers());

                        matchTry = new Match(gameMode, MAX_USERS_PER_MATCH);
                        usersToAdd = matchable.get(gameMode).size() - usersToRemove.size();
                    }
                }

                System.out.println("u to add: " + usersToAdd);
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
