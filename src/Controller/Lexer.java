package Controller;

import Tokens.Keyword;
import Tokens.Tag;

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

    /**
     * Procesa un bloque de codigo individual. Se le hace basic tokenization para poder hacer el proceso de procesar mas facil
     * @param codeBlock un String con un bloque de codigo
     * @return Un Keyword que contiene el tag y actua como un token
     */
    public static Keyword processCodeBlock(String codeBlock){
        //Quita los parentesis exteriores
        String cleanedBlock = cleanedBlock(codeBlock);

        //Saca la palabra inicial del bloque de codigo (todas tienen un espacio que las separa del resto del bloque)
        String initialKeyword = cleanedBlock.split(" ")[0];

        //Encuentra cual tag debe tener el keyword
        Tag keyTag = Keyword.findCmdTag(cleanedBlock);

        //Crea una keyword con el codeBlock original y su keytag correspondiente
        Keyword blockKeyWord = new Keyword(cleanedBlock,keyTag );

        return null;
    }

    /**
     * Quita los parentesis exteriores de un bloque de codigo
     * @param str el string que esta rodeado por parentesis '(' ')'
     * @return El mismo bloque str pero sin los parentesis exeriores
     */
    public static String cleanedBlock(String str){
        String cleanedBlock = "";

        if(str.charAt(0) == '(' && str.charAt(1) == '('){
            cleanedBlock = cleanUpOuterParenthesis(str,2);
            String[] newUserFunc = cleanedBlock.split(" ");
        }
        else{
            cleanedBlock = cleanUpOuterParenthesis(str,1);
        }
        return cleanedBlock;
    }


    /**
     *  Saca un substring del string original basado en el numero de parenteiss
     * @param codeBlock el bloque de codigo original
     * @param numPar el numero de parentesis
     * @return Un string sin el nuemro de parentesis
     */
    public static String cleanUpOuterParenthesis(String codeBlock, int numPar){
        int numChars = codeBlock.length()- 1;
        String cleanedUpBlock = "";

        if(numPar == 1){
            cleanedUpBlock = codeBlock.substring(1,(numChars));
        }
        else{
            cleanedUpBlock = codeBlock.substring(2,(numChars-1));
        }

        assert !cleanedUpBlock.equals("");


        return cleanedUpBlock;
    }


    public static boolean isCodeBlockBasicFunction(String codeBlock){

    }
}
