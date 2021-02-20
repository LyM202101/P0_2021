package Tokens;

/**
 * Esta clase es para las variables definidas por el usuario
 */
public class UserVariable extends Token{


    //TODO: NO estoy segura de como diferenciar escribir variables o escribir funciones

    
    //El nombre de la variable
    String variableName;

    public UserVariable(String variableName){
        super(Tag.VAR);
        this.variableName = variableName;
    }

    public String getVariableName(){
        return variableName;
    }
}
