package domain.game;

import java.util.Arrays;

public class Block {
    private String name;
    private Boolean[][] pattern;

    public Block(Boolean[][] pattern, String name) {
        setPattern(pattern);
        setName(name);
    }
    public Block(Boolean[][] pattern) {
        this(pattern, "[Empty]");
    }

    public Block rotate() {
        Boolean[][] rotatedPattern = new Boolean[pattern[0].length][];

        for(int y=0; y<pattern[0].length; y++) {
            rotatedPattern[y] = new Boolean[pattern.length];

            for(int x=0; x<pattern.length; x++) {
                rotatedPattern[y][x] = pattern[pattern.length - x - 1][y];
            }
        }

        return new Block(rotatedPattern);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Arrays.deepEquals(getPattern(), block.getPattern());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getPattern());
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();

        sb.append("Block:").append(getName()).append(" = {");

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
