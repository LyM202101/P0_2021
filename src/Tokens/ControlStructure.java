package Tokens;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControlStructure extends Token {
    // A conditional command: (if cond command1 command2) where cond is a condition and command1 and command 2 are commands

    /*public static final int BASIC_COMMAND = 0;
    public static final int BLOCK_OF_COMMANDS = 1;
    public static final int DEFINED_FUNCTION = 2;*/

    public enum CommandType{
        BASIC_COMMAND,
        BLOCK_OF_COMMANDS,
        DEFINED_FUNCTION
    }

    /*public static final int BLOCKED = 3;
    public static final int FACING = 4;
    public static final int CAN = 5;
    public static final int NOT = 6;*/

    public enum Conditional{
        BLOCKED,
        FACING,
        CAN,
        NOT
    }

    /**
     * Un control structure como  un if statement tiene tres partes. La condicion que en este caso es 1 de 4 enums y dos comandos posibles
     * En este caso ambos commands seran strings y seran reconocidos y entendidos por Interpreter directamente por lo tanto se van a interpretar casi como argumentos(mirar BasicCommandKeyword para referencia)
     * esto es debido a que cada command puede ser o un comando simple, un bloque de comandos, o hasta una funcion predefinida por el usuario
     *En este caso conditional tambien sera un string y su verificacions sera hecha en el interprete directamente, esas partes no seran tokenizadas per se
     *
     */
    String conditional;
    Conditional condType;
    String command1;
    CommandType com1Type;
    String command2;
    CommandType com2Type;

    //Esto va a implicar un monton de ifs en interpeter


    /**
     * Representa un token de una estructura de control, un if statement
     * @param conditional el condicional usado en el if statement
     * @param command1 el primer commando en el id statement
     * @param command2 el segundo command en el if statement
     */
    public ControlStructure(String conditional, Conditional condType, String command1 , CommandType com1Type, String command2, CommandType com2Type){
        super(Tag.IF); //EL tag de este token siempre tiene que ser un IF
        this.conditional = conditional;
        this.condType = condType;
        this.command1 = command1;
        this.com1Type = com1Type;
        this.command2 = command2;
        this.com2Type = com2Type;
    }

    //Si los command type llegana ser null automaticamente ya se sabe que la estructura de control es invalida, por lo tanto el restro del programa tambien
    public String getConditional(){
        return conditional;
    }

    public Conditional getConditionalType(){
        return condType;
    }

    public String getCommand1(){
        return command1;
    }

    public CommandType getCommand1Type(){
        return com1Type;
    }

    public String getCommand2(){
        return command2;
    }

    public CommandType getCommand2Type(){
        return com2Type;
    }

    public static Conditional findConditional(String condition){
        Conditional condType = null;
        switch (condition){
            case "blocked?":
                condType = Conditional.BLOCKED;
                break;
            case "facing?" :
                condType = Conditional.FACING;
                break;
            case "can":
                condType = Conditional.CAN;
                break;
            case "not":
                condType = Conditional.NOT;
                break;
        }
        return condType;
    }

    public static CommandType findCommandType(String command){
        CommandType commandType = null;
        boolean isBasicCmd = false;

        //Arreglo con todos los metodos que son basic commands
        String[] basicCmdArr = new String[]{"walk", "rotate", "look", "drop", "free", "pick", "grab", " walkTo", "NOP"};
        List<String> basicCmdList = new ArrayList<>(Arrays.asList(basicCmdArr));

        for(String cmd: basicCmdArr){
            if(command.startsWith(cmd)){
                isBasicCmd = true;
            }
        }

        if(isBasicCmd){
            commandType = CommandType.BASIC_COMMAND;
        }
        else if(command.startsWith("block")){
            commandType = CommandType.BLOCK_OF_COMMANDS;
        }
        else if(command.startsWith("defined")){
            commandType = CommandType.DEFINED_FUNCTION;
        }

        return commandType;
    }
}
