package domain.game;

import java.util.Arrays;
import java.util.Objects;

public class Block {
    private int ID;
    private String name;
    private Boolean[][] pattern;

    public Block(int ID, Boolean[][] pattern, String name) {
        setPattern(pattern);
        setName(name);
        setID(ID);
    }
    public Block(int ID, Boolean[][] pattern) {
        this(ID, pattern, "[Empty]");
    }

    public Block rotate() {
        Boolean[][] rotatedPattern = new Boolean[pattern[0].length][];

        for(int y=0; y<pattern[0].length; y++) {
            rotatedPattern[y] = new Boolean[pattern.length];

            for(int x=0; x<pattern.length; x++) {
                rotatedPattern[y][x] = pattern[pattern.length - x - 1][y];
            }
        }

        return new Block(this.ID, rotatedPattern);
    }

    public Boolean[][] getPattern() {
        return pattern;
    }

    private void setPattern(Boolean[][] pattern) {
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    private void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return getID() == block.getID() &&
                Objects.equals(getName(), block.getName()) &&
                Arrays.deepEquals(getPattern(), block.getPattern());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(getID(), getName());
        result = 31 * result + Arrays.hashCode(getPattern());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();

        sb.append("Block").append('-').append(getID()).append(':').append(getName()).append(" = {");

        for (Boolean[] row : pattern) {
            sb.append("\n\t{");

            for (int x = 0; x < row.length; x++) {
                if(row[x]) {
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
