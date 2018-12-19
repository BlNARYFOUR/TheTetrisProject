package tests;

import domain.game.Block;
import org.junit.Test;
import org.pmw.tinylog.Logger;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Test.
 */
public class BlockTest {
    @Test
    public void testBlockToString() {
        final Boolean[][] pattern = {
                {false, false, true},
                {true, true, true},
        };

        final Boolean[][] rotatedPattern = {
                {true, false},
                {true, false},
                {true, true},
        };

        final Block block = new Block(0, pattern);
        final Block rotatedExpected = new Block(0, rotatedPattern);

        Logger.info(block);

        final Block rotated = block.rotate();

        Logger.info(rotated);

        assertEquals(rotatedExpected, rotated);

        Logger.info(Arrays.deepToString(rotated.getPattern()));
        Logger.info(Arrays.deepToString(rotatedExpected.getPattern()));
    }
}
