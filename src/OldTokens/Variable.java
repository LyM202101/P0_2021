package OldTokens;

public class Variable extends Token {
    String varName;


    public Variable(String varName) {
        super(Tag.VAR);
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }
}
