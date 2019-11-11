/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.model.dataStructures;

import main.java.model.Board;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
    ==========================
     CLASS IN CONSTRUCTION!!!!!
    ==========================
 */

public class Cycle implements main.java.model.dataStructures.ICycle {
    private Board board;
    private int ownerId;            //xmin i xmax beda mi potrzebne tylko do tworzenia cyklu (do ominiecia pustych prostych cykli
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
            if(dotsHorrizontally.get(x) == null)
                dotsHorrizontally.put(x, new LinkedList<>());
            dotsHorrizontally.get(x).add(dotNode);
        }
        this.dotNode = dotNode;
    }

    // assumption of order guarantee:
    //      we have a guarantee that toDot is next from fromDot in Board::extendCycle
    public void replacePath(DotNode firstDn, Dot[] path, int pathIndex)
    {
        Dot toDot =  path[--pathIndex];
        DotNode toDotDn = firstDn.next;
        while (!toDotDn.d.equals(toDot)){     // delete old dots   // potencjalnie tu może zawisnac przy zlej implementacji
            toDotDn = removeDotNodeFromCyclesDataStructures(toDotDn);
        }
        // dn contains toDot now
        for (int i = 1 ; i< pathIndex ; i++){          // add new dots to cycle // path[0] is a fromDot
            firstDn.next = addDotToCyclesDataStructures(path[i]);
            firstDn = firstDn.next;
        }
        firstDn.next = toDotDn;
    }

    public void recomputeMinAnsMaxCoordinates()
    {
        int xmin = Integer.MAX_VALUE;
        int xmax = 0;
        int ymin = Integer.MAX_VALUE;
        int ymax = 0;
        for (Dot d : this.getDotsSet()) {
            if(d.getX()>xmax)
                xmax = d.getX();
            if(d.getX()<xmin)
                xmin = d.getX();
            if(d.getY() > ymax)
                ymax = d.getY();
            if(d.getY() < ymin)
                ymin = d.getY();
        }
        this.xmax = xmax;
        this.xmin = xmin;
        this.ymax = ymax;
        this.ymin = ymin;
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

    private DotNode addDotToCyclesDataStructures(Dot d){
        DotNode dncNew = new DotNode(d, null);
        dotsSet.add(d);
        int x = d.getX();
        if (!dotsHorrizontally.keySet().contains(x))
            dotsHorrizontally.put(x, new LinkedList<>());
        dotsHorrizontally.get(x).add(dncNew);
        return dncNew;
    }

    // returns next DotNode
    private DotNode removeDotNodeFromCyclesDataStructures(DotNode dotNode){
        Dot d = dotNode.d;
        int x = d.getX();
        System.out.println("delete successful: "+dotsHorrizontally.get(x).remove(dotNode));
        System.out.println("dot delete successful: " + dotsSet.remove(d));
        return dotNode.next;
    }

    public void cutBase(DotNode firstDnc, Base base){
        Dot firstDot = firstDnc.d;
        List<DotNode> dnbsOnFirstDncX = base.getCycle().dotsHorrizontally.get(firstDot.getX());
        Iterator<DotNode> itr = dnbsOnFirstDncX.iterator();
        DotNode firstDnb = itr.next();
        while (!firstDnb.d.equals(firstDot))
            firstDnb = itr.next();
        DotNode firstDnbNext = firstDnb.next!=null ?  firstDnb.next : base.getDotNode();
        DotNode firstDncNext = firstDnc.next!=null ?  firstDnc.next : this.getDotNode();

        DotNode dnc = firstDnc;
        if (firstDnbNext.d != firstDncNext.d) {     // base cycle goes in an opposite direction - cool
            DotNode dnBNewPath = firstDnbNext;
            while (!this.contains(dnBNewPath.d)) {     // add new path dots to cycle   // potencjalnie tu może zawisnac przy zlej implementacji
                dnc.next = addDotToCyclesDataStructures(dnBNewPath.d);
                dnBNewPath = dnBNewPath.next!=null ? dnBNewPath.next : base.getDotNode();
            }
            //dnc.next =
            DotNode lastDncOnBase = firstDncNext;
            while (!lastDncOnBase.d.equals(dnBNewPath.d)) {     // remove old path dots from cycle
                removeDotNodeFromCyclesDataStructures(lastDncOnBase);
                lastDncOnBase = lastDncOnBase.next;
            }
            dnc.next = lastDncOnBase;
        }
        else {      // base cycle goes in the same direction as cycle
            DotNode[] path = new DotNode[dotsSet.size() + base.getCycle().dotsSet.size()];
            int index = -1;
            DotNode lastDnbOnCycle = firstDnc;
            DotNode dnbOldPath = firstDnbNext;
            while (dnbOldPath.d != firstDnc.d){
                path[++index] = dnbOldPath;
                if(this.contains(dnbOldPath.d))
                    lastDnbOnCycle = dnbOldPath;
                dnbOldPath = dnbOldPath.next!=null ? dnbOldPath.next : base.getDotNode();
            }
            Dot lastDot = lastDnbOnCycle.d;
            while ( index >=0 && !path[index].d.equals(lastDot)){       // add new path to cycle
                DotNode dncToAdd = addDotToCyclesDataStructures(path[index--].d);
                dnc.next = dncToAdd;
                dnc = dncToAdd;
            }
            // find last dnc on base
            DotNode lastDncOnBase = firstDncNext;
            while (!lastDncOnBase.d.equals(lastDot)) {
                lastDncOnBase = lastDncOnBase.next;
            }
            dnc.next = lastDncOnBase;
            while (index >= 0)        // remove old dots from cycle
                removeDotNodeFromCyclesDataStructures(path[index--]);
        }
    }

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
