package stone;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static String regexPat
            ="\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"     //Space、Comment、String、Number
            +"|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";     //Id
    private Pattern pattern=Pattern.compile(regexPat);      //Process Regular Expressions
    private ArrayList<Token> queue=new ArrayList<Token>();  //Store tokens temporarily
    private boolean hasMore;    //if this is the end of text, hasMore is false;
    private LineNumberReader reader;

    public Lexer(Reader r){ //constructor
        hasMore=true;
        reader=new LineNumberReader(r);
    }
    public Token read() throws ParseException{
        if(fillQueue(0)){
            return queue.remove(0);
        }else{
            return Token.EOF;
        }
    }
    public Token peek(int i) throws ParseException{//return ith token which is helpful for parser
        if(fillQueue(i)){
            return queue.get(i);
        }else{
            return Token.EOF;
        }
    }
    private boolean fillQueue(int i) throws ParseException{//check if ith token exists in queue, if not, read more
        while(i>=queue.size()){//queue is not enough,need to read more!
            if(hasMore){
                readline();
            }else{
                return false;
            }
        }
        return true;
    }

    protected void readline() throws ParseException{
        String line;
        try {
            line =reader.readLine();
        } catch (IOException e) {
            throw new ParseException(e);
        }
        if(line==null){//if this is the end of the file
            hasMore=false;
            return;
        }
        int lineNo= reader.getLineNumber();
        Matcher matcher=pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos=0;
        int endPos=line.length();
        while(pos<endPos){
            matcher.region(pos,endPos);//set the beginning and ending.
            if(matcher.lookingAt()){//RE matching from pos to endPos
                addToken(lineNo,matcher);//add new token to the queue
                pos=matcher.end();
            }else{
                throw new ParseException("bad token at line "+ lineNo);
            }
        }
        queue.add(new IdToken(lineNo,Token.EOL));
    }

    protected void addToken(int lineNo, Matcher matcher) {//add new token to the queue
        String m=matcher.group(1);
        if(m!=null) {//if not a space
            if (matcher.group(2) == null) { //if not a comment
                Token token;
                if(matcher.group(3)!=null){ //if type of token is number
                    token=new NumToken(lineNo,Integer.parseInt(m));
                }else if(matcher.group(4)!=null){//if type of token is string
                    token=new StrToken(lineNo,toStringLiteral(m));
                }else{
                    token=new IdToken(lineNo,m);//if type of token is id
                }
                queue.add(token);
            }
        }
    }

    protected String toStringLiteral(String s) {
        StringBuilder sb=new StringBuilder();
        int len=s.length()-1;
        for(int i=1;i<len;i++){
            char c=s.charAt(i);
            if(c=='\\'&&i+1<len){
                int c2=s.charAt(i+1);
                if(c2=='"'||c2=='\\') {
                    c = s.charAt(++i);
                }else if(c2=='n'){
                    ++i;
                    c='\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    protected static class NumToken extends Token{
        private int value;
        protected NumToken(int line,int v){
            super(line);
            value=v;
        }
        @Override
        public boolean isNumber() {
            return true;
        }
        @Override
        public String getText() {
            return Integer.toString(value);
        }
        @Override
        public int getNumber() {
            return value;
        }
    }
    protected static class IdToken extends Token {
        private String text;
        protected IdToken(int line,String id){
            super(line);
            text=id;
        }
        @Override
        public boolean isIdentifier() {
            return true;
        }
        @Override
        public String getText() {
            return text;
        }
    }
    protected static class StrToken extends Token{
        private String literal;
        StrToken(int line,String str){
            super(line);
            literal=str;
        }
        @Override
        public boolean isString() {
            return true;
        }
        @Override
        public String getText() {
            return literal;
        }
    }
}
