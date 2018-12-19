package domain.game;

import data.Repositories;

import java.util.Objects;

/**
 * A block that falls.
 */
public class FallingBlock extends Block {
    private int x;
    private int y;

    FallingBlock(final Block block) {
        super(block.getId(), block.getPattern(), block.getName());
        x = (int) Math.floor((Game.PLAYING_FIELD_WIDTH - block.getPattern()[0].length) / 2.0);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FallingBlock)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final FallingBlock that = (FallingBlock) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getX(), getY());
    }
}
