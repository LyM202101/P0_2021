package HelperClases;

import java.util.ArrayList;

public class UserFunction {
    public String command;
    public String name;
    public int numOfParams;

    public  UserFunction(String name,int numOfParams,String command){
        this.name = name;
        this.numOfParams = numOfParams;
        this.command =command;
    }

    public String getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }


    public int getNumOfParams() {
        return numOfParams;
    }
}
