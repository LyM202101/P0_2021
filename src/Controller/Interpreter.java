package Controller;

import Tokens.Keyword;
import Tokens.Tag;

import java.util.*;

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
    static final HashMap<String, String> userDefinedVariable = new HashMap<>();

    /**
     * Establece las keywords y bloques de codigo que corresponden
     */
    static HashMap<String,Keyword> keywordsAndCodeBlocks;

    public static Tag[] BASIC_COMMANDS = new Tag[]{Tag.DROP, Tag.WALK, Tag.ROTATE, Tag.LOOK, Tag.DROP, Tag.FREE, Tag.PICK, Tag.GRAB, Tag.WALK_TO, Tag.NOP};

    public static String[] basicCmd = new  String[]{"drop", "walk", "rotate", "look", "drop", "free", "pick", "grab", "walkTo", "NOP"};

    public static String[] STR_CARDINAL_DIRS = new String[]{"N", "S", "E", "W"};

    public static String[] STR_RELATIVE_DIRS = new String[]{"left", "right", "back"};

    public static String[] CONDITIONAL_STATEMENTS = new String[]{"blocked?", "facing?" ,"can", "not"};

    public static String[] STR_ROBOT_INSTRUCTIONS = new String[]{"grab", "drop", "free", "pick" };


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

        for(int i = 0 ; i < instructionBlocks.size() && valid; i++){

            currInstructionBlock = instructionBlocks.get(i);

            //Se saca el keyword que correspone al bloque actual
            currentKeyword = blocksAndKeywords.get(currInstructionBlock);

            //Saca el tag del keyword actual
            currKeywordTag = currentKeyword.getTag();

            //BASIC COMMANDS

            //Si el Tag del Keyword actual es alguno de los comandos actuales este redirige a un metodo que se encarga de lidiar con los basic commands
            if(Arrays.asList(BASIC_COMMANDS).contains(currKeywordTag)){
                valid = basicComandCheck(currentKeyword,currKeywordTag);
            }


            //CONDITIONALS -> IF STATEMENT
            else if(currKeywordTag == Tag.IF){
                valid = ifStatementCheck(currentKeyword);
            }


            //BLOCK
            else if(currKeywordTag == Tag.BLOCK){
                //TODO: Implementar metodo para deal with Block statements. Tiene que return bool
                valid = blockStatementCheck(currentKeyword);
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
     * @param keyword la keyword de el bloque de instruccines que se esta evaluando
     * @param tag el tag de la keyword que se esta evaluando. En este caso tiene que ser uno de los que esta definido en BASIC_COMMANDS
     * @return TRUE si el basic command sigue la sintaxis indicada, False de lo contrario
     */
    public static boolean basicComandCheck(Keyword keyword, Tag tag){
        boolean valid = false;
        //  public static Tag[] BASIC_COMMANDS = new Tag[]{Tag.DROP, Tag.WALK, Tag.ROTATE, Tag.LOOK, Tag.DROP, Tag.FREE, Tag.PICK, Tag.GRAB, Tag.WALK_TO, Tag.NOP};

        //Saca un arreglo de la version limpia del String
        String[] strArray = keyword.getLexeme().split(" ");
        //Me saca el string del comando
        String strCommand = strArray[0];

        //inicializa arreglo de parametros
        ArrayList<String> params = new ArrayList<>();

        //nuemro de argumentos
        int numArgs = 0;


        //Saca los argumentos
        if(strArray.length > 1){
            for(int i = 1 ; i < strArray.length; i++){
                params.add(strArray[i]);
            }

            numArgs = params.size();
        }


        switch (tag) {
            case WALK:
                valid = checkWalk(params);
                break;
            case ROTATE:
                valid = checkRotate(params);
                break;
            case LOOK:
                valid = checkLook(params);
                break;
            case DROP:
                valid = checkDrop( params) ;
                break;
            case FREE:
                valid = checkFree( params);
                break;
            case PICK:
                valid = checkPick( params);
                break;
            case GRAB:
                valid = checkGrab(params);
                break;
            case WALK_TO:
                valid = checkWalkTo(params) ;
                break;
            case NOP:
                valid = (numArgs == 0)? false: checkNOP(keyword);
                break;
        }
        return valid;

    }


    public static boolean checkWalk(ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 1){
            //Verifica que el parametro sea un int
            valid = strIsInt(params.get(0));
            if(!valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue = userDefinedVariable.get(params.get(0));
                if(varValue != null){
                    valid = strIsInt(varValue);
                }
            }
        }

        return valid;
    }

    public static boolean checkRotate(ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 1){
            //Verifica que el parametro sea un int
            valid = strIsRelDir(params.get(0));
            if(!valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue = userDefinedVariable.get(params.get(0));
                if(varValue != null){
                    valid = strIsRelDir(varValue);
                }
            }
        }
        return valid;
    }

    public static boolean checkLook(ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 1){
            //Verifica que el parametro sea un int
            valid = strIsCardDir(params.get(0));
            if(!valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue = userDefinedVariable.get(params.get(0));
                if(varValue != null){
                    valid = strIsCardDir(varValue);
                }
            }
        }
        return valid;
    }

    public static boolean checkDrop(ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 1){
            //Verifica que el parametro sea un int
            valid = strIsInt(params.get(0));
            if(!valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue = userDefinedVariable.get(params.get(0));
                if(varValue != null){
                    valid = strIsInt(varValue);
                }
            }
        }
        return valid;
    }

    public static boolean checkFree( ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 1){
            //Verifica que el parametro sea un int
            valid = strIsInt(params.get(0));
            if(!valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue = userDefinedVariable.get(params.get(0));
                if(varValue != null){
                    valid = strIsInt(varValue);
                }
            }
        }
        return valid;
    }

    public static boolean checkPick(ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 1){
            //Verifica que el parametro sea un int
            valid = strIsInt(params.get(0));
            if(!valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue = userDefinedVariable.get(params.get(0));
                if(varValue != null){
                    valid = strIsInt(varValue);
                }
            }
        }
        return valid;
    }

    public static boolean checkGrab( ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 1){
            //Verifica que el parametro sea un int
            valid = strIsInt(params.get(0));
            if(!valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue = userDefinedVariable.get(params.get(0));
                if(varValue != null){
                    valid = strIsInt(varValue);
                }
            }
        }
        return valid;
    }

    public static boolean checkWalkTo( ArrayList<String> params){
        boolean valid = false;
        if(params.size() == 2){
            //Verifica que el parametro sea un int
            boolean param1Valid = false;
            boolean param2Valid = false;

            //Revisa si primer parametro es un int o no
            if(strIsInt(params.get(0))){
                param1Valid =true;
            }
            //Si no es un int hace un ultimo chequeo si es alguna variable definida y si es un int
            if(!param1Valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue1 = userDefinedVariable.get(params.get(0));
                if(varValue1 != null){
                    param1Valid = strIsInt(varValue1);
                }
            }

            //Revisa si el segundo parametro es una direccion cardinal o no
            if(strIsCardDir(params.get(1))){
                param2Valid = true;
            }
            //Si no es un int hace un ultimo chequeo si es alguna variable definida y si es una direccion cardinal
            if(!param2Valid){
                //Revisar las variables definidas si hay alguna variable con el nombre del parametro
                String varValue2 = userDefinedVariable.get(params.get(1));
                if(varValue2 != null){
                    param1Valid = strIsCardDir(varValue2);
                }
            }

            //Si ambos parametros son validos entonces se valida el basic command
            if(param1Valid && param2Valid){
                valid = true;
            }
        }
        return valid;
    }

    /**
     * Revisa que se siga la sintaxis del comando NOP
     * @param keyword la keyword del comando
     * @return True si es valido, False de lo contrario
     */
    public static boolean checkNOP(Keyword keyword){
        //Solo tengo que revisar que sea NOP el lexeme
        return keyword.getLexeme().equals("NOP");
    }
    //------------------------------------------------------------------------------------------------------------------
    // CONDITIONAL IF STATEMENT METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean ifStatementCheck(Keyword keyword){
        boolean valid = false;


        //Expected Structure : (if (cond) (command1) (command2))
        String cleanedCommand = keyword.getLexeme();
        //Saca los componentes
        ArrayList<String> components = getComponents(cleanedCommand);

        if(components.size() == 3){
            valid = (conditionalCheck(components.get(0)) && (commandCheck(components.get(1)) && commandCheck(components.get(2))));
        }

        return valid;


    }

    public static boolean conditionalCheck(String conditionStr){
        boolean valid = false;

        if(containsConditional(conditionStr)){
            String[] componentsConditional = conditionStr.split(" ");
            String condition = componentsConditional[0];


            switch (condition) {
                case "blocked?":
                    valid = checkBlocked(componentsConditional);
                    break;
                case "facing?":
                    valid = checkFacing(componentsConditional);
                    break;
                case "can":
                    valid = checkCan(componentsConditional);
                    break;
                case  "not":
                    valid = checkNot(componentsConditional, conditionStr);
                    break;
            }
        }

        return valid;


    }

    public static boolean checkBlocked(String[] componentsConditional){
        //Si se tiene mas de un elemento en el arreglo significa que hayparametros por lo tanto no esta bien definido blocked
        return componentsConditional.length <= 1;
    }

    public static boolean checkFacing(String[] componentsConditional){
        boolean valid = false;
        if(componentsConditional.length == 2){
            valid = strIsCardDir(componentsConditional[1]);
        }
        return valid;
    }
    public static boolean checkCan(String[] componentsConditional){
        boolean valid = false;
        if(componentsConditional.length == 2){
            valid = strIsInstruction(componentsConditional[1]);
        }
        return valid;
    }

    //TODO: REVISAR ESTO CON MUCHO CUIDADO DURANTE TESTING
    //Esto es recursivo. Cuando hay una cadena de nots esto se sigue intentando resolver rcursivamente hasta que la conduicion sea un con
    public static boolean checkNot(String[] componentsConditional, String str){
        boolean valid = false;
            if(componentsConditional.length == 2){
                //El " " extra es por que substring es non inclusive del ultimo indice entonces si no estaria comiendose el ultimo parentesis
                String clean = cleanUpOuterParenthesis(str,1) + " ";
                clean = clean.substring(clean.indexOf("("),clean.lastIndexOf(")") + 1);
                valid = conditionalCheck(clean);
            }
        return valid;
    }




    public static boolean commandCheck(String commandStr){
        boolean valid = false;
        //3 casos : basic command , defined function, block commands o if statements

        String strWithParenthesis = "(" + commandStr + ")";
        Keyword kwdCommand = Lexer.processCodeBlock(strWithParenthesis);

        //BASIC COMMAND CHECK
        if(containsBasicCommand(commandStr)){
            //Delega el chequeo a basicCommandCheck
            valid = basicComandCheck(kwdCommand,kwdCommand.getTag());
        }
        //IF STATEMENTS
        if(commandStr.startsWith("if")){
            //Delega chequeo de nuevo a ifStatement (queda recursivo)
            valid = ifStatementCheck(kwdCommand);
        }
        //BLOCK COMMAND
        if(commandStr.startsWith("block")){
            //Delega chequeo a block statement check
            valid = blockStatementCheck(kwdCommand);
        }
        //USER FUNCTIN
        if(containsUserFunction(commandStr)){
            //TODO HACER LO DE USER DEFINED FUNCTIONS
            //valid = userDefinedFunctionCheck(kwdCommand);
        }
        return valid;
    }


    //------------------------------------------------------------------------------------------------------------------
    // BLOCK METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean  blockStatementCheck(Keyword keyword){
        boolean valid = true;
        int firstParenthesisIndex = keyword.getLexeme().indexOf("(");
        int lastParenthesisIndex = keyword.getLexeme().lastIndexOf(")");
        String str = keyword.getLexeme() + " ";
        String commandsStr = str.substring(firstParenthesisIndex,lastParenthesisIndex + 1);

        //Saca el arreglo de commands
        ArrayList<String> commands = getComponents(commandsStr);

        //Le quita los parentesis a commands dentro del arraylist
        for(String command: commands){
            command = cleanUpOuterParenthesis(command,1);
        }

        //Itera a travez de commands para ir revisando la validez de cada uno individualmente
        //Si uno de los comands es falso entonces se sale del for y se declara como invalido todo el block statement
        for(int i = 0 ; i < commands.size() && valid; i++){
            if(!commandCheck(commands.get(i))){
                valid =false;
            }
        }

        //Si para el return valid sigue siendo true significa que el bloque paso todas las pruebas nesecarias 
        return valid;

    }

    //------------------------------------------------------------------------------------------------------------------
    // DEFINE METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean defineFunctionCheck(String instructionBlock,Keyword keyword, Tag tag){
        //TODO
        return false;
    }

    public static boolean defineVariableCheck(String instructionBlock,Keyword keyword, Tag tag){
        //TODO
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    // USER FUNCTION CALL METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean userDefinedFunctionCheck(Keyword keyword){
        //TODO
        return false;
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


    /**
     * Revisa si un string tien valores numericos como contenido
     * @param str
     * @return
     */
    public static boolean strIsInt(String str) {
        for (int a = 0; a < str.length(); a++) {
            if (!Character.isDigit(str.charAt(a))) {
                return false;
            }
        }
        return true;
    }

    public static boolean strIsRelDir(String str) {

        for (String relativeDirections : STR_RELATIVE_DIRS) {
            if ((str.equals(relativeDirections))){
                return true;
            }
        }
        return false;
    }


    public static boolean strIsCardDir(String str) {

        for (String relativeDirections : STR_CARDINAL_DIRS) {
            if ((str.equals(relativeDirections))){
                return true;
            }
        }
        return false;
    }


    public static boolean strIsInstruction(String str) {

        for (String instruction : STR_ROBOT_INSTRUCTIONS) {
            if ((str.equals(instruction))){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getComponentBlocks(String str){
        char leftParen = '(';
        char rightParen = ')';

        int initialIndexCurrComp = 0;
        int finalIndexCurrBlock = str.indexOf(')') ;

        ArrayList<String> setOfComponents = new ArrayList<>();
        Stack<Character> stack = new Stack<Character>();

        for (int i = 0; i < str.length(); i++)
        {
            String currChar = str.charAt(i) + "";
            if (str.charAt(i) == leftParen) {
                stack.push(leftParen);
            }
            else if (str.charAt(i) == rightParen) {
                stack.pop();
            }
            if(stack.isEmpty()){
                String currBlock = str.substring(initialIndexCurrComp, (i + 1));
                initialIndexCurrComp = i + 1;
                setOfComponents.add(currBlock);
            }
        }

        return setOfComponents;

    }


    public static ArrayList<String> getComponents(String instructionBlock){
        //Saca el indice del primer parentesis
        int indexOfFirstParenthesis = instructionBlock.indexOf('(');
        int indexOfLastParenthesis = instructionBlock.lastIndexOf(')');
        instructionBlock = instructionBlock + " ";// Para tratar con el problema de substring

        //Me saca el contenido de los componentes (deberia ser (cond) (cmd) (cmd)
        String subStringOfComponents = instructionBlock.substring(indexOfFirstParenthesis, indexOfLastParenthesis + 1
        );
        ArrayList<String> components = getComponentBlocks(subStringOfComponents);

        //Limpia los parentesis de los componentes
        for(String comp : components){
            // El regex de [()] va a hace match con cualquiera ( o )
            comp = comp.replaceAll("[()]", "");
        }
        return  components;
    }

   public static boolean containsConditional(String str){
       for(String conditional: CONDITIONAL_STATEMENTS){
           if(str.startsWith(conditional)){
               return true;
           }
       }
       return false;
   }

    public static boolean containsBasicCommand(String str){
        for(String comand: basicCmd){
            if(str.startsWith(comand)){
                return true;
            }
        }
        return false;
    }

    public static boolean containsUserFunction(String str){
        for(String funct: userDefinedFunctions){
            if(str.startsWith(funct)){
                return true;
            }
        }
        return false;
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
}
