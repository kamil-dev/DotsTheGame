/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.model.dataStructures;

import java.io.Serializable;

public class DotNode implements Serializable, Cloneable {
    public Dot d;
    public DotNode next;

    public DotNode(Dot d, DotNode next){
        this.d = d;
        this.next = next;
    }

    @Override
    public boolean equals(Object obj) {         // compares d and next.d
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        DotNode dn2 = (DotNode) obj;
        if (dn2 == null)
            return false;
        return this.d.equals(dn2.d);
    }

    public int getY(){
        return d.getY();
    }
    public int getX(){
        return d.getX();
    }

    @Override
    public int hashCode() {
        return d.hashCode();
    }

    @Override
    public String toString() {
        return "(" + d.getX() + ", " + d.getY() + ") ";
    }
}
