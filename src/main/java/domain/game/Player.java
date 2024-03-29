package domain.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import domain.game.events.EventHandler;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import org.pmw.tinylog.Logger;
import util.InputException;
import util.MatchableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Player class.
 * Every player runs there own game loop.
 */
@SuppressWarnings("PMD")
public class Player {
    private static final int BEGIN_MOVEMENT_TIME = 750;
    private static final int FULL_LINE_POINTS = 10;
    private static final int SUPER_SONIC_POINTS = 5;

    private static final long FAST_MOVEMENT_TIME = 25;

    private static final String ARROW_DOWN = "ArrowDown";
    private static final String KEY_S = "KeyS";
    private static final String ARROW_UP = "ArrowUp";
    private static final String KEY_W = "KeyW";
    private static final String KEY_D = "KeyD";
    private static final String ARROW_RIGHT = "ArrowRight";
    private static final String KEY_A = "KeyA";
    private static final String ARROW_LEFT = "ArrowLeft";

    private static final String DONKEY_KONG = "donkeykong";
    private static final String ACTIVATED = "ACTIVATED";
    private static final String PIKACHU = "pikachu";

    private ObjectMapper objectMapper = new ObjectMapper();

    private int playerID;
    private User user;
    private String address;
    private String session;
    private String gameAddress;
    private boolean ready;
    private boolean isDead;
    private Integer[][] playingField;

    //private FallingBlock holdFallingBlock;
    private FallingBlock nextFallingBlock;
    private FallingBlock fallingBlock;

    private int score;
    private int amountOfScoredLines;
    private int level;
    private int maxScore = 500;

    private double normalMovementTime;
    private long periodicID;

    private boolean isKeyDown;
    private MessageConsumer<Object> consumer;

    private EventHandler eventHandler;
    private int arrowUps;

    private String hero;
    private HeroAbility heroAbility = new HeroAbility();
    private int timerAbility = heroAbility.getTimerAbility();
    private int countAmountBlocksDonkeyKong;


    public Player(int playerID, User user, String sessionID, String gameAddress, EventHandler eventHandler) {
        setUser(user);
        setAddress(sessionID);

        createPlayingFields();
        getNextFallingBlock();
        setFallingBlock();
        this.score = 0;
        this.amountOfScoredLines = 0;
        this.level = 0;
        this.normalMovementTime = BEGIN_MOVEMENT_TIME;
        this.ready = false;
        this.isDead = false;
        this.session = sessionID;
        this.gameAddress = gameAddress;
        this.playerID = playerID;
        this.isKeyDown = false;

        this.eventHandler = eventHandler;
    }

    void startPlaying() {
        final Context context = Vertx.currentContext();
        periodicID = context.owner().setPeriodic(Math.round(normalMovementTime), this::updateCycle);
        //updateCycle(0);
        setupListener();
    }

    private void setupListener() {
        final Context context = Vertx.currentContext();
        final EventBus eb = context.owner().eventBus();
        Logger.warn(address);
        consumer = eb.consumer(address, this::gameHandler);
    }

    private void onArrowUp() {
        rotate();
        onArrowUpEvent();
    }

    private void switchOnKeyDown(String key, boolean heroActivated) {
        switch (key) {
            case ARROW_LEFT:
            case KEY_A:
                goLeft();
                break;
            case ARROW_RIGHT:
            case KEY_D:
                goRight();
                break;
            case KEY_W:
            case ARROW_UP:
                onArrowUp();
                break;
            case KEY_S:
            case ARROW_DOWN:
                goSonic();
                break;
            case "Space":
                goSuperSonic();
                break;
            default:
                break;
        }

        switchKeyR(key, heroActivated);
    }

    private void switchKeyR(String key, boolean heroActivated) {
        switch (key) {
            case "KeyR":
                controleIfHeroCanBeActivated(heroActivated);
                break;
            default:
                break;
        }
    }

    private void onArrowUpEvent() {
        arrowUps++;
        if (arrowUps == 4) {
            dropFaster();
            arrowUps = 0;
        }
    }

    private void dropFaster() {
        System.out.println("You did a 360°!!");
    }

    private void switchOnKeyUp(String key) {
        switch (key) {
            case KEY_S:
            case ARROW_DOWN:
                stopSonic();
                break;
            default:
                break;
        }
    }

