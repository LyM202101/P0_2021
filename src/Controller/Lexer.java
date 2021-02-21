package Controller;

import Tokens.Keyword;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Un lexer es responsable del proceso de tokenizacion del texto, lo cual es como pre procesarlo y organizarlo,
 * de tal manera que sea mas sencillo entender la estructura del codigo para que sea mas facil definir si cumple
 * o no las caracteristicas en el interpreter
 */
public class Lexer {

    /**
     * Este arreglo contiene la informacion de los comandos simples que tiene el robot
     */
    static final String[] commands = {"walk", "rotate", "look", "drop", "free", "pick", "grab", "walkTo","NOP","block","if", "define"};




    //-------------------------------------------------------------------------------------
    // METODOS
    //-------------------------------------------------------------------------------------

    // Asi se ve el contenido de una rutina (el arraylist) que es lines ;  { (r), (i(n(b))(w)(n)), (b(i(n(b))(w)(n))(r)),(d),(d(c)(b(d)(f)(w))),(f)}

    /**
     * Este metodo procesa todos los bloques de codigo de un archivo en particular
     * @param codeBlock un bloque o unidad de codigo ej: (i(n(b))(w)(n))
     * @return Un arrayList que contiene la keyword que coresponde a un boloque de codigo
     */
    //TODO: Re considerar si deberia usar un HashMap en lugar de una Arraylist para esto
    public static ArrayList<Keyword> processRoutine(ArrayList<String> codeBlock) {
        ArrayList<Keyword> keywordList = new ArrayList<>();
        for (String block : codeBlock) {
            keywordList.add(processCodeBlock(block));
        }
        return keywordList;
    }

    //TODO : Metodo que procese un bloque de codigo individualmente
    public static Keyword processCodeBlock(String codeBlock){
        //Formato del string ej's : ((r)), (i(n(b))(w)(n))

        String cleanedBlock = cleanedBlock(codeBlock);




        return null;
    }

    public static String cleanedBlock(String str){
        String cleanedBlock = "";

        //Uno tiene que lidiar de una forma completamente diferente con las user defined functions y variables que con el resto del codigo

        //Si el bloque de codigo comienza con (( hay 3 posibles opciones validas ((f a1 . . . an)) que es cuando se esta llamando a una funcion que ha sido definida por el usuario
        if(str.charAt(0) == '(' && str.charAt(1) == '('){
            cleanedBlock = cleanUpOuterParenthesis(str,2);

            //Esto va a añadir una nueva funcion a la lista de funciones definidas por el usuario asi cuando se cree el token no habra problemsas
            String[] newUserFunc = cleanedBlock.split(" ");
            Keyword.addUsrDefinedFunction(newUserFunc[0]);
        }
        //El resto de funciones que pueden comenzar un bloque de codigo. Incluye commands, condicionales y definiciones
        else{
            cleanedBlock = cleanUpOuterParenthesis(str,1);
        }
        return cleanedBlock;

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
