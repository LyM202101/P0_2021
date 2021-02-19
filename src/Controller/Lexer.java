package Controller;

import Tokens.Keyword;

import java.util.ArrayList;

public class Lexer {

    //TODO: Implementar los TOKENS en el paquete de tokens

    /**
     * Este arreglo contiene la informacion de los comandos simples que tiene el robot
     */
    static final String[] commands = {"walk", "rotate", "look", "drop", "free", "pick", "grab", "walkTo","NOP","block","if", "define"};

    /**
     * En este arreglo se guardan las funciones definidas por el usuario
     */
    static final ArrayList<String> userDefinedFunctions = new ArrayList<>();

    /**
     * En este arreglo se guardan las variables definidas por el usuario
     */
    static final ArrayList<String> userDefinedVariable = new ArrayList<>();

    //TODO: Hay que encontrar una manera de ir añadiendo a una lista a las funciones que se vayan definiendo. Aunque creo que eso seria la responsabilidad del interpreter

    //-------------------------------------------------------------------------------------
    // METODOS
    //-------------------------------------------------------------------------------------

    // Asi se ve el contenido de una rutina (el arraylist) que es lines ;  { (r), (i(n(b))(w)(n)), (b(i(n(b))(w)(n))(r)),(d),(d(c)(b(d)(f)(w))),(f)}

    //TODO: Metodo que procese una lista de bloques de codigo
    public static ArrayList<Keyword> processRoutine(String[] lines) {
        ArrayList<Keyword> keywordList = new ArrayList<>();
        for (String line : lines) {
            keywordList.add(processCodeBlock(line));
        }
        return keywordList;
    }

    //TODO : Metodo que procese un bloque de codigo individualmente
    public static Keyword processCodeBlock(String codeBlock){
        //Formato del string ej's : ((r)), (i(n(b))(w)(n))

        String cleanedBlock = "";

        //Uno tiene que lidiar de una forma completamente diferente con las user defined functions y variables que con el resto del codigo

        //Si el bloque de codigo comienza con (( hay 3 posibles opciones validas ((f a1 . . . an)) que es cuando se esta llamando a una funcion que ha sido definida por el usuario
        if(codeBlock.charAt(0) == '(' && codeBlock.charAt(1) == '('){
            cleanedBlock = cleanUpOuterParenthesis(codeBlock,2);

            //Esto va a añadir una nueva funcion a la lista de funciones definidas por el usuario asi cuando se cree el token no habra problemsas
            String[] newUserFunc = cleanedBlock.split(" ");
            Keyword.addUsrDefinedFunction(newUserFunc[0]);
        }
        //El resto de funciones que pueden comenzar un bloque de codigo. Incluye commands, condicionales y definiciones
        else{
            cleanedBlock = cleanUpOuterParenthesis(codeBlock,1);
        }

        //remove this cuando termine el metodo
        return null;
    }

    //TODO:Metodo que limpia los surrounding parentesis del codeblock
    public static String cleanUpOuterParenthesis(String codeBlock, int numPar){

        int numChars = codeBlock.length()- 1;
        String cleanedUpBlock = "";

        //Para las funciones con solo un set de outer parenthesis (f)
        if(numPar == 1){
            cleanedUpBlock = codeBlock.substring(2,(numChars - 2));
        }
        //Para las funciones con un set de 2 outer parenthesis ((f a1..an))
        else{
            cleanedUpBlock = codeBlock.substring(1,(numChars-1));
        }

        assert !cleanedUpBlock.equals("");


        return cleanedUpBlock;

    }

    //TODO: Metodo que revisa el tipo de argumento


    //TODO: Metodos que revisan si un argumento(str) es INT, CARDIR, RELDIR, ENTITY








}
