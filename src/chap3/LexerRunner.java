package chap3;
import stone.*;
public class LexerRunner {
    public static void main(String[] args) throws ParseException {
        Lexer l=new Lexer(new CodeDialog());
        for(Token t;(t=l.read())!=Token.EOF;){
            String type="";
            if(t.isIdentifier()){
                type="Id";
            }else if(t.isNumber()){
                type="Num";
            }else if(t.isString()){
                type="String";
            }
            System.out.println(type+" => "+t.getText());
        }
    }
}
