package Controller;

import java.util.ArrayList;

public class Lexer {

    //TODO: Implementar los TOKENS en el paquete de tokens

    /**
     * Este arreglo contiene la informacion de los comandos simples que tiene el robot
     */
    static final String[] command = {"walk", "rotate", "look", "drop", "free", "pick", "grab", "walkTo","NOP","block","if", "define"};

    //TODO: Hay que encontrar una manera de ir añadiendo a una lista a las funciones que se vayan definiendo




    public static ArrayList<Keyword> processRoutine(String[] lines){
        ArrayList<Keyword> KeywordList = new ArrayList<>();
    }
}
