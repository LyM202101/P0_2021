package Tokens;

public class Keyword extends Token{

    /**
     * El numero de parametros que tiene el comando
     */
    public static final int EMPTY = 0;
    public static final int UNARY = 1;
    public static final int BINARY = 2;

    int functionType;
    String lexeme;
    Token[] arguments;

    public Keyword(String lexeme, Tag tag, Token... args) {
        super(tag);
        this.lexeme = lexeme;
        this.functionType = args.length;
        if (functionType == UNARY || functionType == BINARY) {
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
}
