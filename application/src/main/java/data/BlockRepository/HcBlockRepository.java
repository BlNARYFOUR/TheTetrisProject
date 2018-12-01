package data.BlockRepository;

import domain.game.Block;

import java.util.HashSet;
import java.util.Set;

public class HcBlockRepository implements BlockRepository {
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

    private static Set<Block> blocks = new HashSet<>();

    public HcBlockRepository() {
        blocks.add(new Block(PATTERN_LINE, "LINE"));
        blocks.add(new Block(PATTERN_L_1, "L_1"));
        blocks.add(new Block(PATTERN_L_2, "L_2"));
        blocks.add(new Block(PATTERN_SQUARE, "SQUARE"));
        blocks.add(new Block(PATTERN_ZIGZAG_1, "ZIGZAG_1"));
        blocks.add(new Block(PATTERN_ZIGZAG_2, "ZIGZAG_2"));
        blocks.add(new Block(PATTERN_T, "T"));
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
