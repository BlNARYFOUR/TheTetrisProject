package domain.game;

import data.Repositories;

/**
 * A block that falls.
 */
public class FallingBlock extends Block {
    private int x;
    private int y;

    FallingBlock(final Block block) {
        super(block.getId(), block.getPattern(), block.getName());
        x = (int) Math.floor((Game.PLAYING_FIELD_WIDTH - block.getPattern()[0].length) / 2);
        y = 0;
    }

    FallingBlock() {
        this(Repositories.getInstance().getBlockRepository().getRandomBlock());
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    protected void goLeft() {
        x--;
    }

    protected void goRight() {
        x++;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    protected void fall() {
        this.y += 1;
    }

    protected void applyRotation() {
        final FallingBlock rotatedBlock = new FallingBlock(this.rotate());
        this.setPattern(rotatedBlock.getPattern());
    }
}
