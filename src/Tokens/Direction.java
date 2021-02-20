package Tokens;

import java.util.Arrays;

/**
 * Esta clase especifica los tokens de direccion. Estos pueden ser o una direccion cardinal o una direccion relativa.
 */
public class Direction extends Token {
    /**
     * \
     * Define las direcciones cardinales: N,S,W,E
     */
    public static Directions[] CARDINAL_DIRS = new Directions[]{Directions.NORTH, Directions.SOUTH, Directions.EAST, Directions.WEST};
    /**
     * Define las direcciones relativas: L,R,B
     */
    public static Directions[] RELATIVE_DIRS = new Directions[]{Directions.LEFT, Directions.RIGHT, Directions.BACK};


    /**
     * Especifica la direccion Cardinal o Relativa del robot
     */
    public Directions direction;

    /**
     * Constructor de un token de direccion cardinal o relativa.
     *
     * @param dir la direccion cardinal o relativa
     */
    public Direction(Directions dir) {
        super(Tag.DIR);
        direction = dir;
    }

    /**
     * Obtierne la direccion de un token de direccion en particular
     * @return El enum de direccion, este puede ser una direccion cardinal o relativa
     */
    public Directions getDirection() {
        return direction;
    }

    /**
     * Verifica si el token actual es Cardinal
     * @return True si la direccion es cardinal, false de lo contrario
     */
    public boolean isCardinal() {
        return Arrays.asList(CARDINAL_DIRS).contains(direction);
    }


    /**
     * Verifica si el token actual es Relativa
     * @return True si la direcion es relativa, false de lo contrario
     */
    public boolean isRelative() {
        return Arrays.asList(RELATIVE_DIRS).contains(direction);
    }

    public static Directions findCardDir(String str) {
        Directions dirOf = null;

        switch (str) {
            case "north":
                dirOf = Directions.NORTH;
                break;
            case "south":
                dirOf = Directions.SOUTH;
                break;
            case "west":
                dirOf = Directions.WEST;
                break;
            case "east":
                dirOf = Directions.EAST;
                break;
        }
        return dirOf;
    }

    public static Directions findRelDir(String str) {
        Directions dirOf = null;
        switch (str) {
            case "left":
                dirOf = Directions.LEFT;
                break;
            case "right":
                dirOf = Directions.RIGHT;
                break;
            case "back":
                dirOf = Directions.BACK;
                break;
        }
        return dirOf;
    }

}