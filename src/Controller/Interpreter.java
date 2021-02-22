package Controller;

import HelperClases.TripleTuple;
import HelperClases.UserFunction;
import Tokens.Keyword;
import Tokens.Tag;

import java.util.*;

/**
 * El lexer es quien recibe los comandos y es responsable de verificar si siguen la sintaxis apropiada de cada uno definido por el lenguje
 */
public class Interpreter {
    /**
     * En este hashmap se guardan las funciones definidas por el usuario. Tiene como llave el nombre de la funcion y como valor un objeto de UserFunction
     */
    static final HashMap<String, UserFunction> userDefinedFunctions = new HashMap<>();

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
                //Delegado a block statement check
                valid = blockStatementCheck(currentKeyword);
            }

            //DEFINE FUNCTIONS AND VARIABLES
            else if(currKeywordTag == Tag.DEFINE){

                //DEFINE FUNCTION
                if(countParenthesis(currInstructionBlock) > 2){
                    valid = defineFunctionCheck(currInstructionBlock,currentKeyword);
                }
                //DEFINE VARIABLE
                else{
                    valid = defineVariableCheck(currentKeyword);
                }
            }

            //USER FUNCTIONS

           else{
                //REVISAR SI EL NOMBRE ES EL DE UNA FUNCION CONOCIDA
                valid = userDefinedFunctionCheck(currentKeyword);
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
            case WALK -> valid = checkWalk(params);
            case ROTATE -> valid = checkRotate(params);
            case LOOK -> valid = checkLook(params);
            case DROP -> valid = checkDrop(params);
            case FREE -> valid = checkFree(params);
            case PICK -> valid = checkPick(params);
            case GRAB -> valid = checkGrab(params);
            case WALK_TO -> valid = checkWalkTo(params);
            case NOP -> valid = numArgs == 0 && checkNOP(keyword);
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
            //condition =condition.trim().substring(1,condition.lastIndexOf(")"));

            switch (condition) {
                case "blocked?" -> valid = checkBlocked(componentsConditional);
                case "facing?" -> valid = checkFacing(componentsConditional);
                case "can" -> valid = checkCan(componentsConditional);
                case "not" -> valid = checkNot(componentsConditional);
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
    public static boolean checkNot(String[] componentsConditional){
        boolean valid = false;
            if(componentsConditional.length == 2){
                String clean = cleanUpOuterParenthesis(componentsConditional[1],1) + " ";
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
            //Delega checqueo a userDefinedFunction
            valid = userDefinedFunctionCheck(kwdCommand);
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


    public static boolean defineFunctionCheck(String instructionBlock,Keyword keyword){
        boolean valid = false;

        //define name (param) (cmd)

        //Saca el contenido de definir la funcion
        String defFun = keyword.getLexeme();

        //Saca nombre, params , comandos que definen a una funcion
        TripleTuple<String,String,String> partition = processFunctionDefinition(defFun);


        //Saca la informacion importante para defiir la funcion
        String name = partition.getLeft();
        String params = partition.getCenter();
        String instructions = partition.getRight();

        int numArgs;

        //Revisar que esta correctamente definido
        if(name.matches("[a-zA-Z]+")){
            if(userDefinedFunctions.get(name) == null) {
                instructionBlock = instructionBlock.trim();
                if(commandCheck(instructionBlock)){

                    if(params.equals("()")){
                        numArgs = 0;
                    }
                    else{
                        params = cleanUpOuterParenthesis(params.trim(),1);
                        String[] paramsIndividuals = params.trim().replaceAll("[ +]", " ").split(" ");
                        numArgs = paramsIndividuals.length;
                    }

                    //No me voy a molestar en revisar lo de los parametros correctos con el tipo de comando\


                    //AQUI SE AÑADE UNA NUEVA FUNCION
                    UserFunction usrFunct = new UserFunction(name, numArgs, instructions);
                    userDefinedFunctions.put(name,usrFunct);
                    valid = true;


                }

            }

        }

        return valid;
    }


    public static TripleTuple<String,String,String> processFunctionDefinition(String functionDef){

        //Quita secuencias de whitespaces antes y despues del string y hace que todos los espacios solo sean " "
        String cleanedFunctionDef = functionDef.trim().replaceAll(" +", " ");

        //Saca indice del primer (
        int firstRightParenthesis = cleanedFunctionDef.indexOf('(');

        //Saca define name
        String defAndName = cleanedFunctionDef.substring(0,firstRightParenthesis);

        //Saca (params) (command)
        String paramsAndCommand = cleanedFunctionDef.substring(firstRightParenthesis);
        ArrayList<String> paramsAndCmd = getComponentBlocks(paramsAndCommand.trim());


        //Sacar nombre, parametros y instrucciones
        String name = defAndName.split(" ")[1];
        String params = paramsAndCmd.get(0);
        String instructions = paramsAndCmd.get(1);

        return  new TripleTuple<>(name,params,instructions);


    }



    /**
     * Define una nueva variable creada por el usuario que tenga el valor de un numero o una variable previamente definida
     * @param keyword la palabara clave
     * @return True si la definicion de la variable es correcta y se añadio exitosamente al hashmap, false de lo contrario
     */
    public static boolean defineVariableCheck(Keyword keyword){
        boolean valid = false;

        String[] partitions= keyword.getLexeme().split(" ");
        //Idealmente index 0 tiene la palabra define, index 1 tiene el nombre y index 2 tiene el valor

        //Si hay 3 sub strings entonces se tiene algo similar a la estructura de define variable
        if(partitions.length == 3){
            //Double check que el primer index tenga define
            if(partitions[0].equals("define") ){
                //Sacar el nombre y el valor definidos
                String name = partitions[1];
                String value = partitions[2];

                //Se revisa si la variable es un nummero o una variable previamente definida
                if(strIsCardDir(value) || strIsInt(value) || strIsRelDir(value) || strIsInstruction(value)){
                    valid = true;
                    //Se añade la variable al hashmap de variables definidas por elusuario para que se puede usar en otras funciones posteriores
                    userDefinedVariable.put(name,value);
                }
            }
        }

        return valid;
    }

    //------------------------------------------------------------------------------------------------------------------
    // USER FUNCTION CALL METHODS
    //------------------------------------------------------------------------------------------------------------------

    public static boolean userDefinedFunctionCheck(Keyword keyword){
        boolean valid = false;
        String functionCall = keyword.getLexeme().trim().replaceAll(" +", " ");
        String[] partition = functionCall.split(" ");

        String functionName = partition[0];
        String[] parameters = Arrays.copyOfRange(partition, 1, (partition.length));
        int paramNum = parameters.length;


        for(String definedFunctions : userDefinedFunctions.keySet()){
            //Check to see if any function mateches name
            if(functionCall.startsWith(definedFunctions)){
                //Check parameters and match
                if(paramNum == userDefinedFunctions.get(definedFunctions).getNumOfParams()){
                    valid = true;
                }
            }
        }

        return valid;
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
            if(stack.isEmpty() && !currChar.equals(" ")){
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

        ArrayList<String> cleanedArr = new ArrayList<>();

        //Limpia los parentesis de los componentes
        for(String comp : components){
            // El regex de [()] va a hace match con cualquiera ( o )

            comp = comp.trim();
            int ins = comp.lastIndexOf(")");
            comp = comp.substring(1,ins);
            comp =comp.trim();
            cleanedArr.add(comp);
        }
        return  cleanedArr;
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
        for(String funct: userDefinedFunctions.keySet()){
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
