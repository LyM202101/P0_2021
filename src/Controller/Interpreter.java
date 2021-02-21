package Controller;

import Tokens.Keyword;
import Tokens.Tag;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Establece las keywords y bloques de codigo que corresponden
     */
    static HashMap<String,Keyword> keywordsAndCodeBlocks;

    public static Tag[] BASIC_COMMANDS = new Tag[]{Tag.DROP, Tag.WALK, Tag.ROTATE, Tag.LOOK, Tag.DROP, Tag.FREE, Tag.PICK, Tag.GRAB, Tag.WALK_TO, Tag.NOP};

    //-------------------------------------------------------------------------------------
    // PROCESSAMIENTO
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

        HashMap<String,Keyword> codeBlockKeyword = new HashMap<>();

        //Crea un hashmap de las instrucciones y su keyword respectivas
        for(int i = 0 ; i < instructionBlocks.size(); i++){
            codeBlockKeyword.put(instructionBlocks.get(i), tokenList.get(i));
        }
        keywordsAndCodeBlocks = codeBlockKeyword;

        //Aqui se llama a parse and check, este metodo es responsable de verificar la validez de cada bloque segun el keyword con el que comienza
        return parseAndCheck(codeBlockKeyword, instructionBlocks);
    }


    /**
     * Es el metodo responsable de validar cada bloque de codigo de acuerdo al tag y keyword que le fue asignado. Verifica que el programa este siguiendo o no la sintaxis definida para el robot
     * @param blocksAndKeywords Hash map con todos los bloques y sus respectivas keywords
     * @param instructionBlocks Lista con todos los bloques de instrucciones
     * @return True si todos los bloques son validos, false si al menos un bloque de codigo es falso
     */
    private static boolean parseAndCheck(HashMap<String,Keyword> blocksAndKeywords, ArrayList<String> instructionBlocks){
        boolean valid = true;
        String currInstructionBlock;
        Keyword currentKeyword;
        Tag currKeywordTag;

        for(int i = 0 ; i < instructionBlocks.size() && !valid; i++){

            currInstructionBlock = instructionBlocks.get(i);

            //Se saca el keyword que correspone al bloque actual
            currentKeyword = blocksAndKeywords.get(currInstructionBlock);

            //Saca el tag del keyword actual
            currKeywordTag = currentKeyword.getTag();

            //BASIC COMMANDS

            //Si el Tag del Keyword actual es alguno de los comandos actuales este redirige a un metodo que se encarga de lidiar con los basic commands
            if(Arrays.asList(BASIC_COMMANDS).contains(currKeywordTag)){
                //TODO: Implementar metodo para deal with basic commands. Tiene que return bool

                //valid = basicComandCheck(currInstructionBlock,currentKeyword,currKeywordTag);
            }


            //CONDITIONALS -> IF STATEMENT
            else if(currKeywordTag == Tag.IF){
                //TODO: Implementar metodo para deal with if statements. tiene que return bool

                //valid = conditionalStatementCheck(currInstructionBlock,currentKeyword,currKeywordTag);
            }


            //BLOCK
            else if(currKeywordTag == Tag.BLOCK){
                //TODO: Implementar metodo para deal with Block statements. Tiene que return bool
                //valid = blockStatementCheck(currInstructionBlock,currentKeyword,currKeywordTag);
            }

            //DEFINE FUNCTIONS AND VARIABLES
            else if(currKeywordTag == Tag.DEFINE){
                //TODO: Implementar metodo para deal with DEFINE statements tanto para variables como funciones. Tiene que return bool

                //Check pre eliminar de la estructura para ver si es la definicion de una funcion o la de una funcion

                //NOTA: Una forma boba de revisar que se esta declarando es el numero de parentesis. Define de var solo puede tener 2, Define de funciones tiene que tener mas que 2

                //NOTA: Tanto define function como define variable deben añadir a su respectivas listas / hash maps la funcion o variable para que se puedan reconocer cuando las llamen mas tarde en el programa

                //DEFINE FUNCTION
                if(countParenthesis(currInstructionBlock) > 2){
                    //valid = defineFunctionCheck(currInstructionBlock,currentKeyword,currKeywordTag);
                }
                //DEFINE VARIABLE
                else{
                    //valid = defineVariableCheck(currInstructionBlock,currentKeyword,currKeywordTag);
                }
            }

            //USER FUNCTIONS

           /* NOTA: Cuando en el Block de commandos no se pudo encontrar un tag adecuado se agrupa bajo Tag.USER_FUN_VAR ya que se puede estar llamando a una user defined o function.
            Por lo tanto en esos casos se tiene que revisar si existen vars o funciones con ese nombre. Si no existen entonces se concluye que no es valido el bloque y por lo tanto el programa no es valido.*/
            else{
                //REVISAR SI EL NOMBRE ES EL DE UNA FUNCION CONOCIDA
                //TODO: Implementar metodo para deal with possible User function calls y verificar que siga las especificaciones de esa funcion

                //valid = userDefinedFunctionCheck(currInstructionBlock,currentKeyword,currKeywordTag);
            }


        }


        //Si al finalizar el corrido valid sigue siendo verdad significa que todos los bloques de codigo pasaron la prueba. Si no sera false y esto sera lo que se le pasa a consolae
        return valid;
    }



    //------------------------------------------------------------------------------------------------------------------
    // BASIC COMMANDS METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Este metodo redirige a los metodos particulares que revisan que cada basic command este correcto sintacticamente
     * @param instructionBlock el string con el bloque de instrucciones que se esta evaluando
     * @param keyword la keyword de el bloque de instruccines que se esta evaluando
     * @param tag el tag de la keyword que se esta evaluando. En este caso tiene que ser uno de los que esta definido en BASIC_COMMANDS
     * @return TRUE si el basic command sigue la sintaxis indicada, False de lo contrario
     */
    public static boolean basicComandCheck(String instructionBlock,Keyword keyword, Tag tag){
        boolean valid = false;
        //  public static Tag[] BASIC_COMMANDS = new Tag[]{Tag.DROP, Tag.WALK, Tag.ROTATE, Tag.LOOK, Tag.DROP, Tag.FREE, Tag.PICK, Tag.GRAB, Tag.WALK_TO, Tag.NOP};

        switch (tag) {
            case WALK:
                valid = checkWalk(instructionBlock,keyword);
                break;
            case ROTATE:
                valid = checkRotate(instructionBlock,keyword);
                break;
            case LOOK:
                valid = checkLook(instructionBlock,keyword);
                break;
            case DROP:
                valid = checkDrop(instructionBlock,keyword) ;
                break;
            case FREE:
                valid = checkFree(instructionBlock,keyword);
                break;
            case PICK:
                valid = checkPick(instructionBlock,keyword);
                break;
            case GRAB:
                valid = checkGrab(instructionBlock,keyword);
                break;
            case WALK_TO:
                valid = checkWalkTo(instructionBlock,keyword) ;
                break;
            case NOP:
                valid = checkNOP(instructionBlock,keyword);
                break;
        }
        return valid;

    }


    public static boolean checkWalk(String instructionBlock,Keyword keyword){

    }

    public static boolean checkRotate(String instructionBlock,Keyword keyword){

    }

    public static boolean checkLook(String instructionBlock,Keyword keyword){

    }

    public static boolean checkDrop(String instructionBlock,Keyword keyword){

    }

    public static boolean checkFree(String instructionBlock,Keyword keyword){

    }

    public static boolean checkPick(String instructionBlock,Keyword keyword){

    }

    public static boolean checkGrab(String instructionBlock,Keyword keyword){

    }

    public static boolean checkWalkTo(String instructionBlock,Keyword keyword){

    }

    public static boolean checkNOP(String instructionBlock,Keyword keyword){

    }
    //------------------------------------------------------------------------------------------------------------------
    // CONDITIONAL IF STATEMENT METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean conditionalStatementCheck(String instructionBlock,Keyword keyword, Tag tag){

    }

    //------------------------------------------------------------------------------------------------------------------
    // BLOCK METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean  blockStatementCheck(String instructionBlock,Keyword keyword, Tag tag){

    }

    //------------------------------------------------------------------------------------------------------------------
    // DEFINE METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean defineFunctionCheck(String instructionBlock,Keyword keyword, Tag tag){

    }

    public static boolean defineVariableCheck(String instructionBlock,Keyword keyword, Tag tag){

    }

    //------------------------------------------------------------------------------------------------------------------
    // USER FUNCTION CALL METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean userDefinedFunctionCheck(String instructionBlock,Keyword keyword, Tag tag){

    }

    //------------------------------------------------------------------------------------------------------------------
    // HELPER METHODS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Este metodo cuenta el numero de parentesis presentes en un bloque
     * (Este metodo se usa para diferenciar si se esta definiendo una funcion o una variable)
     * @param block un bloque de codigo en forma de string
     * @return int con el numero de parentesis ( ) que se contaron en el string
     */
    public static int countParenthesis(String block){
        int numParenthesis = 0;
        for(int i = 0; i < block.length(); i++){
            char curChar = block.charAt(i);

            if(curChar == '(' || curChar == ')' ){
                numParenthesis++;
            }
        }
        return numParenthesis;
    }



}
