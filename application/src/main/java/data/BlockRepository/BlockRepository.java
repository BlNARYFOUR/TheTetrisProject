package data.BlockRepository;

import domain.game.Block;

import java.util.Set;

public interface BlockRepository {
    Set<Block> getBlocks();
    Block getRandomBlock();
}