    private void testHeroAbility(String key) {
        if (heroAbility.isHeroAbilityIsActivated()) {
            switch (heroAbility.getHeroAttack()) {
                case PIKACHU:
                    //pikachuAbility();
                    if (heroAbility.activatedID() == this.user.getId()) {
                        System.out.println(ACTIVATED);
                        if (!this.isKeyDown) {
                            switchOnKeyDown(key, true);
                        } else {
                            switchOnKeyUp(key);
                        }
                    } else {
                        System.out.println("Mixed");
                        mixedControls(key);

                    }
                    break;
                case DONKEY_KONG:
                    //donkeyKongAbility();
                    if (heroAbility.activatedID() == this.user.getId()) {
                        System.out.println(ACTIVATED);
                        if (!this.isKeyDown) {
                            switchOnKeyDown(key, true);
                        } else {
                            switchOnKeyUp(key);
                        }
                    } else {
                        System.out.println("Donkey Kill");
                        donkeyKongControls(key);
                    }
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("NOT ACTIVATED");
            if (!this.isKeyDown) {
                switchOnKeyDown(key, false);
            } else {
                switchOnKeyUp(key);
            }
        }
    }

    private void gameHandler(Message message) {
        //Logger.info(this.session + " got a message!");

        //Logger.warn(message.body());

        Map<String, Object> data = null;

        try {
            data = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new MatchableException("json in readyHandler not valid!");
        }

        try {
            final String key = (String) data.get("key");
            final Boolean isKeyDown = (Boolean) data.get("state");

            hero = user.getHeroName();

            System.out.println(score);

            testHeroAbility(key);

            this.isKeyDown = isKeyDown;
        } catch (Exception e) {
            throw new InputException("Key or state not of valid type!");
        }

        message.reply("GOT IT");
        sendUpdate();
    }

    private void donkeyKongControls(String key) {
        goSonic();

        if (!this.isKeyDown) {
            switch (key) {
                case ARROW_LEFT:
                case KEY_A:
                    goRight();
                    break;
                case ARROW_RIGHT:
                case KEY_D:
                    goLeft();
                    break;
                default:
                    break;
            }
        }
    }

    private void mixedControls(String key) {
        if (!this.isKeyDown) {
            switch (key) {
                case ARROW_LEFT:
                case KEY_A:
                    goRight();
                    break;
                case ARROW_RIGHT:
                case KEY_D:
                    goLeft();
                    break;
                case KEY_W:
                case ARROW_UP:
                    goSuperSonic();
                    break;
                case KEY_S:
                case ARROW_DOWN:
                    rotate();
                    break;
                default:
                    break;
            }
        }
    }

    private void controleIfHeroCanBeActivated(boolean heroActivated) {
        //System.out.println("pressed on R");
        //System.out.println(score);

        if (!heroActivated) {
            if (score < maxScore) {
                System.out.println("Not enough points to use hero ability");
            } else {
                System.out.println("Hero ability is activated");
                score = score - maxScore;

                heroAbility.setActivatedHero(this.user.getId());
                hero = user.getHeroName();

                switch (hero) {
                    case PIKACHU:
                        System.out.println("Pika Pika bitch");
                        heroAbility.setHeroAbilityIsActivated(true);
                        heroAbility.setHeroAttack(hero);
                        //HeroAbility.setSwitchingControls(true);
                        break;

                    case DONKEY_KONG:
                        System.out.println("I want to kill Mario");
                        heroAbility.setHeroAbilityIsActivated(true);
                        heroAbility.setHeroAttack(hero);
                        break;

                    default:
                        break;
                }

            }
        }
    }


    private void goRight() {
        if (!checkCollision(fallingBlock, fallingBlock.getX() + 1, fallingBlock.getY())) {
            fallingBlock.goRight();
        }
    }

    private void goLeft() {
        if (!checkCollision(fallingBlock, fallingBlock.getX() - 1, fallingBlock.getY())) {
            fallingBlock.goLeft();
        }
    }

    private void rotate() {
        final FallingBlock rotatedBlock = new FallingBlock(fallingBlock.rotate());

        if (!checkCollision(rotatedBlock, fallingBlock.getX(), fallingBlock.getY())) {
            //Logger.warn("Rotate: did not collide!");
            fallingBlock.applyRotation();
        }
    }

    private void goSonic() {
        Vertx.currentContext().owner().cancelTimer(periodicID);
        periodicID = Vertx.currentContext().owner().setPeriodic(FAST_MOVEMENT_TIME, this::updateCycle);
    }

    private void stopSonic() {
        Vertx.currentContext().owner().cancelTimer(periodicID);
        periodicID = Vertx.currentContext().owner().setPeriodic(Math.round(normalMovementTime), this::updateCycle);
    }

    private void goSuperSonic() {
        boolean placed;
        int levels = 0;

        do {
            placed = nextBlockFall();
            levels++;
        } while (!placed);

        score += levels * SUPER_SONIC_POINTS;
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

        if (checkCollision(fallingBlock, fallingBlock.getX(), fallingBlock.getY() + 1)) {
            isNew = true;
            placeBlock(fallingBlock, fallingBlock.getX(), fallingBlock.getY(), fallingBlock.getId());

            if (fallingBlock.getY() <= 0) {
                die();
            } else {
                fallingBlock = nextFallingBlock;
                getNextFallingBlock();
                final boolean hasScored = checkAndScoreFullRows();
                Logger.debug(hasScored);

                if (10 * level <= amountOfScoredLines) {
                    level++;
                    normalMovementTime /= 1.5;
                    updateSpeed();
                }

                countNumberOfBlocksDK();
            }
        } else {
            fallingBlock.fall();

            countTimerPikachu();
        }

        return isNew;
    }

    private void countTimerPikachu() {
        if (heroAbility.isHeroAbilityIsActivated()) {
            if (heroAbility.getHeroAttack().equals(PIKACHU)) {
                System.out.println("TIMER " + timerAbility);

                if (timerAbility == 0) {
                    heroAbility.setActivatedHero(-1);
                    heroAbility.setHeroAbilityIsActivated(false);
                    timerAbility = 30;
                }

                timerAbility--;

            }
        }
    }

    private void countNumberOfBlocksDK() {
        if (heroAbility.isHeroAbilityIsActivated()) {
            if (heroAbility.getHeroAttack().equals(DONKEY_KONG)) {
                System.out.println("AMOUNT " + countAmountBlocksDonkeyKong);

                if (countAmountBlocksDonkeyKong == 2) {
                    heroAbility.setActivatedHero(-1);
                    heroAbility.setHeroAbilityIsActivated(false);
                    countAmountBlocksDonkeyKong = 0;
                    stopSonic();
                }
                countAmountBlocksDonkeyKong++;
            }
        }
    }

    private Integer[][] getPlayingFieldWithFallingBlock() {
        final Integer[][] pwfb = new Integer[playingField.length][];
        final boolean collided = checkCollision(fallingBlock, fallingBlock.getX(), fallingBlock.getY());

        final int maxHeight = fallingBlock.getPattern().length;
        final int maxWidth = fallingBlock.getPattern()[0].length;

        for (int i = 0; i < playingField.length; i++) {
            pwfb[i] = new Integer[playingField[i].length];

            for (int j = 0; j < playingField[i].length; j++) {
                final int yDiff = i - fallingBlock.getY();
                final int xDiff = j - fallingBlock.getX();

                //System.out.println(yDiff + " " + xDiff);

                if ((0 <= yDiff && 0 <= xDiff) && (yDiff < maxHeight && xDiff < maxWidth)) {
                    //System.out.println("Gets here");
                    if (fallingBlock.getPattern()[yDiff][xDiff] && !collided) {
                        pwfb[i][j] = fallingBlock.getId();
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

    private void sendUpdate() {
        final Context context = Vertx.currentContext();
        final EventBus eb = context.owner().eventBus();

        final Map<String, Object> data = new HashMap<>();
        data.put("playingField", getPlayingFieldWithFallingBlock());
        data.put("playerId", playerID);
        data.put("score", score);
        data.put("abilityCost", 500);
        data.put("isDead", isDead);


        String json = "<ERROR>";

        try {
            json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Logger.info("Update to client! <3 : tetris.events.client.game." + gameAddress);

        eb.publish("tetris.events.client.game." + gameAddress, json);
    }

    private void updateSpeed() {
        Logger.warn("Speed updated! Lines scored: " + amountOfScoredLines + " Level: " + level);
        Vertx.currentContext().owner().cancelTimer(periodicID);
        periodicID = Vertx.currentContext().owner().setPeriodic(Math.round(normalMovementTime), this::updateCycle);
    }

    private void disable() {
        consumer.unregister();
    }

    private void die() {
        Logger.warn("Die NYI");
        final Context context = Vertx.currentContext();
        Logger.warn("Periodic: " + periodicID);
        context.owner().cancelTimer(periodicID);
        disable();
        Logger.debug(isDead);
        isDead = true;
    }

    private boolean checkAndScoreFullRows() {
        boolean isFullLine;
        boolean hasScored = false;
        int totalLineScore = 0;

        int totalLines = 0;

        for (int i = 0; i < playingField.length; i++) {
            totalLineScore = 0;
            isFullLine = true;
            for (int j = 0; j < playingField[i].length; j++) {
                if (playingField[i][j] != null) {
                    if (0 < playingField[i][j]) {
                        totalLineScore += playingField[i][j];
                    } else {
                        isFullLine = false;
                    }
                } else {
                    isFullLine = false;
                }

            }

            if (isFullLine) {
                dropTopLayers(i);
                hasScored = true;
                score += totalLineScore * FULL_LINE_POINTS;
                amountOfScoredLines++;
                totalLines++;
            }
        }

        if (totalLines >= 2) {
            System.out.println("Proficiat je scoorde zonet 2 lijnen of meer tergelijk!!!!!");
            eventHandler.spawnUnbreakable(this);
        }

        return hasScored;
    }

    private void dropTopLayers(int lineHeight) {
        System.arraycopy(playingField, 0, playingField, 1, lineHeight);
        emptyLine(0);
    }

    private void emptyLine(int lineHeight) {
        playingField[lineHeight] = new Integer[Game.PLAYING_FIELD_WIDTH];

        for (int j = 0; j < Game.PLAYING_FIELD_WIDTH; j++) {
            playingField[lineHeight][j] = 0;
        }
    }

    private boolean checkCollision(Block block, int x, int y) {
        boolean collided = false;

        final Boolean[][] blockPattern = block.getPattern();

        final int maxHeight = blockPattern.length;
        final int maxWidth = blockPattern[0].length;

        if (x + maxWidth <= Game.PLAYING_FIELD_WIDTH && y + maxHeight <= Game.PLAYING_FIELD_HEIGHT && x >= 0) {
            for (int i = 0; i < maxHeight; i++) {
                for (int j = 0; j < maxWidth; j++) {
                    if (playingField[y + i][x + j] != null) {
                        if (blockPattern[i][j] && 0 < playingField[y + i][x + j]) {
                            collided = true;
                        }
                    } else {
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
        final int maxHeight = block.getPattern().length;
        final int maxWidth = block.getPattern()[0].length;

        if (!checkCollision(block, x, y)) {
            //System.out.println("Placed!");
            for (int i = 0; i < maxHeight; i++) {
                for (int j = 0; j < maxWidth; j++) {
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
        this.address = "tetris.events.server.game." + sessionID;
    }

    public FallingBlock getFallingBlock() {
        return fallingBlock;
    }

    private Integer[] createRow() {
        return new Integer[Game.PLAYING_FIELD_WIDTH];
    }

    private void createPlayingFields() {
        playingField = new Integer[Game.PLAYING_FIELD_HEIGHT][];

        for (int y = 0; y < Game.PLAYING_FIELD_HEIGHT; y++) {
            playingField[y] = createRow();
        }

        //System.out.println("PLayingfield length: " + playingField.length);

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

    public Integer[][] getPlayingField() {
        return playingField.clone();
    }

    public void setPlayingField(int x, int y, Integer value) {
        playingField[y][x] = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Player player = (Player) o;
        return playerID == player.playerID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID);
    }

    @Override
    public String toString() {
        return "Player{"
                + "user=" + user
                + ", address='" + address + '\''
                + '}';
    }

    public String playingFieldWithFallingBlock() {
        final StringBuilder sb = new StringBuilder();

        final int maxHeight = fallingBlock.getPattern().length;
        final int maxWidth = fallingBlock.getPattern()[0].length;

        for (int i = 0; i < playingField.length; i++) {
            for (int j = 0; j < playingField[i].length; j++) {
                final int yDiff = i - fallingBlock.getY();
                final int xDiff = j - fallingBlock.getX();

                //System.out.println(yDiff + " " + xDiff);

                if ((0 <= yDiff && 0 <= xDiff) && (yDiff < maxHeight && xDiff < maxWidth)) {
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
