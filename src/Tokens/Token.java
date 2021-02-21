package Tokens;

import HMMTOKEN.Tag;

abstract class Token {
    public final Tag tag;
    public Token(Tag tag){
        this.tag = tag;
    }
}
