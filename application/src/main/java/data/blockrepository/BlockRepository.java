package data.blockrepository;

import domain.game.Block;

import java.util.Set;

/**
 * Contract for BlockRepository.
 */
public interface BlockRepository {
    Set<Block> getBlocks();
    Block getRandomBlock();
}
