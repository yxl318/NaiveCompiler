package stone;

public abstract class Token {
    public static final Token EOF=new Token(-1) {};//end of file
    public static final String EOL="\\n";//end of line
    private int lineNumber;//lineNumber of Token
    public Token(int line) {//Constructor
        lineNumber = line;
    }
    public int getLineNumber() {//for safety
        return lineNumber;
    }
    public boolean isIdentifier(){//waiting for inheriting
        return false;
    }
    public boolean isNumber(){//waiting for inheriting
        return false;
    }
    public boolean isString(){//waiting for inheriting
        return false;
    }
    public int getNumber(){//if Token is about number,return this number,else return error.
        throw new StoneException("not number token");
    }
    public String getText(){return "";}//return the content of token
}
