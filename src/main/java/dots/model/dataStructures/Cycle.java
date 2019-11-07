/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.dots.model.dataStructures;

import main.java.dots.model.Board;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
    ==========================
     CLASS IN CONSTRUCTION!!!!!
    ==========================
 */

public class Cycle implements ICycle {
    private Board board;
    private int ownerId;
    private int xmin = Integer.MAX_VALUE;
    private int xmax = 0;
    private int ymin = Integer.MAX_VALUE;
    private int ymax = 0;
    private HashMap<Integer,LinkedList<DotNode>> dotsHorrizontally  // integer is an x position of a dot,
            = new HashMap<>();                          // list contains dots on the cycle with that x position.
                                                        // does not contain the lists for xmin and xmax
    private HashSet<Dot> dotsSet = new HashSet<>();
    private DotNode dotNode;                            // last dotNode.next == null to mark the end of cycle

    // condition for a circle to have at least one Dot inside
    //      xmax - xmin >= 2
    //      ymax - ymin >= 2
    public Cycle(Board board, int ownerId, Dot[] path, AtomicInteger pathIndex){
        this.board = board;
        this.ownerId = ownerId;
        DotNode next = null;
        DotNode dotNode = null;
        while (pathIndex.get() > 0){
            pathIndex.set(pathIndex.get() -1);
            Dot dot = path[pathIndex.get()];
            int x = dot.getX();
            int y = dot.getY();
            if( x < xmin)
                xmin = x;
            if( x > xmax)
                xmax = x;
            if( y < ymin)
                ymin = y;
            if( y > ymax)
                ymax = y;

            dotsSet.add(dot);
            dotNode = new DotNode(dot,next);
            next = dotNode;
            if(x != xmin && x != xmax) {
                if(dotsHorrizontally.get(x) == null)
                    dotsHorrizontally.put(x, new LinkedList<>());
                dotsHorrizontally.get(x).add(dotNode);
            }
        }
        this.dotNode = dotNode;
    }

    public void replacePath(DotNode dn1, Dot[] path, AtomicInteger pathIndex)
    {
        int addedDotsNb = pathIndex.get() -1;
        Dot toDot =  path[addedDotsNb];
        DotNode dn = dn1.next;
        Dot d = dn.d;
        while (d != toDot){     // delete old dots   // potencjalnie tu może zawisnac przy zlej implementacji
            dotsSet.remove(d);
            dotsHorrizontally.get(d.getX()).remove(d);
            dn = dn.next;
            d = dn.d;
        }
        // dn contains toDot now
        for (int i = addedDotsNb -1; i>=0  ; i--){          // add new dots to cycle
            d = path[i];
            dn = new DotNode(d,dn);
            dotsSet.add(d);
            int x = d.getX();
            if(!dotsHorrizontally.keySet().contains(x))
                dotsHorrizontally.put(x,new LinkedList<>());
            dotsHorrizontally.get(x).add(dn);
        }
        return;
    }

    public boolean doesOverlapWithCycleOrDoesContainIt(Cycle c) {
        int commonDotsCount = 0;
        for (Dot d : c.dotsSet) {
            if (this.contains(d))
                if (++commonDotsCount > 1)
                    return true;
        }
        return doesContainACycle(c);
    }

    public boolean doesOverlapWithCycle(Cycle c){
        int commonDotsCount = 0;
        for (Dot d : c.dotsSet) {
            if (this.contains(d))
                if (++commonDotsCount > 1)
                    return true;
        }
        return false;
    }

    public boolean doesContainACycle(Cycle c){
        if(    c.getYmax()<= this.getYmax() && c.getYmin() >= this.getYmin()
                && c.getXmax()<= this.getXmax() && c.getXmin() >= this.getXmin() )
            return true;
        return false;
    }

    public boolean contains(Dot d){
        return dotsSet.contains(d);
    }

    // Dot d is inside a cirle if on its x coordinate there is an odd number of circle Dots cd with
    // cd.getX() < d.getX()
    // with an exception:
    // when (d.getX()== xmin || d.getX()== xmax) dot is never inside a circle
    public boolean hasInside(Dot d) {
        int x = d.getX();                       //
        int y = d.getY();
        if(x <=xmin || x>= xmax)                //
            return false;
        int outerDotsCount = 0;                 //
        DotNode prevDotNode = dotsHorrizontally.get(x).get(0);
        if (prevDotNode.d.getY() < y)
            outerDotsCount++;
        for (int i = 1; i< dotsHorrizontally.get(x).size(); i++){
            DotNode dn = dotsHorrizontally.get(x).get(i);
            if (dn.next != prevDotNode && prevDotNode.next != dn  // situation of crosscutting line alignment with the wall
                    && dn.d.getY() < y)
                outerDotsCount++;
            prevDotNode = dn;
        }
        return (outerDotsCount%2) == 1;
    }

    public boolean hasOutside(Dot d) {
        return !dotsSet.contains(d) && ! hasInside(d);
    }

    // == in construction==
    // adds any external dots that could make the cycle area bigger
    public Cycle extendCycle() {
        DotNode dn = this.dotNode;
        Dot addedDot = dn.d;
        Stack<Dot> stack = new Stack<>();
        boolean cycleExtended = false;
        do {
            Set<Edge> visited = new HashSet<>();
            //pushNeighboringDotsOnStack(stack, addedDot, visited);
        }while (cycleExtended);
        return this;
    }

//    private void pushNeighboringDotsOnStack(Stack stack, Dot d, Set<Edge> visited){ // assuming size >2
//        int x = d.getX();
//        int y = d.getY();
//        if(x > 0){
//            pushEdgeOnStack(stack,x-1,y, this.ownerId, visited);
//            if(y > 0)
//                pushEdgeOnStack(stack, x - 1, y - 1, this.ownerId, visited);
//            if(y < board.getSize() -1)
//                pushEdgeOnStack(stack, x - 1, y + 1, this.ownerId, visited);
//        }
//        if(x < board.getSize() -1){
//            pushEdgeOnStack(stack,x+1,y, this.ownerId, visited);
//            if(y > 0)
//                pushEdgeOnStack(stack,x + 1,y - 1, this.ownerId, visited);
//            if(y < board.getSize() -1)
//                pushEdgeOnStack(stack,x + 1,y + 1, this.ownerId, visited);
//        }
//        if (y > 0)
//            pushEdgeOnStack(stack,x,y - 1, this.ownerId, visited);
//        if (y < board.getSize() -1)
//            pushEdgeOnStack(stack,x,y + 1, this.ownerId, visited);
//    }
//
//    private boolean pushEdgeOnStack(Stack<Edge> stack, int x, int y, int activePlayer, Set<Dot> visited){
//        Dot dot = board.getDot(x,y);
//        if(dot == null || dot.getOwnerId()!= activePlayer || visited.contains(dot))
//            return false;
//        stack.push(dot);
//        return true;
//    }
    public void printCycle(){
        DotNode dn = this.dotNode;
        System.out.println("cycle:");
        do{
            System.out.println(""+dn.d.getX() +", " + dn.d.getY());
            dn = dn.next;
        }while (dn!= null);
    }

    public int getXmin() {
        return xmin;
    }

    public int getXmax() {
        return xmax;
    }

    public int getYmin() {
        return ymin;
    }

    public int getYmax() {
        return ymax;
    }

    public DotNode getDotNode() {
        return dotNode;
    }

    public HashSet<Dot> getDotsSet() {
        return dotsSet;
    }
}
