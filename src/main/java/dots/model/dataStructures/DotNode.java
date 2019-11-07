/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.dots.model.dataStructures;

public class DotNode {
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
        if( this.d.equals(dn2.d)){
            if (this.next == null)
            {
                if (dn2.next == null)
                    return true;
                return false;
            }
            if(dn2.next == null)
                return false;
            return this.next.d.equals(dn2.next.d);
        }
        return false;
    }
}
