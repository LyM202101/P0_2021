package Tokens;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Keyword extends Token {

    /**
     * En este arreglo se guardan las funciones definidas por el usuario
     */
    static final ArrayList<String> userDefinedFunctions = new ArrayList<>();

    /**
     * En este arreglo se guardan las variables definidas por el usuario
     */
    static final HashMap<String, String> userDefinedVariable = new HashMap<>();


    /**
     * En este caso el lexema es el mismo que el code block pero no incluye los parentesis exteriores
     */
    String lexeme;


    /**
     * Constructor de Keyword que tiene el cleaned lexeme
     * @param lexeme el cleaned block lexeme
     * @param tag el tag que corresponde al bloque de codigo
     */
    public Keyword(String lexeme, Tokens.Tag tag) {
        super(tag);
        this.lexeme = lexeme;

    }


    public Tag getTag(){
        return this.tag;
    }
    public String getLexeme() {
        return this.lexeme;
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
        }

        //TODO: Quitar esto en el caso que se decida reconocer las user defined functions de otra forma
        if(cmdTag == null){

            if(command.equals("define")){
                cmdTag = Tag.DEFINE;
            }

            //COMO DEFAULT SE VA A DEFIIR TO DO LO NULO COMO UN POSIBLE CASO DE LLAMADO A UNA FUNCION(uno no puede llamar una variable de forma aisalda)
            else {
                cmdTag = Tag.USER_FUN;
            }
        }


        return cmdTag;
    }


    public static void addUsrDefinedFunction(String newFunct){
        userDefinedFunctions.add(newFunct);
    }
}
