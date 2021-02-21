package Tokens;

import HMMTOKEN.Tag;

abstract class Token {
    public final HMMTOKEN.Tag tag;
    public Token(Tag tag){
        this.tag = tag;
    }
}
