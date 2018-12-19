package data.blockrepository;

import domain.game.Block;

import java.util.HashSet;
import java.util.Set;

public class HcBlockRepository implements BlockRepository {
    private int nextID = 1;

    private static final Boolean[][] PATTERN_LINE = {
            {true, true, true, true}
    };

    private static final Boolean[][] PATTERN_L_1 = {
            {true, false, false},
            {true, true, true}
    };

    private static final Boolean[][] PATTERN_L_2 = {
            {false, false, true},
            {true, true, true}
    };

    private static final Boolean[][] PATTERN_SQUARE = {
            {true, true},
            {true, true}
    };

    private static final Boolean[][] PATTERN_ZIGZAG_1 = {
            {false, true, true},
            {true, true, false}
    };

    private static final Boolean[][] PATTERN_ZIGZAG_2 = {
            {true, true, false},
            {false, true, true}
    };

    private static final Boolean[][] PATTERN_T = {
            {false, true, false},
            {true, true, true}
    };

    private static final Boolean[][] PATTERN_CROSS = {
            {false, true, false},
            {true, true, true},
            {false, true, false}
    };

    private static final Boolean[][] PATTERN_TEA = {
            {true, true, true},
            {false, true, false},
            {false, true, false}
    };
    private static final Boolean[][] PATTERN_HORSESHOE = {
            {true, false, true},
            {true, true, true}
    };



    private Set<Block> blocks = new HashSet<>();

    public HcBlockRepository() {
        registerBlock(PATTERN_LINE, "LINE");
        registerBlock(PATTERN_L_1, "L_1");
        registerBlock(PATTERN_L_2, "L_2");
        registerBlock(PATTERN_SQUARE, "SQUARE");
        registerBlock(PATTERN_ZIGZAG_1, "ZIGZAG_1");
        registerBlock(PATTERN_ZIGZAG_2, "ZIGZAG_2");
        registerBlock(PATTERN_T, "T");
        registerBlock(PATTERN_CROSS, "CROSS");
        registerBlock(PATTERN_TEA, "TEA");
        registerBlock(PATTERN_HORSESHOE, "HORSESHOE");
    }

    @SuppressWarnings("WeakerAccess")
    public void registerBlock(Boolean[][] pattern, String name) {
        blocks.add(new Block(nextID, pattern, name));
        nextID++;
    }

    @Override
    public Set<Block> getBlocks() {
        return blocks;
    }

    @Override
    public Block getRandomBlock() {
        double r = Math.random();

        return (Block) blocks.toArray()[Math.toIntExact(Math.round(r * (blocks.size() - 1)))];
    }
}
