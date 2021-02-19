package OldTokens;


public class Direction extends Token {
    public Directions direction;

    public Direction(Directions dir) {
        super(Tag.DIR);
        direction = dir;
    }

    public enum Directions {
        NORTH, EAST, SOUTH, WEST,
        LEFT, RIGHT, BACK, FRONT, AROUND
    }
}
