/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.model.dataStructures;

import main.java.model.Board;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Cycle implements ICycle, Cloneable, Serializable {
    private Board board;
    private int ownerId;
    private int xmin = Integer.MAX_VALUE;
    private int xmax = 0;
    private int ymin = Integer.MAX_VALUE;
    private int ymax = 0;

    private HashSet<Dot> dotsSet = new HashSet<>();
    private DotNode dotNode;        // last dotNode.next == null to mark the end of cycle

    public Cycle(Board board, int ownerId, Dot[] path, AtomicInteger pathIndex){
        this.board = board;
        this.ownerId = ownerId;
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

            addDotToCycleAsHead(dot);
        }
    }

    private DotNode addDotToCycleAsHead(Dot dot){
        this.dotNode = new DotNode(dot, this.dotNode);
        dotsSet.add(dot);
        return this.dotNode;
    }


    // assumption of order guarantee:
    //      we have a guarantee that toDot is next from fromDot in Board::extendCycle
    // returns oldPath
    public LinkedList<DotNode> replacePath(DotNode firstDn, Dot[] path, int pathIndex)
    {
        Dot toDot =  path[--pathIndex];

        LinkedList<DotNode> oldPath = new LinkedList<>();
        DotNode toDotDn = deleteOldPath(firstDn, toDot, oldPath);

        DotNode dn = firstDn;
        for (int i = 1 ; i< pathIndex ; i++){          // add new dots to cycle // path[0] is a fromDot
            Dot d = path[i];
            dn = dn.next = new DotNode(d,null);
            dotsSet.add(d);
        }
        dn.next = toDotDn;

        this.recomputeMinAndMaxCoordinatesAndResetDotsSet();
        return oldPath;
    }

    public void recomputeMinAndMaxCoordinatesAndResetDotsSet()
    {
        int xmin = Integer.MAX_VALUE;
        int xmax = 0;
        int ymin = Integer.MAX_VALUE;
        int ymax = 0;
        this.dotsSet = new HashSet<>();
        DotNode dn = this.dotNode;
        while (dn!=null ){
            Dot d = dn.d;
            dotsSet.add(d);
            if(d.getX() > xmax)
                xmax = d.getX();
            if(d.getX() < xmin)
                xmin = d.getX();
            if(d.getY() > ymax)
                ymax = d.getY();
            if(d.getY() < ymin)
                ymin = d.getY();
            dn = dn.next;
        }

        this.xmax = xmax;
        this.xmin = xmin;
        this.ymax = ymax;
        this.ymin = ymin;
    }

    public DotNode getNext(DotNode dn){
        return dn.next!=null ? dn.next : this.getDotNode();
    }

    public DotNode getDotNodeWithDot(Dot d){
        DotNode dn = this.dotNode;
        while (dn!=null && !dn.d.equals(d))
            dn = dn.next;
        return dn;
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
        for(Dot d : c.getDotsSet()){
            if (this.hasOutside(d))
                return false;
        }
        return true;
    }

    public boolean contains(Dot d){
        return dotsSet.contains(d);
    }

    // Dot d is inside a circle if on d.getX() coordinate cycle contains
    //      an odd number cutting through the circle border inside or outside the circle
    // The check is performed by Cycle::isCrossing(DotNode prevDn, DotNode dn)
    public boolean hasInside(Dot d) {

        if (this.contains(d))
            return false;
        if (this.getXmax() - this.getXmin() < 2 || this.getYmax() - this.getYmin() < 2)
            return false;

        int x = d.getX();
        int y = d.getY();

        int borderCrossCount = 0;

        DotNode stopDn = getLastDotDnOnLineOfNeighboringDotsWithTheSameX(this.dotNode);

        DotNode prevDnc = stopDn;
        DotNode dnc;
        do{
            dnc = getNext(prevDnc);
            dnc = getLastDotDnOnLineOfNeighboringDotsWithTheSameX(dnc);

            Dot dot = dnc.d;
            if(dot.getX() == x && dot.getY() < y) {
                if (isCrossing(prevDnc, dnc))
                    borderCrossCount++;
            }

            prevDnc = dnc;
        }while (prevDnc != stopDn);

        return (borderCrossCount%2) == 1;
    }

    public boolean areNeighbours(DotNode dn1, DotNode dn2){
        return dn1.next == dn2 || dn2.next == dn1
                || (dn1.next == null && dn2 == this.dotNode)
                || (dn2.next == null && dn1 == this.dotNode);
    }

    private boolean isCrossing(DotNode prevDn, DotNode dn){
        DotNode nextDn = getNext(dn);
        return ( prevDn.getX() < dn.getX() && dn.getX() < nextDn.getX() )
                ||
                (prevDn.getX() > dn.getX() && dn.getX() > nextDn.getX());
    }

    private DotNode getLastDotDnOnLineOfNeighboringDotsWithTheSameX(DotNode prevDn){
        int x = prevDn.getX();
        DotNode dn = getNext(prevDn);
        while (dn.getX() == x){
            prevDn = dn;
            dn = getNext(dn);
        }
        return prevDn;
    }

    public boolean hasOutside(Dot d) {
        return !dotsSet.contains(d) && !hasInside(d);
    }

    public void cutBase(DotNode firstDnc, Base base){
        DotNode dnc = this.getDotNode();
        while (dnc!=null){
            dnc = dnc.next;
        }
        DotNode dnbb = base.getDotNode();
        while (dnbb!=null){
            dnbb = dnbb.next;
        }


        DotNode firstDnb = base.getCycle().findDnWithDot(firstDnc.d);

        DotNode lastDncOnBase = firstDnc;
        DotNode dn = firstDnc;
        while ( dn != null ){
            if(base.contains(dn.d))
                lastDncOnBase = dn;
            dn = dn.next;
        }

        DotNode firstDnbNext = base.getNext(firstDnb);
        DotNode firstDncNext =  firstDnc.next ;     // firstDnc.next has to exist checked - invocation check required

        deleteOldPath(firstDnc,lastDncOnBase);

        if (firstDnbNext.d != firstDncNext.d) {     // base and cycle go in an opposite direction
            addNewPath(firstDnc,firstDnbNext,lastDncOnBase,base);
        }
        else {  // base and cycle go in the same direction
            DotNode dnb = firstDnbNext;
            while (!dnb.equals(lastDncOnBase)){
                dnb = base.getNext(dnb);
            }
            DotNode lastDnbOnCycle = dnb;

            addNewPathReverse(lastDncOnBase, lastDnbOnCycle, firstDnc, base);
        }
    }

    private void deleteOldPath(DotNode fDnc, DotNode lDnc){
        DotNode dn = fDnc;
        while (dn.next != lDnc){
            dn = dn.next;
            dotsSet.remove(dn.d);
        }
    }

    private DotNode deleteOldPath(DotNode fDnc, Dot lDotnc, LinkedList<DotNode> oldPath){
        DotNode dn = fDnc;
        while (dn.next.d != lDotnc){
            dn = dn.next;
            dotsSet.remove(dn.d);
            oldPath.addLast(dn);
        }
        return dn.next;
    }

    private void addNewPath(DotNode fDnc, DotNode fDnbNext, DotNode lDnc, Base base){
        DotNode dnc = fDnc;
        DotNode dnb = fDnbNext;
        while ( ! dnb.equals(lDnc)){
            dnc = dnc.next = new DotNode(dnb.d,null);
            dotsSet.add(dnb.d);
            dnb = base.getNext(dnb);
        }
        dnc.next = lDnc;
    }

    private void addNewPathReverse(DotNode lDnc, DotNode lDnb, DotNode fDnc, Base base){
        DotNode dnc = lDnc;
        DotNode dnb = base.getNext(lDnb);
        while ( !dnb.equals(fDnc)){
            Dot d = dnb.d;
            dnc = new DotNode(d,dnc);
            dotsSet.add(d);
            dnb = base.getNext(dnb);
        }
        fDnc.next = dnc;
    }

    public void printCycle(){
        System.out.println(""+this.getXmin()+"-" + this.getXmax() + ", " + this.getYmin()+ "-"+ this.getYmax());
        DotNode dn = this.dotNode;
        System.out.println("cycle:");
        do{
            System.out.println(""+dn.d.getX() +", " + dn.d.getY());
            dn = dn.next;
        }while (dn!= null);
    }

    public DotNode findDnWithDot(Dot d){
        DotNode dn = this.dotNode;
        while (dn!=null){
            if(dn.d.equals(d))
                return dn;
            dn = dn.next;
        }
        return null;
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
