package domain.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.pmw.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private static final int BEGIN_MOVEMENT_TIME = 750;
    private static final int FULL_LINE_POINTS = 10;

    private ObjectMapper objectMapper = new ObjectMapper();

    private int playerID;
    private User user;
    private String address;
    private String session;
    private String gameAddress;
    private boolean ready;
    private boolean isDead;
    private Integer[][] playingField;

    private FallingBlock holdFallingBlock;
    private FallingBlock nextFallingBlock;
    private FallingBlock fallingBlock;

    private int score;
    private int amountOfScoredLines;
    private int level;

    private double normalMovementTime;
    private long periodic;

    public Player(int playerID, User user, String sessionID, String gameAddress) {
        setUser(user);
        setAddress(sessionID);

        createPlayingFields();
        getNextFallingBlock();
        setFallingBlock();
        score = 0;
        amountOfScoredLines = 0;
        level = 0;
        normalMovementTime = BEGIN_MOVEMENT_TIME;
        ready = false;
        isDead = false;
        this.session = sessionID;
        this.gameAddress = gameAddress;
        this.playerID = playerID;
    }

    void startPlaying() {
        Context context = Vertx.currentContext();
        periodic = context.owner().setPeriodic(Math.round(normalMovementTime), this::updateCycle);
        //updateCycle(0);
        setupListener();
    }

    private void setupListener() {
        Context context = Vertx.currentContext();
        EventBus eb = context.owner().eventBus();
        Logger.warn(address);
        eb.consumer(address, this::gameHandler);
    }

    private void gameHandler(Message message) {
        Logger.info(this + " got a message!");
        message.reply("GOT IT");
    }

    private void updateCycle(long l) {
        nextBlockFall();
        sendUpdate();
    }

    private void getNextFallingBlock() {
        nextFallingBlock = new FallingBlock();
    }

    private void setFallingBlock() {
        fallingBlock = nextFallingBlock;
        getNextFallingBlock();
    }

    public boolean nextBlockFall() {
        boolean isNew = false;

        if(checkCollision(fallingBlock, fallingBlock.getX(), fallingBlock.getY() + 1)) {
            isNew = true;
            placeBlock(fallingBlock, fallingBlock.getX(), fallingBlock.getY(), fallingBlock.getID());

            if(fallingBlock.getY() <= 0) {
                die();
            } else {
                fallingBlock = nextFallingBlock;
                getNextFallingBlock();
                boolean hasScored = checkAndScoreFullRows();

                if(10*level <= amountOfScoredLines) {
                    level++;
                    normalMovementTime /= 1.5;
                    updateSpeed();
                }
            }

        } else {
            fallingBlock.fall();
        }

        return isNew;
    }

    private Integer[][] getPlayingFieldWithFallingBlock() {
        Integer[][] pwfb = new Integer[playingField.length][];
        boolean collided = checkCollision(fallingBlock, fallingBlock.getX(), fallingBlock.getY());

        final int MAX_HEIGHT = fallingBlock.getPattern().length;
        final int MAX_WIDTH = fallingBlock.getPattern()[0].length;

        for (int i = 0; i < playingField.length; i++) {
            pwfb[i] = new Integer[playingField[i].length];

            for (int j = 0; j < playingField[i].length; j++) {
                int yDiff = i - fallingBlock.getY();
                int xDiff = j - fallingBlock.getX();

                //System.out.println(yDiff + " " + xDiff);

                if((0 <= yDiff && 0 <= xDiff) && (yDiff < MAX_HEIGHT && xDiff < MAX_WIDTH)) {
                    //System.out.println("Gets here");
                    if (fallingBlock.getPattern()[yDiff][xDiff] && !collided) {
                        pwfb[i][j] = fallingBlock.getID();
                    } else {
                        pwfb[i][j] = playingField[i][j];
                    }
                } else {
                    pwfb[i][j] = playingField[i][j];
                }
            }
        }

        return pwfb;
    }

    public void sendUpdate() {
        Context context = Vertx.currentContext();
        EventBus eb = context.owner().eventBus();

        Map<String, Object> data = new HashMap<>();
        data.put("playingField", getPlayingFieldWithFallingBlock());
        data.put("fallingBlock", fallingBlock);
        data.put("playerId", playerID);

        String json = "<ERROR>";

        try {
            json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Logger.info("Update to client! <3 : tetris-16.socket.client.game." + gameAddress);

        eb.publish("tetris-16.socket.client.game." + gameAddress, json);
    }

    private void updateSpeed() {
        Logger.warn("updateSpeed NYI");
    }

    private void die() {
        Logger.warn("Die NYI");
        Context context = Vertx.currentContext();
        Logger.warn("Periodic: " + periodic);
        context.owner().cancelTimer(periodic);
        isDead = true;
    }

    private boolean checkAndScoreFullRows() {
        boolean isFullLine;
        boolean hasScored = false;
        int totalLineScore = 0;

        for(int i=0; i<playingField.length; i++) {
            totalLineScore = 0;
            isFullLine = true;
            for(int j=0; j<playingField[i].length; j++) {
                if(0 < playingField[i][j]) {
                    totalLineScore += playingField[i][j];
                } else {
                    isFullLine = false;
                }
            }

            if(isFullLine) {
                dropTopLayers(i);
                hasScored = true;
                score += totalLineScore * FULL_LINE_POINTS;
                amountOfScoredLines++;
            }
        }

        return hasScored;
    }

    private void dropTopLayers(int lineHeight) {
        System.arraycopy(playingField, 0, playingField, 1, lineHeight);
        emptyLine(0);
    }

    private void emptyLine(int lineHeight) {
        playingField[lineHeight] = new Integer[Game.PLAYING_FIELD_WIDTH];

        for(int j=0; j<Game.PLAYING_FIELD_WIDTH; j++) {
            playingField[lineHeight][j] = 0;
        }
    }

    private boolean checkCollision (Block block, int x, int y) {
        boolean collided = false;

        Boolean[][] blockPattern = fallingBlock.getPattern();

        final int MAX_HEIGHT = blockPattern.length;
        final int MAX_WIDTH = blockPattern[0].length;

        if(x+MAX_WIDTH <= Game.PLAYING_FIELD_WIDTH && y+MAX_HEIGHT <= Game.PLAYING_FIELD_HEIGHT && x >= 0) {
            for(int i=0; i<MAX_HEIGHT; i++) {
                for (int j = 0; j < MAX_WIDTH; j++) {
                    if(blockPattern[i][j] && 0 < playingField[y+i][x+j]) {
                        collided = true;
                    }
                }
            }
        } else {
            collided = true;
        }

        return collided;
    }

    private void placeBlock(Block block, int x, int y, int colorIndex) {
        final int MAX_HEIGHT = block.getPattern().length;
        final int MAX_WIDTH = block.getPattern()[0].length;

        if(!checkCollision(block, x, y)) {
            //System.out.println("Placed!");
            for (int i = 0; i < MAX_HEIGHT; i++) {
                for (int j = 0; j < MAX_WIDTH; j++) {
                    if (block.getPattern()[i][j]) {
                        //System.out.println("Gets here: " + (y + i) + " " + (x + j) + " ID: " + colorIndex);
                        playingField[y + i][x + j] = colorIndex;
                    }
                }
            }
        }
    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setAddress(String sessionID) {
        this.address = "tetris-16.socket.server.game." + sessionID;
    }

    public FallingBlock getFallingBlock() {
        return fallingBlock;
    }

    private void createPlayingFields() {
        playingField = new Integer[Game.PLAYING_FIELD_HEIGHT][];

        for (int y = 0; y < Game.PLAYING_FIELD_HEIGHT; y++) {
            playingField[y] = new Integer[Game.PLAYING_FIELD_WIDTH];
        }

        System.out.println("PLayingfield length: " + playingField.length);

        for (int y = 0; y < playingField.length; y++) {
            for (int x = 0; x < playingField[0].length; x++) {
                playingField[y][x] = 0;
            }
        }
    }

    public void setReady() {
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public String getSession() {
        return session;
    }

    public int getPlayerID() {
        return playerID;
    }

    @Override
    public String toString() {
        return "Player{" +
                "user=" + user +
                ", address='" + address + '\'' +
                '}';
    }

    public String playingFieldWithFallingBlock() {
        StringBuilder sb = new StringBuilder();

        final int MAX_HEIGHT = fallingBlock.getPattern().length;
        final int MAX_WIDTH = fallingBlock.getPattern()[0].length;

        for (int i = 0; i < playingField.length; i++) {
            for (int j = 0; j < playingField[i].length; j++) {
                int yDiff = i - fallingBlock.getY();
                int xDiff = j - fallingBlock.getX();

                //System.out.println(yDiff + " " + xDiff);

                if((0 <= yDiff && 0 <= xDiff) && (yDiff < MAX_HEIGHT && xDiff < MAX_WIDTH)) {
                    //System.out.println("Gets here");
                    if (fallingBlock.getPattern()[yDiff][xDiff]) {
                        sb.append('F');
                    } else {
                        sb.append(playingField[i][j]);
                    }
                } else {
                    sb.append(playingField[i][j]);
                }

                sb.append(' ');
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
