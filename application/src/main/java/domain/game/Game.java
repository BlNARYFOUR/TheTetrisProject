package domain.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Repositories;
import data.loggedInRepository.LoggedInRepository;
import domain.User;
import domain.game.matchmaking.Match;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.pmw.tinylog.Logger;
import util.Hash;
import util.MatchableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
    static final int PLAYING_FIELD_WIDTH = 10;
    static final int PLAYING_FIELD_HEIGHT = 18;

    private static LoggedInRepository repo = Repositories.getInstance().getLoggedInRepository();

    private static final String SALT = "A1fj65mg<2eigo";
    private static int nextGameID = 0;

    private int gameID;
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Player> players;

    public Game(Match match) {
        Set<User> users = match.getUsers();
        players = new ArrayList<>();
        gameID = nextGameID;
        nextGameID++;

        users.forEach(user -> {
            Player player = new Player(user, repo.getSessionID(user));
            players.add(player);
        });
    }

    public void setupListener() {
        Context context = Vertx.currentContext();
        EventBus eb = context.owner().eventBus();
        eb.consumer("tetris-16.socket.server.ready", this::readyHandler);
    }

    private void readyHandler(Message message) {
        Map<String, Object> jsonMap = null;

        try {
            jsonMap = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            throw new MatchableException("json in readyHandler not valid!");
        }

        Logger.warn("Match request received: " + jsonMap);

        Logger.info("Game " + gameID + " got a ready-state for " + );
        message.reply("GOT IT");
    }

    public void startGame() {
        players.forEach(Player::startPlaying);
    }

    public String getGameAddress() {
        return Hash.md5(SALT + gameID);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
