/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.model.dataStructures;

import main.java.model.Board;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Cycle implements ICycle {
    private Board board;
    private int ownerId;            //xmin i xmax beda mi potrzebne tylko do tworzenia cyklu (do ominiecia pustych prostych cykli
    private int xmin = Integer.MAX_VALUE;
    private int xmax = 0;
    private int ymin = Integer.MAX_VALUE;
    private int ymax = 0;

    private HashSet<Dot> dotsSet = new HashSet<>();
    private DotNode dotNode;                            // last dotNode.next == null to mark the end of cycle

    // condition for a circle to have at least one Dot inside
    //      xmax - xmin >= 2
    //      ymax - ymin >= 2
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

    private DotNode addDotToCyclesDataStructures(Dot d){
        DotNode dncNew = new DotNode(d, null);
        dotsSet.add(d);
        return dncNew;
    }

    private DotNode addDotToCyclesDataStructures(Dot d, DotNode next){
        DotNode dncNew = addDotToCyclesDataStructures(d);
        dncNew.next = next;
        return dncNew;
    }

    private DotNode addDotToCyclesDataStructures(DotNode prev, Dot d){
        DotNode dncNew = addDotToCyclesDataStructures(d);
        prev.next = dncNew;
        return dncNew;
    }


    // assumption of order guarantee:
    //      we have a guarantee that toDot is next from fromDot in Board::extendCycle
    public void replacePath(DotNode firstDn, Dot[] path, int pathIndex)
    {
        Dot toDot =  path[--pathIndex];
        System.out.println("replace path TO DOT:" + toDot.getX() +"," + toDot.getY());

        DotNode toDotDn = deleteOldPath(firstDn, toDot);

        DotNode dn = firstDn;
        for (int i = 1 ; i< pathIndex ; i++){          // add new dots to cycle // path[0] is a fromDot
            Dot d = path[i];
            dn = dn.next = new DotNode(d,null);
            dotsSet.add(d);
        }
        dn.next = toDotDn;

        System.out.println("cycle after replace path");
        printCycle();
        this.recomputeMinAndMaxCoordinatesAndResetDotsSet();
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

    // Dot d is inside a cirle if on its x coordinate there is an odd number of circle Dots cd with
    // cd.getX() < d.getX()
    // with an exception:
    // when (d.getX()== xmin || d.getX()== xmax) dot is never inside a circle
    public boolean hasInside(Dot d) {
        if (dotsSet.contains( d))
            return false;
        int x = d.getX();
        int y = d.getY();

        boolean northDn = false;
        boolean southDn = false;
        boolean eastDn = false;
        boolean westDn = false;


        DotNode dn = this.dotNode;

        while (dn != null){
            if (dn.getX() == x)
            {
                if(dn.getY() < y)
                    westDn = true;
                else
                    eastDn = true;
            }
            if(dn.getY() == y){
                if(dn.getX() < x)
                    northDn = true;
                else
                    southDn = true;
            }
            dn = dn.next;
        }
        return northDn && eastDn && westDn && southDn;

//        int x = d.getX();
//
//        LinkedList<DotNode> dnsOnX = dotsHorrizontally.get(x);
//        if(dnsOnX == null || dnsOnX.size() == 0)
//            return false;
//
//        DotNode dn, prevDn, nextOnList;
//        dn = dnsOnX.get(0);
//        prevDn = this.dotNode;
//        while (prevDn.next != null && dn != prevDn.next ){
//            prevDn = prevDn.next;
//        }
//
//        int borderCrossCount = 0;
//        int i = 1;
//        while (i< dnsOnX.size()){
//            while (i< dnsOnX.size() -1 && x == (nextOnList = dnsOnX.get(i+1)).getX() && areNeighbours(nextOnList, dn) ) {
//                dn = nextOnList;
//                i++;
//            }
//            if(areOpposite(prevDn,dn))
//                borderCrossCount++;
//
//            prevDn = dn;
//            dn = dnsOnX.get(i++);
//        }
//
//        return (borderCrossCount%2) == 1;
    }
    
//    private boolean areNeighbours(DotNode dn1, DotNode dn2){
//        return dn1.next == dn2 || dn2.next == dn1;
//    }
//
//    private boolean areOpposite(DotNode prevDn, DotNode dn){
//        DotNode nextDn = dn.next!= null ? dn.next : this.dotNode;
//        return ( prevDn.getX() < dn.getX() && dn.getX() < nextDn.getX() )
//                ||
//                (prevDn.getX() > dn.getX() && dn.getX() > nextDn.getX());
//    }
//    //private boolean didCross()

    public boolean hasOutside(Dot d) {
        return !dotsSet.contains(d) && !hasInside(d);
    }

    // returns next DotNode
    private DotNode removeDotNodeFromCyclesDataStructures(DotNode dotNode){
        Dot d = dotNode.d;
        System.out.println("dot "+d.getX()+','+d.getY()+"dot delete successful: " + dotsSet.remove(d));
        return dotNode.next;
    }

    //including DotNode excluding Dot
    private DotNode removePathFromDotNodeUntilDot(DotNode fromDnc, Dot toDot){
        while (!fromDnc.d.equals(toDot)) {     // remove old path dots from cycle  //
            removeDotNodeFromCyclesDataStructures(fromDnc);
            System.out.println("ttuu");
            if(fromDnc.next == null)
                return fromDnc;
            fromDnc = fromDnc.next;
        }
        return fromDnc;
    }

    public void cutBase(DotNode firstDnc, Base base){


        DotNode firstDnb = base.getDotNode();
        while (!firstDnb.equals(firstDnc))
            firstDnb = base.getNext(firstDnb);

        DotNode lastDncOnBase = firstDnc;
        DotNode dn = firstDnc;
        while ( dn != null ){
            if(base.contains(dn.d))
                lastDncOnBase = dn;
            dn = dn.next;
        }

        deleteOldPath(firstDnc,lastDncOnBase);

        DotNode firstDnbNext = base.getNext(firstDnb);
        DotNode firstDncNext =  firstDnc.next ; // firstDnc.next musi istniec w przeciwnym wypadku bledne wywolanie

        if (firstDnbNext.d != firstDncNext.d) {     // base cycle goes in an opposite direction - cool
            DotNode dnb = firstDnbNext;
            while (!dnb.equals(lastDncOnBase)){
                dnb = base.getNext(dnb);
            }
            DotNode lastDnbOnCycle = dnb;

            addNewPathReverse(lastDncOnBase, lastDnbOnCycle, firstDnc, base);
        }
        else {
            addNewPath(firstDnc,firstDnbNext,lastDncOnBase,base);
        }

        this.recomputeMinAndMaxCoordinatesAndResetDotsSet();
    }

    private void deleteOldPath(DotNode fDnc, DotNode lDnc){
        DotNode dn = fDnc;
        while (dn.next != lDnc){
            dn = dn.next;
            dotsSet.remove(dn.d);
        }
    }
    private DotNode deleteOldPath(DotNode fDnc, Dot lDotnc){
        DotNode dn = fDnc;
        while (dn.next.d != lDotnc){
            dn = dn.next;
            dotsSet.remove(dn.d);
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
            dotsSet.add(d);
            dnb = base.getNext(dnb);
        }
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
