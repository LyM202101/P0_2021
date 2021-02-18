package Tokens;

public class Function extends Token{
    String functionName;

    public Function(String functionName) {
        super(Tag.USER_FUNCTION);
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
