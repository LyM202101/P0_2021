package Tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase es para las funciones definidas por el usuario
 */
public class UserFunction extends Token {

    //Estructura : (define name (params)command)

    /**
     * El nombre de la funcion dado por el usuario
     */
    String name;
    /**
     * El commando que invoca la funcion definida por el usuario
     */
    String command;
    /**
     * El tipo de comando , un enum, que llama la funcion
     */
    CommandType comType;
    /**
     * Los parametros de una funcion
     */
    String[] params;



    /**
     * El constructor para definir una funcion definida por el usuario
     * @param name el nombre de la funcion
     * @param command el commando que se llama en la funcion
     * @param params un numero variable (0..*) de parametros que puede recibir la funcion que esta siendo llamada
     */
    public UserFunction( String name, String command, CommandType commandType, String... params) {
        super(Tag.USER_FUNCTION);
        this.name = name;
        this.command = command;
        this.params = params;
        this.comType = commandType;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public CommandType getComType() {
        return comType;
    }

    public String[] getParams() {
        return params;
    }


    /**
     * Encuentra el tipo de comando que se esta usando dentro de los bloques de comandos enun if statement
     * @param command el comando
     * @return Un enum con el tipo de comando que se esta usando. Si no es ningun tipo de los enums entonces sera null
     */
    public static CommandType findCommandType(String command){
        CommandType commandType = null;
        boolean isBasicCmd = false;

        //Arreglo con todos los metodos que son basic commands
        String[] basicCmdArr = new String[]{"walk", "rotate", "look", "drop", "free", "pick", "grab", " walkTo", "NOP"};
        //List<String> basicCmdList = new ArrayList<>(Arrays.asList(basicCmdArr));

        for(String cmd: basicCmdArr){
            if(command.startsWith(cmd)){
                isBasicCmd = true;
            }
        }


        //Si el tipo de comando es true significa se encontro en la lista de comandos basicos entonces se asigna ahi
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
