package Tokens;

import java.util.Arrays;

/**
 * Esta clase es para las variables definidas por el usuario
 */
public class UserVariable extends Token{
    //Estructura : (define name value)

    //Value := NUM || DIRECTION

    public enum VariableType{
        NUM,
        DIRECTION
    }

    /**
     * El nombre de la variable
     */
    String variableName;

    /**
     * El tipo de la variable
     */
    VariableType varType;

    /**
     * El contenido de la variable
     */
    String varValue;



    //El responsable de ver que tipo de variable es debe ser el Lexer


    /**
     * Es el constructor de un token de User variable, lo cual define una variable definida por el usuario que
     * puede ser un numero o una direccion (cardinal o relativa)
     * @param variableName El nombre de la variable definida por el usurario
     * @param varValue el valor de la variable
     * @param varType el tipo de la variable, NUM o DIR
     */
    public UserVariable(String variableName, String varValue, VariableType varType){
        super(Tag.VAR);
        this.variableName = variableName;
        this.varType = varType;
        this.varValue = varValue;
    }

    /**
     * Retorna el nombre de una variable
     * @return String con el variable name
     */
    public String getVariableName(){
        return variableName;
    }

    /**
     * Retorna el tipo de variable
     * @return Un enum que es o un Num o una direccion
     */
    public VariableType getVarType() {
        return varType;
    }


    //Prefiero mantenerlo en un string porque si no further processing creo que solo seria un problema

    /**
     * Retorna el valor de la variable en el formato de un string
     * @return Un string con el contenido de la variable
     */
    public String getVarValue() {
        return varValue;
    }


    /**
     * Encuentra el tipo de variable que se esta definiendo si esta es NUM o DIR. Si no es de estos tipos retornara un Null
     * @param varValue el string que contiene el valor de la variable
     * @return VariableType de DIRECTION, NUM o null si la variable no es ninguno de los tipos anteriores
     */
    public static VariableType findVariableType(String varValue){
        VariableType variableType = null;

        if(varValue.matches("[0-9]+")){
            variableType = VariableType.NUM;
        }
        else if(Arrays.asList(Direction.cardinalDirections).contains(varValue)||Arrays.asList(Direction.relativeDirections).contains(varValue)){
            variableType = VariableType.DIRECTION;
        }

        return  variableType;
    }

}
