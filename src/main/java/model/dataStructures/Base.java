/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.model.dataStructures;

import main.java.model.Board;

public class Base implements ICycle{
    private int ownerId;
    private Cycle cycle;
    private Board board;
    private int pointsCount;

    public Base(Cycle c, Board b, int ownerId){
        this.cycle = c;
        this.board = b;
        this.ownerId = ownerId;
        this.pointsCount = countPoints();
    }

    public DotNode getNext(DotNode dn){
        return cycle.getNext(dn);
    }

    @Override
    public boolean contains(Dot d) {
        return cycle.contains(d);
    }

    @Override
    public boolean hasInside(Dot d) {
        return cycle.hasInside(d);
    }

    @Override
    public boolean hasOutside(Dot d) {
        return cycle.hasOutside(d);
    }

    private int countPoints(){
        int pointsCount = 0;
        for(int i = this.cycle.getXmin(); i<=this.cycle.getXmax(); i++)
            for(int j = this.cycle.getYmin(); j <= this.cycle.getYmax(); j++) {
                Dot d = board.getDot(i,j);
                if ( d!=null && (this.contains(d) || this.hasInside(d)) )
                    pointsCount++;
            }
        return pointsCount;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
    }

    public DotNode getDotNode(){
        return this.cycle.getDotNode();
    }
//    public int getXmin() {
//        return cycle.getXmin();
//    }
//
//    public int getXmax() {
//        return cycle.getXmax();
//    }
//
//    public int getYmin() {
//        return cycle.getYmin();
//    }
//
//    public int getYmax() {
//        return cycle.getYmax();
//    }

    public int getPointsCount() {
        return pointsCount;
    }
}
