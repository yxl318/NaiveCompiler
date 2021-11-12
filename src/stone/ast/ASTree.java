package stone.ast;

import java.util.Iterator;

public abstract class ASTree implements Iterable<ASTree> {
    public abstract ASTree child(int i); //return ith child
    public abstract int numChildren();  //return the number of children
    public abstract Iterator<ASTree> children();   //return an Iterator for visiting children
    public abstract String location();
    public Iterator<ASTree> iterator(){return children();}
}
