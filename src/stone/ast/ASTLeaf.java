package stone.ast;

import stone.Token;

import java.util.ArrayList;
import java.util.Iterator;

public class ASTLeaf extends ASTree{
    private static ArrayList<ASTree> empty=new ArrayList<ASTree>();
    protected Token token;
    @Override
    public ASTree child(int i) {//Leaf does not have any children!
        throw new IndexOutOfBoundsException();
    }
    @Override
    public int numChildren() {
        return 0;
    }
    @Override
    public Iterator<ASTree> children() {
        return empty.iterator();
    }
    @Override
    public String location() {
        return "at line "+token.getLineNumber();
    }
    public ASTLeaf(Token t){
        token=t;
    }
    public String toString(){
        return token.getText();
    }
    public Token token(){
        return token;
    }
}
