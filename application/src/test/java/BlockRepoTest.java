import data.blockrepository.BlockRepository;
import data.Repositories;
import org.junit.Test;

public class BlockRepoTest {
    @Test
    public void testGetBlocks() {
        // VISUAL TEST: Set<> is to unpredictable!
        BlockRepository repo = Repositories.getInstance().getBlockRepository();

        System.out.println(repo.getBlocks());

        System.out.println();

        System.out.println(repo.getRandomBlock());
    }
}
