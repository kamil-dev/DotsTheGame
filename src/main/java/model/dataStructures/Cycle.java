/*created on 2019-11-03
 Author: Marcin Bartosiak */
package main.java.model.dataStructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/*
    ==========================
     CLASS IN CONSTRUCTION!!!!!
    ==========================
 */

public class Cycle implements ICycle {
    private int xmin = Integer.MAX_VALUE;
    private int xmax = 0;
    private int ymin = Integer.MAX_VALUE;
    private int ymax = 0;
    private HashMap<Integer,LinkedList<Dot>> dotsHorrizontally  // integer is an x position of a dot,
            = new HashMap<>();                          // list contains dots on the cycle with that x position.
                                                        // does not contain the lists for xmin and xmax
    private HashSet<Dot> dotsSet = new HashSet<>();
    private DotNode dotNode;

    // condition for a circle to have at least one Dot inside
    //      xmax - xmin >= 2
    //      ymax - ymin >= 2
    Cycle(Stack<Dot> stack, int xmin, int xmax, int ymin, int ymax){
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        DotNode next = null;
        DotNode dn = null;
        while (!stack.empty()){
            Dot dot = stack.pop();
            int x = dot.getX();
            if(x != xmin && x != xmax) {
                if(dotsHorrizontally.get(x) == null)
                    dotsHorrizontally.put(x, new LinkedList<>());
                dotsHorrizontally.get(x).add(dot);
            }
            dotsSet.add(dot);
            dn = new DotNode(dot,next);
            next = dn;
        }
        this.dotNode = dn;
    }

    public void replacePath(DotNode dn1, DotNode dn2, Stack<Dot> pathStack)
    {
        if(dn1 == dn2)
            return;
        DotNode dn = dn1.next;
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
        if(x <=xmin || x>= xmax)                //
            return false;
        int outerDotsCount = 0;
        for (Dot circleDot :dotsHorrizontally.get(x)) {
            if (circleDot.getX() < x)
                outerDotsCount ++;
        }
        return (outerDotsCount%2) == 1;
    }

    public Cycle extendCycle() {
        return this;
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
}
