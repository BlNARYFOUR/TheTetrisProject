package domain.game;

import java.util.Arrays;
import java.util.Objects;

/**
 * Block class.
 */
public class Block {
    private int id;
    private String name;
    private Boolean[][] pattern;

    public Block(int id, Boolean[][] pattern, String name) {
        setPattern(pattern);
        setName(name);
        setId(id);
    }
    public Block(int id, Boolean[][] pattern) {
        this(id, pattern, "[Empty]");
    }

    public Block rotate() {
        final Boolean[][] rotatedPattern = new Boolean[pattern[0].length][];

        for (int y = 0; y < pattern[0].length; y++) {
            rotatedPattern[y] = new Boolean[pattern.length];

            for (int x = 0; x < pattern.length; x++) {
                rotatedPattern[y][x] = pattern[pattern.length - x - 1][y];
            }
        }

        return new Block(this.id, rotatedPattern);
    }

    public Boolean[][] getPattern() {
        return pattern;
    }

    void setPattern(Boolean[][] pattern) {
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Block block = (Block) o;
        return getId() == block.getId()
                && Objects.equals(getName(), block.getName())
                && Arrays.deepEquals(getPattern(), block.getPattern());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(getId(), getName());
        result = 31 * result + Arrays.hashCode(getPattern());
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("Block").append('-').append(getId()).append(':').append(getName()).append(" = {");

        for (Boolean[] row : pattern) {
            sb.append("\n\t{");

            for (int x = 0; x < row.length; x++) {
                if (row[x]) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }

                if (x < row.length - 1) {
                    sb.append(", ");
                }
            }

            sb.append('}');
        }

        sb.append("\n}");

        return sb.toString();
    }
}
