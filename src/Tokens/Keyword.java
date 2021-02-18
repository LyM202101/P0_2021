package Tokens;

import java.util.ArrayList;
import java.util.List;

public class Keyword extends Token{

    /**
     * El numero de parametros que tiene el comandOS
     */

    //TODO: El numero de comandos va a ser un problema para cosas como block, defined functions, conditionals(if comands)

    //This is mainly para los comandos normales con un numero conocido de parametros
    public static final int EMPTY = 0;// (NOP), ((blocked?)),
    public static final int UNARY = 1; // (walk n) , (rotate D) , (look O) , (drop n), (free n) , (pick n), ((facing? O)),((can a), (not cond)
    public static final int BINARY = 2; // (walkTo n O),  (define name value)
    public static final int TERNARY = 3; // (if cond comand1 command2)
    public static final int NARY = 4; // (block commands), ((f a1 ...an))

    //TODO: How on earth do i deal with function definition
    //(define name (params)command)
    //How do i deal with multiple params and multiple stacked comands
    //How do i deal with saving the commands



    //(define name (params)command)
    //(define name value)
    // **conditions**


    static List<String> userDefinedFunctions = new ArrayList<>();

    int functionType;
    String lexeme;
    Token[] arguments;



    public Keyword(String lexeme, Tag tag, Token... args) {
        super(tag);
        this.lexeme = lexeme;
        this.functionType = args.length;

        if (functionType == UNARY || functionType == BINARY || functionType == TERNARY || functionType == NARY) {
            arguments = args;
        } else {
            arguments = null;
        }

    }

    public int getFunctionType() {
        return functionType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Token[] getArguments() {
        return arguments;
    }

    public static Tag findCmdTag(String command){
        Tag cmdTag = null;

        switch(command){
            case "walk":
                cmdTag = Tag.WALK;
                break;
            case "rotate":
                cmdTag = Tag.ROTATE;
                break;
            case "look":
                cmdTag = Tag.LOOK ;
                break;
            case "drop":
                cmdTag = Tag.DROP ;
                break;
            case "free":
                cmdTag = Tag.FREE ;
                break;
            case "pick":
                cmdTag = Tag.PICK ;
                break;
            case "grab":
                cmdTag = Tag.GRAB ;
                break;
            case "walkTo":
                cmdTag = Tag.WALK_TO ;
                break;
            case "NOP":
                cmdTag = Tag.NOP ;
                break;
            case "block":
                cmdTag = Tag.BLOCK ;
                break;
            case "if":
                cmdTag = Tag.IF ;
                break;
            case "define":
                cmdTag = Tag.DEFINE ;
                break;
            case "blocked?":
                cmdTag = Tag.BLOCKED ;
                break;
            case "facing?":
                cmdTag = Tag.FACING ;
                break;
            case "can":
                cmdTag = Tag.CAN ;
                break;
            case "not":
                cmdTag = Tag.NOT ;
                break;
        }

        //TODO: Quitar esto en el caso que se decida reconocer las user defined functions de otra forma
        //Si para esta etapa el cmdTag es null esta la posibilidad que ese comando no este definidio o que sea una user defined function
        if(cmdTag == null){

            //Busca entre la lista de funciones que ya han sido definidas por el usuario
            for(String userFunction : userDefinedFunctions){
               if(userFunction.equals(command)){
                   cmdTag = Tag.USER_FUNCTION;
               }
            }

            //Esto esta destinado para revvisar si depronto es una funcion que esta siendo definida
            if(cmdTag == null){
                //TODO: Check y add una nueva funcion definida a la lista de funcion
            }
        }
        return cmdTag;
    }


    public static void addUsrDefinedFunction(String newFunct){
        userDefinedFunctions.add(newFunct);
    }
}
