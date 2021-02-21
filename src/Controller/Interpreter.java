package Controller;

import Tokens.Keyword;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * El lexer es quien recibe los comandos y es responsable de verificar si siguen la sintaxis apropiada de cada uno definido por el lenguje
 */
public class Interpreter {
    /**
     * En este arreglo se guardan las funciones definidas por el usuario
     */
    static final ArrayList<String> userDefinedFunctions = new ArrayList<>();

    /**
     * En este arreglo se guardan las variables definidas por el usuario
     */
    static final HashMap<String, Integer> userDefinedVariable = new HashMap<>();

    //-------------------------------------------------------------------------------------
    // METODOS
    //-------------------------------------------------------------------------------------

    /**
     * Este metodo recibe los bloques de instrucciones del archivo que se lee y es el responsable de verificar la sintaxis del programa como un todo.
     * Lo primero que hace es llamar al lexer. este le devuleve cada bloque de codigo tokenizado como una keyword de tal manera que sea mas facil procesarlo,
     * Cuando ya se termino el llamado al lexer se comienza a verificar la validez de la sintaxis de cada bloque de codigo y si cada uno sigue las reglas de la gramatica del robot.
     * @param instructionBlocks Un ArrayList que contiene los bloques de instrucciones que se encontraron en el programa
     * @return True si todos los bloque s de codigo son validos, False si alguno de los bloques de codigo no es valido sintacticamente
     */
    public static boolean process(ArrayList<String> instructionBlocks ){
        //La console ya hace los basic checks tal como que los parentesis esten balanceados

        //Este metodo llama el lexer para que procese la rutina y que tokenize la primera parte de cada bloque de codigo.
        ArrayList<Keyword> tokenList = Lexer.processRoutine(instructionBlocks);

        //Aqui se llama a parse and check, este metodo es responsable de verificar la validez de cada bloque segun el keyword con el que comienza
        return parseAndCheck(tokenList, instructionBlocks);
    }


    private static boolean parseAndCheck(ArrayList<Keyword> tokenList, ArrayList<String> instructionBlocks){
        for(int i = 0 ; i < instructionBlocks.size() ; i++){
            //COSAS
        }
        return true;
    }


}
