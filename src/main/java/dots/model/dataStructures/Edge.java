/*created on 2019-11-05
 Author: Marcin Bartosiak */
package main.java.dots.model.dataStructures;

public class Edge {
    private Dot d1;
    private Dot d2;
    private int hashCode;

    // max size of a board ensuring unique Edge hashcodes is 1289
    public Edge(Dot d1,Dot d2){
        this.d1 = d1;
        this.d2 = d2;
        this.hashCode = (1289*d1.getX()+d1.getY())* 1289 + (1289*d2.getX()+d2.getY());
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass())
            return false;
        return this.hashCode() == o.hashCode();
    }

    public Dot getD1() {
        return d1;
    }

    public Dot getD2() {
        return d2;
    }
}
