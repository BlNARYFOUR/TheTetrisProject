package domain.game;

import data.BlockRepository.BlockRepository;
import data.Repositories;

public class FallingBlock extends Block {
    private int x, y;

    public FallingBlock(Block block) {
        super(block.getID(), block.getPattern(), block.getName());
        x = (int) Math.floor((Game.PLAYING_FIELD_WIDTH - block.getPattern()[0].length) / 2);
        y = 0;
    }

    FallingBlock() {
        this(Repositories.getInstance().getBlockRepository().getRandomBlock());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void goLeft() {
        x--;
    }

    public void goRight() {
        x++;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void fall() {
        this.y += 1;
    }
}
