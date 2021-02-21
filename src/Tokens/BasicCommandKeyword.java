package Tokens;

public class BasicCommandKeyword extends Token{

    //Comandos Basicos: walk , rotate, look , drop, free, pick , grab, walkTo, NOP
    //NOTA: Otras cosas interpretados como comandos tal como block commands, defined function, y conditional command se tratan con otras clases especiales a tokens que se ajustan a sus caracteristicas particulares

    /**
     * El numero de Argumentos que tiene un commando
     */
    public static final int EMPTY = 0; //Como (NOP)
    public static final int UNARY = 1; //Como (walk n)
    public static final int BINARY = 2; //Como (walkTo n O)


    int functionType;
    String lexeme;
    Token[] arguments;


    /**
     * Construye un basic command keyword la cual decribe un comando basico
     * @param lexeme un es la cadena del lenguaje que represetna el comando ej "walk"
     * @param tag el tag que representa el tipo de token
     * @param args los argumentos del basic command, puede no tener, ser binario o unario
     */
    public BasicCommandKeyword(String lexeme, Tag tag, Token... args){
        super(tag);
        this.lexeme = lexeme;
        this.functionType = args.length;
        if(functionType == UNARY || functionType == BINARY){
            arguments = args;
        }
        else{
            arguments = null;
        }
    }


    /**
     * Retorna el tipo de funcion, esto implica que tantos argumentos recibe
     * @return Un int que simboliza el numero de argumentos que tiene un comando (0, 1  o 2)
     */
    public int getFunctionType() {
        return functionType;
    }

    /**
     * Retorna el tipo de lexeme del comando
     * @return Un string con el nombre del lexeme
     */
    public String getLexeme() {
        return lexeme;
    }


    /**
     * Retorna los argumentosque tiene un command bascio
     * @return Un arreglo con los argumentos de un comando basico
     */
    public Token[] getArguments() {
        return arguments;
    }

    public static Tag findCmdTag(String basicCommand){
        Tag cmdTag = null;
        //Comandos Basicos: walk , rotate, look , drop, free, pick , grab, walkTo, NOP

        switch(basicCommand){
            case "walk":
                cmdTag = Tag.WALK;
                break;
            case "rotate":
                cmdTag = Tag.ROTATE ;
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
                cmdTag = Tag.GRAB;
                break;
            case "walkTo":
                cmdTag = Tag.WALK_TO;
                break;
            case "NOP":
                cmdTag = Tag.NOP;
                break;
        }
        return cmdTag;
    }

}
