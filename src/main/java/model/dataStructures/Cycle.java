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

    private HashMap<Integer, DotNode> dotsSet = new HashMap<>();
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
        addToDotsSet(this.dotNode);
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
            addToDotsSet(dn);
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
        this.dotsSet = new HashMap<>();
        DotNode dn = this.dotNode;
        while (dn!=null ){
            Dot d = dn.d;
            addToDotsSet(dn);
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
        for (DotNode dn : c.dotsSet.values()) {
            if (this.contains(dn.d))
                if (++commonDotsCount > 1)
                    return true;
        }
        return doesContainACycle(c);
    }

    public boolean doesOverlapWithCycle(Cycle c){
        int commonDotsCount = 0;
        for (DotNode dn : c.dotsSet.values()) {
            if (this.contains(dn.d))
                if (++commonDotsCount > 1)
                    return true;
        }
        return false;
    }

    public boolean doesContainACycle(Cycle c){ // chyba wystarczy sprawdzic czy pierwsza kropka nie nie bedaca czescia cyklu jest w jego wnetrzu
        for(DotNode dn : c.getDotsSet()){
            if (this.hasOutside(dn.d))
                return false;
        }
        return true;
    }

    public boolean contains(Dot d){
        return dotsSet.containsKey(d.hashCode());
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
        return !dotsSet.containsKey(d.hashCode()) && !hasInside(d);
    }

    public void cutOrAddBase(Base base){
        // changing head to prevent infinite cycles
        changeHeadToNodeNotOnBorder(base.getCycle());
        DotNode firstDnc = getDotNode();
        while (!base.contains(firstDnc.d)){
            firstDnc = firstDnc.next;
        }

        DotNode firstDnb = base.getCycle().getDotNodeWithDot(firstDnc.d);

        DotNode lastDncOnBase = firstDnc;
        DotNode dn = firstDnc;
        while ( dn != null ){
            if(base.contains(dn.d))
                lastDncOnBase = dn;
            dn = dn.next;
        }


        DotNode firstDnbNext = base.getNext(firstDnb);
        DotNode firstDncNext = getNext(firstDnc);     // firstDnc.next has to exist checked - invocation check required

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

        breakInfiniteCycleIfItExists();
    }

    private void breakInfiniteCycleIfItExists(){
        Set<Dot> visitedDots = new HashSet<>();
        DotNode head = getDotNode();
        visitedDots.add(head.d);
        DotNode dn = head.next;
        while (dn.next != null ){
            visitedDots.add(dn.d);
            if(visitedDots.contains(dn.next.d)) {
                dn.next = null;
                break;
            }
            dn = dn.next;
        }
    }

    private void deleteOldPath(DotNode fDnc, DotNode lDnc){
        DotNode dn = fDnc;
        while (getNext(dn) != lDnc){
            dn = getNext(dn);
            dotsSet.remove(dn.hashCode());
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
        boolean is_fDncNextNull = fDnc.next==null;
        DotNode dnb = fDnbNext;
        while ( ! dnb.equals(lDnc)){
            dnc = dnc.next = new DotNode(dnb.d,null);
            addToDotsSet(dnc);
            dnb = base.getNext(dnb);
        }
        //if(is_fDncNextNull)   // chyba nie zawsze dobrze to dziala dowiedz sie czemu
            dnc.next = lDnc;
    }

    private void addNewPathReverse(DotNode lDnc, DotNode lDnb, DotNode fDnc, Base base){
        DotNode dnc = lDnc;
        DotNode dnb = base.getNext(lDnb);
        while ( !dnb.equals(fDnc)){
            Dot d = dnb.d;
            dnc = new DotNode(d,dnc);
            addToDotsSet(dnc);
            dnb = base.getNext(dnb);
        }
        //if(fDnc.next != null)
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

    public DotNode getDotNodeWithDot(Dot d){
        return this.dotsSet.get(d.hashCode());
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

    public void changeHeadToNodeNotOnBorder(Cycle neighboringCycle){
        if(neighboringCycle.contains(getDotNode().d)) {
            DotNode head = getDotNode();
            DotNode prevNewHead = head;
            if(head == null || prevNewHead.next == null || null == prevNewHead.d)
                System.out.println("wow");
            try {
                while (neighboringCycle.contains(prevNewHead.next.d))
                    prevNewHead = getNext(prevNewHead);
            }
            catch (Exception e){
                System.out.println("wow2");
            }
            DotNode dn = prevNewHead;
            while(dn.next != null && dn.next != head)
                dn = dn.next;
            dn.next = head;
            this.dotNode = prevNewHead.next;
            prevNewHead.next = null;
        }
    }
    
    public Collection<DotNode> getDotsSet() {
        return dotsSet.values();
    }

    @Override
    protected Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException){
            cloneNotSupportedException.printStackTrace();
            return null;
        }
    }
    
    private DotNode addToDotsSet(DotNode dn){
        return this.dotsSet.put(dn.hashCode(),dn);
    }
    
    private DotNode removeFromDotsSet(DotNode dn){
        return this.dotsSet.remove(dn.hashCode());
    }
}
