import domain.game.Block;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class BlockTest {
    @Test
    public void testBlockToString() {
        Boolean[][] pattern = {
                {false, false, true},
                {true, true, true}
        };

        Boolean[][] rotatedPattern = {
                {true, false},
                {true, false},
                {true, true}
        };

        Block block = new Block(pattern);
        Block rotatedExpected = new Block(rotatedPattern);

        System.out.println(block);

        Block rotated = block.rotate();

        System.out.println(rotated);

        assertEquals(rotatedExpected, rotated);

        System.out.println(Arrays.deepToString(rotated.getPattern()));
        System.out.println(Arrays.deepToString(rotatedExpected.getPattern()));
    }
}
