/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.model.dataStructures;

import main.java.model.Board;

import java.io.Serializable;

public class Base implements ICycle, Serializable {
    private int ownerId;
    private Cycle cycle;
    private Board board;
    private int pointsCount;

    public Base(Cycle c, Board b, int ownerId){
        this.cycle = c;
        this.board = b;
        this.ownerId = ownerId;
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

    public boolean doesContainACycle(Cycle cycle){
        return this.getCycle().doesContainACycle(cycle);
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

    public DotNode getDotNodeWithDot(Dot d) {
        return this.cycle.getDotNodeWithDot(d);
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public Board getBoard() {
        return board;
    }
}
