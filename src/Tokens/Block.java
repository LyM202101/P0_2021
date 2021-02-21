package Tokens;

import java.util.ArrayList;

public class Block extends Token{

    /**
     * Esto es un arreglo que se encarga de guardar el set de comandos que esten dentro de un codeblock
     */
    String[] commands;

    /**
     * Constructor de un bloque de comandos
     * @param commands una lista de comandos (0 ..*) que tiene un bloque
     */
    public Block(String ... commands) {
        super(Tag.BLOCK);
        this.commands = commands;
    }

}
