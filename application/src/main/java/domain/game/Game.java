package domain.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Repositories;
import data.loggedinrepository.LoggedInRepository;
import domain.User;
import domain.game.events.EventHandler;
import domain.game.matchmaking.Match;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import org.pmw.tinylog.Logger;
import util.Hash;
import util.MatchableException;

import java.io.IOException;
import java.util.*;

/**
 * Game class.
 */
public class Game {
    protected static final int PLAYING_FIELD_WIDTH = 10;
    protected static final int PLAYING_FIELD_HEIGHT = 18;
    private static final String ERROR = "<ERROR>";

    private static LoggedInRepository repo = Repositories.getInstance().getLoggedInRepository();

    private static final String SALT = "A1fj65mg<2eigo";
    private static int nextGameID;

    private final int gameID;
    private int nextPlayerID;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Player> players;
    private MessageConsumer<Object> consumer;

    private final EventHandler eventHandler;

    public Game(final Match match) {
        players = new ArrayList<>();
        gameID = nextGameID;
        increaseNextGameID();
        nextPlayerID = 0;
        final Set<User> users = match.getUsers();

        setupListener();

        this.eventHandler = new EventHandler(players);

        users.forEach(user -> {
            final Player player = new Player(nextPlayerID, user, repo.getSessionID(user),
                    genGameAddress(), eventHandler);
            players.add(player);
            nextPlayerID++;
        });

    }

    private static void increaseNextGameID() {
        nextGameID++;
    }

    private void setupListener() {
        final Context context = Vertx.currentContext();
        final EventBus eb = context.owner().eventBus();
        Logger.warn("SETUP: tetris-16.socket.server.ready." + Hash.md5(SALT + gameID));
        consumer = eb.consumer("tetris-16.socket.server.ready." + Hash.md5(SALT + gameID), this::readyHandler);
    }

    private int getPlayerIdBySession(final String session) {
        int playerId = -1;

        for (Player player : players) {
            if (player.getSession().equals(session)) {
                playerId = player.getPlayerID();
            }
        }

        return playerId;
    }

    private void readyHandler(final Message message) {
        Map<String, Object> data = null;

        try {
            data = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>() { });
        } catch (IOException e) {
            throw new MatchableException("json in readyHandler not valid!", e);
        }

        final String sessionID = String.valueOf(data.getOrDefault("session", ERROR));

        Logger.info("Game " + gameID + " got a ready-state for " + sessionID);

        players.get(getPlayerIdBySession(sessionID)).setReady();

        data = new HashMap<>();
        data.put("status", "oke");
        data.put("playerId", getPlayerIdBySession(sessionID));
        //data.put("amountOfPlayers", players.size());

        String json = ERROR;

        try {
            json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            Logger.warn(e.getMessage());
        }

        message.reply(json);

        boolean allReady = true;

        for (Player player : players) {
            if (!player.isReady()) {
                allReady = false;
                break;
            }
        }

        if (allReady) {
            Logger.info("All ready!!");
            startGame();
            disableReadyHandler();
        }
    }

    public void startGame() {
        players.forEach(Player::startPlaying);
    }

    public void disableReadyHandler() {
        consumer.unregister();
    }

    public String genGameAddress() {
        return Hash.md5(SALT + gameID);
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Game game = (Game) o;
        return gameID == game.gameID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(gameID);
    }
}
