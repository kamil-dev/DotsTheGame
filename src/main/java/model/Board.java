package main.java.model;

import main.java.model.dataStructures.*;
import main.java.view.BoardSquare;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
    ==========================
     CLASS IN CONSTRUCTION!!!!!
    ==========================
 */
public class Board {
    private Dot[][] matrixOfDots;
    private int size;
    private int dotNb = 1;
    private int freeDotSpacesCount;

    private int activePlayer = 0; //  opposing player nb is: 1 - activePlayer
    private int pointsPlayer0 = 0;
    private int pointsPlayer1 = 0;

    private Set<Cycle>[] cyclesOfPlayers = new Set[2];
    private Set<Base>[] basesOfPlayer = new Set[2];

    public Board(int size) {
        this.matrixOfDots = new Dot[size][size];
        this.cyclesOfPlayers[0] = new HashSet<>();
        this.cyclesOfPlayers[1] = new HashSet<>();
        this.basesOfPlayer[0] = new HashSet<>();
        this.basesOfPlayer[1] = new HashSet<>();
        this.size = size;
        this.freeDotSpacesCount = size * size;
        int activePlayer = 0;
    }

    private boolean isPlacingADotPossible(Dot d){       // checking if d is a border of a cycle is not needed
        if(matrixOfDots[d.getX()][d.getY()] != null)
            return false;
        for (Base b: basesOfPlayer[1-activePlayer])
            if ( b.hasInside(d))
                return false;
        for (Base b: basesOfPlayer[activePlayer])
            if ( b.hasInside(d))
                return false;
        return true;
    }

    private boolean isAvailable(Dot neighbouringDot, Set<Dot> visited){
        return neighbouringDot!= null
                && !neighbouringDot.isInsideBase()
                && !visited.contains(neighbouringDot);
    }

    private boolean isAvailableExcludingFromDot(Dot neighbouringDot, Dot fromDot, Set<Dot> visited){
        return isAvailable(neighbouringDot,visited)
                && !neighbouringDot.equals(fromDot);
    }

    private boolean isAvailableForOutsidePath(Dot neighbouringDot, Dot fromDot, Cycle cycle, Set<Dot> visited, int pathInd){
        return isAvailableExcludingFromDot(neighbouringDot, fromDot, visited)
                && !(pathInd == 1 && cycle.contains(neighbouringDot))
                && !cycle.hasInside(neighbouringDot);
    }


    private void extendCycle(Cycle cycle){
        DotNode dn = cycle.getDotNode();
        while (dn != null){
            Dot[] path = new Dot[(dotNb+3)/2]; // size == nb of player's dots
            AtomicInteger pathIndex = new AtomicInteger(0);
            HashSet<Dot> visited = new HashSet<>();
            if(findPathExtendingCycle(cycle, dn.d, path, pathIndex, visited)) {
                System.out.println(" From node:" + path[0].getX()+"'" + path[0].getY());
                System.out.println(" Replacement Path:");
                for(int i = 0 ; i < pathIndex.get(); i++){
                    Dot d = path[i];
                    System.out.println("x:" + d.getX()+" y:" + d.getY());
                }
                cycle.replacePath(dn, path, pathIndex.get());
                dn = cycle.getDotNode();
                System.out.println("Path was repeeeeeeeeed");
            }
            else
                dn = dn.next;
        }
    }

    private boolean findPathExtendingCycle(Cycle cycle, Dot startingDot, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited){
        visited.add(startingDot);
        path[pathIndex.get()] = startingDot;
        pathIndex.set(pathIndex.get() + 1);
        if(applyFindPathToCycleToNeighboringDotsOutsideCycle(cycle, startingDot, path, pathIndex, visited))
            return true;

        // if path to toDot through currentDot (considering previous Dots on path) does not exist
        // Remove current Dot from path and mark it as unvisited
        visited.remove(startingDot);
        pathIndex.set(pathIndex.get() -1);
        return false;
    }


    private boolean applyFindPathToCycleToNeighboringDotsOutsideCycle(Cycle cycle, Dot startingDot, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited){

        int x = startingDot.getX();
        int y = startingDot.getY();

        // check if the path to cycle exists through neighboring Dots
        Dot neighbouringDot;
        if (y > 0)
        {
            neighbouringDot = getDot(x,y-1, activePlayer);
            if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle,visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) )
                return true;
        }
        if ( y < size -1){
            neighbouringDot = getDot(x,y+1, activePlayer);
            if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle, visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) )
                return true;
        }
        if ( x > 0){
            neighbouringDot = getDot(x-1,y, activePlayer);
            if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle, visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) ) {
                System.out.println("yep here");
                return true;
            }
            if (y > 0)
            {
                neighbouringDot = getDot(x-1,y-1, activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) )
                    return true;
            }
            if ( y < size -1){
                neighbouringDot = getDot(x-1,y+1, activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) )
                    return true;
            }
        }
        if ( x < size - 1){
            neighbouringDot = getDot(x+1,y,activePlayer);

            if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle, visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) )
                return true;
            if (y > 0)
            {
                neighbouringDot = getDot(x+1,y-1,activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) )
                    return true;
            }
            if ( y < size -1){
                neighbouringDot = getDot(x+1,y+1,activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, startingDot, cycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited) )
                    return true;
            }
        }
        return false;
    }

    boolean findPathToCycle(Dot currentDot, Dot fromDot, Cycle toCycle, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited){
        visited.add(currentDot);
        path[pathIndex.get()] = currentDot;
        pathIndex.set(pathIndex.get() + 1);

        if ( toCycle.contains(currentDot))
            return true;

        if(applyFindPathToCycleViaNeighboringDots(currentDot, fromDot, toCycle, path, pathIndex, visited))
            return true;

        // if path to toDot through currentDot (considering previous Dots on path) does not exist
        // Remove current Dot from path and mark it as unvisited
        pathIndex.set(pathIndex.get() -1);
        visited.remove(currentDot);
        return false;
    }

    private boolean applyFindPathToCycleViaNeighboringDots(Dot currentDot, Dot fromDot, Cycle toCycle, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited){

        int x = currentDot.getX();
        int y = currentDot.getY();

        Dot neighbouringDot;
        if (y > 0)
        {
            neighbouringDot = getDot(x,y-1, activePlayer);
            if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                return true;
        }
        if ( y < size -1){
            neighbouringDot = getDot(x,y+1, activePlayer);
            if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                return true;
        }
        if ( x > 0){
            neighbouringDot = getDot(x-1,y, activePlayer);
            if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                return true;
            if (y > 0)
            {
                neighbouringDot = getDot(x-1,y-1, activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                    return true;
            }
            if ( y < size -1){
                neighbouringDot = getDot(x-1,y+1, activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                    return true;
            }
        }
        if ( x < size - 1){
            neighbouringDot = getDot(x+1,y, activePlayer);

            if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                    && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                return true;
            if (y > 0)
            {
                neighbouringDot = getDot(x+1,y-1, activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                    return true;
            }
            if ( y < size -1){
                neighbouringDot = getDot(x+1,y+1, activePlayer);
                if( isAvailableForOutsidePath(neighbouringDot, fromDot, toCycle, visited, pathIndex.get())
                        && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited) )
                    return true;
            }
        }
        return false;
    }


    private void shrinkCycleToBordersWithBases(Cycle cycle){
        DotNode dn = cycle.getDotNode();
        List<Base> basesContainingD;
        Set<Base> bases = new HashSet<>(basesOfPlayer[activePlayer]);
        while (dn != null){
            basesContainingD = getBasesFromASetContainingDot(dn.d, bases);
            System.out.println("Bases of player"+activePlayer + " count:" + bases.size());
            boolean baseDeleted = false;
            for(Base base: basesContainingD){
                if(base != null && dn.next!=null && base.contains(dn.next.d)) {
                    baseDeleted = true;
                    System.out.println("player"+activePlayer+" cut base:");
                    base.getCycle().printCycle();
                    for(Dot d : base.getCycle().getDotsSet())
                        System.out.println("d"+d.getX()+" " +d .getY());
                    System.out.println("from cycle:");
                    cycle.printCycle();
                    cycle.cutBase(dn, base);
                    System.out.println("Cycle after cut:");
                    cycle.printCycle();
                    for(Dot d : cycle.getDotsSet())
                        System.out.println("d.x:" + d.getX()+" d.y:"+d.getY());
                    bases.remove(base); // to nie koniecznie musi byc zawsze poprawne
                }
            }
            if(!baseDeleted)
                dn = dn.next;
        }
        cycle.recomputeMinAndMaxCoordinatesAndResetDotsSet();
    }

    private List<Base> getBasesFromASetContainingDot(Dot d, Set<Base> bases){
        LinkedList<Base> basesList = new LinkedList<>();
        for (Base b : bases){
            if (b.contains(d))
                basesList.add(b);
        }
        return basesList;
    }










    boolean applyFindCyclePathToNeighboringDots(Dot currentDot, Dot toDot, int xmin, int xmax, int ymin, int ymax, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited){
        int x = currentDot.getX();
        int y = currentDot.getY();
        if(x < xmin)
            xmin = x;
        if( x > xmax)
            xmax = x;
        if( y < ymin)
            ymin = y;
        if( y > ymax)
            ymax = y;

        // check the path to toDot exists through neighboring Dots
        Dot neighbouringDot;
        if (y > 0)
        {
            neighbouringDot = getDot(x,y-1,activePlayer);
            //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY());
            if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                return true;
        }
        if ( y < size -1){
            neighbouringDot = getDot(x,y+1,activePlayer);
            //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY());
            if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                return true;
        }
        if ( x > 0){
            neighbouringDot = getDot(x-1,y,activePlayer);
            //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY());
            if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                return true;
            if (y > 0)
            {
                neighbouringDot = getDot(x-1,y-1,activePlayer);
                //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY());
                if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                    return true;
            }
            if ( y < size -1){
                neighbouringDot = getDot(x-1,y+1,activePlayer);
                //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY());
                if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                    return true;
            }
        }
        if ( x < size - 1){
            neighbouringDot = getDot(x+1,y,activePlayer);
            //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY());
            if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                return true;
            if (y > 0)
            {
                neighbouringDot = getDot(x+1,y-1,activePlayer);
                //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY());
                if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                    return true;
            }
            if ( y < size -1){
                neighbouringDot = getDot(x+1,y+1,activePlayer);
                //System.out.println("neighb:" + neighbouringDot.getX() + " " + neighbouringDot.getY() );
                if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited) )
                    return true;
            }
        }
        return false;
    }

    // for each neighbor of toDot(which is an addedDot) call findCyclePath
    // until you find
    boolean findCyclePath(Dot currentDot, Dot toDot, int xmin, int xmax, int ymin, int ymax, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited){

        System.out.println(" " +currentDot.getX() +" " + currentDot.getY());
        if ( currentDot.equals(toDot)) {
            if (pathIndex.get()!= 0) {
                // check for not allowing a neighbor to come back to toDot at start
                if (xmax - xmin < 2 && ymax - ymin < 2)  // I think the outOfBounds check is not needed
                    return false;
                return true;    // cycle found - naiwny check
            }
        }
        else
            visited.add(currentDot);

        path[pathIndex.get()] = currentDot;
        pathIndex.set(pathIndex.get() + 1);

        System.out.println( "currentDot: " +currentDot.getX() +" " + currentDot.getY() +" owner:" + currentDot.getOwnerId());

        if(applyFindCyclePathToNeighboringDots(currentDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited))
            return true;

        // if path to toDot through currentDot (considering previous Dots on path) does not exist
        // Remove current Dot from path and mark it as unvisited
        if(visited.remove(currentDot))
            pathIndex.set(pathIndex.get() -1);
        return false;
    }

    // == in construction ==
    // to raczej trzeba inaczej zrobic: https://www.geeksforgeeks.org/print-all-the-cycles-in-an-undirected-graph/
    Cycle aNewCycleCreatedByDot(Dot addedDot){
        int x = addedDot.getX();
        int y = addedDot.getY();
        Dot[] path = new Dot[(dotNb+1)/2+8]; // size == nb of player's dots
        //path[0] = addedDot;
        AtomicInteger pathIndex = new AtomicInteger(0);
        HashSet<Dot> visited = new HashSet<>();     // addedDot is on the path but not visited at start
        if (findCyclePath(addedDot,addedDot, x,x,y,y, path, pathIndex, visited)){
            System.out.println("cycle found!");
            for (int i = 0; i< pathIndex.get(); i++){
                Dot ddd = path[i];
                System.out.println(""+ddd.getX() +", " + ddd.getY());
            }
            System.out.println("end of cycle");
            return new Cycle(this, activePlayer, path, pathIndex);
        }
        return null;
    }

    Cycle aSecondCycleCreatedByDot(Dot addedDot, Cycle firstCycle){
        int x = addedDot.getX();
        int y = addedDot.getY();
        Dot[] path = new Dot[(dotNb+3)/2]; // size == nb of player's dots
        //path[0] = addedDot;
        AtomicInteger pathIndex = new AtomicInteger(0);
        HashSet<Dot> visited = new HashSet<>();     // addedDot is on the path but not visited at start
        visited.addAll(firstCycle.getDotsSet());
        visited.remove(addedDot);
        if (findCyclePath(addedDot,addedDot, x,x,y,y, path, pathIndex, visited)){
            System.out.println("second cycle found!");
            for (int i = 0; i< pathIndex.get(); i++){
                Dot ddd = path[i];
                System.out.println(""+ddd.getX() +", " + ddd.getY());
            }
            return new Cycle(this, activePlayer, path,pathIndex);
        }
        return null;
    }

    boolean isBase(Cycle newCycle, Dot d, int activePlayer){
        if (newCycle.contains(d)){
            for(int x = newCycle.getXmin(); x<newCycle.getXmax() ; x++)
                for ( int y = newCycle.getYmin() ; y< newCycle.getYmax(); y++){
                    Dot dot = getDot(x,y);
                    if(null != dot && !dot.isInsideBase() && dot.getOwnerId() != activePlayer && newCycle.hasInside(dot) )
                        return true;
                }
        }
        return false;
    }


    private Cycle getCycleContainingDotFromASet(Dot dot, Set<Cycle> cycles) {
        for(Cycle c: cycles)
            if (c.contains(dot))
                return c;
        return null;
    }

    private Cycle getCycleFromASetHavingADotInside(Dot dot, Set<Cycle> cycles) {
        for(Cycle c: cycles)
            if (c.hasInside(dot))
                return c;
        return null;
    }

    private Base getBaseContainingDot(Dot d){
        int x = d.getX();
        int y = d.getY();
        for (Base b : basesOfPlayer[activePlayer]) {
            if(b.contains(d))
                return b;
        }
        return null;
    }

    Cycle getAnEmptyOpponentCycleContainingDot(Dot dot, int activePlayer){
        return getCycleFromASetHavingADotInside(dot, cyclesOfPlayers[1-activePlayer]);
    }

    // koncepcja 1 przechowujesz puste cykle graczy
    // koncepcja 2 nie przechowujesz pustych cykli graczy

    // == fill the commented areas ==
    private Base createBase(Cycle cycle, int owner) {
        System.out.println("Base created!");
        cycle.printCycle();
        cyclesOfPlayers[activePlayer].remove(cycle);
        Base base = new Base(cycle, this, owner);
        // Koncepcja 1 : zmien odpowiednio puste cykle graczy
        for (int i = 0; i < 2; i++) {                     // usun zamkniete wewnatrz bazy i cykle
            Set<Cycle> cyclesOfPlayer = cyclesOfPlayers[i];
            for (Cycle c : cyclesOfPlayer) {
                if (cycle.doesContainACycle(c))
                    cyclesOfPlayer.remove(c);
                else if (cycle.doesOverlapWithCycle(c)) {
                    if (shrinkCycleIfItWasReduced(c, cycle) == null)
                        cyclesOfPlayer.remove(c);
                }
            }
        }

        int pointsCount = 0;
        for (int x = cycle.getXmin() + 1; x < cycle.getXmax(); x++)
            for (int y = cycle.getYmin() + 1; y < cycle.getYmax(); y++) {
                Dot d = getDot(x, y);
                if (d != null && cycle.hasInside(d)) {
                    d.markAsInsideBase();
                    if (d.getOwnerId() == activePlayer)
                        pointsCount--;
                    else
                        pointsCount++;
                }
            }
        base.setPointsCount(pointsCount);
        basesOfPlayer[activePlayer].add(base);
        // policz punkty: wersja ambitna lub (malo kosztowny) brute force: zlicz pkty z baz kazdego z graczy
        return base;
    }

    private Cycle shrinkCycleIfItWasReduced(Cycle cycleToBeShrinked, Cycle base){
        Cycle newCycle = null;
        for(Dot d : cycleToBeShrinked.getDotsSet()){
            if (base.hasOutside(d))
                if( null != (newCycle = aNewCycleCreatedByDot(d)) )
                    break;
        }
        return newCycle;
    }

    private void replaceOldCyclesWithANewOne(Cycle newCycle, int activePlayer){
        Set<Cycle> cyclesOfActivePlayer = cyclesOfPlayers[activePlayer];
        Set<Cycle> cyclesToRemove = new HashSet<>();
        for(Cycle c: cyclesOfActivePlayer) {
            if (newCycle.doesOverlapWithCycleOrDoesContainIt(c))
                cyclesToRemove.add(c);
        }
        cyclesOfActivePlayer.removeAll(cyclesToRemove);
        cyclesOfActivePlayer.add(newCycle);
    }

    public void addDot(int x, int y){
        addDot(new Dot(x,y,activePlayer));
    }

    // == fill the commented areas ==
    public void addDot(Dot d){
        if(!isPlacingADotPossible(d))
            return;
        matrixOfDots[d.getX()][d.getY()] = d;
        this.dotNb++;
        boolean baseCreated = false;
        Cycle newCycle = aNewCycleCreatedByDot(d);
        if (newCycle != null) {
            extendCycle(newCycle);
            System.out.println("extended cycle:");
            newCycle.printCycle();
            shrinkCycleToBordersWithBases(newCycle);

            if (newCycle.contains(d)) {
                replaceOldCyclesWithANewOne(newCycle, activePlayer);    // konc1
                if (isBase(newCycle, d, activePlayer)) {
                    Base base = createBase(newCycle, activePlayer);
                    basesOfPlayer[activePlayer].add(base);
                    drawBase(base);
                    baseCreated = true;
                }
                newCycle = aSecondCycleCreatedByDot(d, newCycle);        // case of the second cycle created by dot
                if (newCycle != null) {
                    extendCycle(newCycle);
                    shrinkCycleToBordersWithBases(newCycle);
                    if (newCycle.contains(d)) {
                        replaceOldCyclesWithANewOne(newCycle, activePlayer);    // konc1
                        if (isBase(newCycle, d, activePlayer)) {
                            Base base = createBase(newCycle, activePlayer);
                            basesOfPlayer[activePlayer].add(base);
                            drawBase(base);
                            baseCreated = true;
                        }
                    }
                }
            }
        }

        // konc 1:
        if( !baseCreated &&
                null != (newCycle = getAnEmptyOpponentCycleContainingDot(d, activePlayer))
        ) {
            Base base = createBase(newCycle, 1 - activePlayer);
            drawBase(base);
            // konc 2: znajdz wszystkie cykle przeciwnika w których się zawiera Dot d i stwórz baze z najbardziej zewnetrznego
            //       nie lubie tego rozwiazania bo czesto sie wywoluje i cos tam kosztuje.

        }
        if(baseCreated) updatePoints(activePlayer);
        this.activePlayer = 1 - this.activePlayer;
    }

    public int getSize() {
        return size;
    }

    public Dot getDot(int x, int y){
        return matrixOfDots[x][y];
    }
    public Dot getDot(int x, int y, int activePlayer){
        Dot d = getDot(x,y);
        if (d== null || d.getOwnerId() != activePlayer)
            return null;
        return d;
    }

    public void setMatrixOfDots(Dot[][] b){
        this.matrixOfDots = b;
    }
    //
//
//    private boolean isClosurePossible(Dot d){
//        List<Dot> adjacentDots = this.findAdjacent(d);
//        return !(adjacentDots.size() < 2 || areAdjacent(adjacentDots.get(0),adjacentDots.get(1))) ;
//    }
//
//    private static boolean areAdjacent(Dot dot1, Dot dot2){
//        return (Math.abs(dot1.getX()-dot2.getX()) <= 1 && Math.abs(dot1.getY()-dot2.getY()) <= 1);
//    }
//
//    public List<Dot> findAdjacent(Dot d){
//        List<Dot> adjacentDots = new LinkedList<>();
//        int x = d.getX();
//        int y = d.getY();
//        for (int i = (x == 0) ? 0 : x - 1; i <= ((x == matrixOfDots.length - 1) ? x : x + 1 ) ; i++) {
//            for (int j = (y == 0) ? 0 : y - 1; j <= ((y == matrixOfDots.length - 1) ? y : y + 1 ) ; j++) {
//                if (i == x && j == y) ;
//                else if (matrixOfDots[i][j] != null && matrixOfDots[i][j].getOwnerId().equals(d.getOwnerId()) && !matrixOfDots[i][j].isInsideBase()){
//                        adjacentDots.add(matrixOfDots[i][j]);
//                }
//            }
//        }
//        return adjacentDots;
//    }
//
//    public boolean isEnclosement(Dot startingDot){
//        Stack<Dot> stackOfChainedDots = new Stack<>();
//        stackOfChainedDots.push(startingDot);
//        Enclousure.LAST_ENCLOUSURE = null;
//        findClousure(stackOfChainedDots);
//        return Enclousure.LAST_ENCLOUSURE != null;
//    }
//
//    public void findClousure(Stack<Dot> listOfChainedDots) {
//        System.out.println(listOfChainedDots);
//        if (listOfChainedDots.isEmpty()) return;
//        if (listOfChainedDots.size() > 3 && areAdjacent(listOfChainedDots.get(0), listOfChainedDots.peek())) {
//            List<Dot> opponentsLockedDots = listOfOpponentsLockedDots(listOfChainedDots);
//            if (!opponentsLockedDots.isEmpty()) {
//                List<Dot> outerDots = new LinkedList<>();
//                for (Dot d : listOfChainedDots){
//                    outerDots.add(d);
//                }
//                Enclousure.setEnclousure(outerDots,opponentsLockedDots);
//                return;
//            }
//        }
//        List<Dot> adjacentToTheLast = this.findAdjacent(listOfChainedDots.peek());
//        if (adjacentToTheLast.size() == 1) {
//            listOfChainedDots.pop();
//        } else {
//            for (Dot d : adjacentToTheLast) {
//                if (!listOfChainedDots.contains(d)) {
//                    listOfChainedDots.push(d);
//                    findClousure(listOfChainedDots);
//                }
//            }
//            listOfChainedDots.pop();
//        }
//        return;
//    }
//
//    public List<Dot> listOfOpponentsLockedDots(List<Dot> chainOfDots){
//        List<Dot> lockedDots = new LinkedList<>();
//        int minX = chainOfDots.get(0).getX();
//        int maxX = chainOfDots.get(0).getX();
//        int minY = chainOfDots.get(0).getY();
//        int maxY = chainOfDots.get(0).getY();
//        for (int i = 1; i < chainOfDots.size() ; i++) {
//            minX = Math.min(minX,chainOfDots.get(i).getX());
//            maxX = Math.max(maxX,chainOfDots.get(i).getX());
//            minY = Math.min(minY,chainOfDots.get(i).getY());
//            maxY = Math.max(maxY,chainOfDots.get(i).getY());
//        }
//
//        if (maxX - minX < 2 || maxY - minY < 2) return null;
//        for (int i = minX+1; i < maxX; i++) {
//            for (int j = minY+1; j < maxY ; j++) {
//                if (! (matrixOfDots[i][j].getOwnerId() == chainOfDots.get(0).getOwnerId()))
//                    if (isSourrounded(chainOfDots, matrixOfDots[i][j])) lockedDots.add(matrixOfDots[i][j]);
//            }
//        }
//
//        return lockedDots;
//    }
//
    public static boolean isSourrounded(List<Dot> chainOfDots, Dot d){
        boolean isNorthBorderDot = false;
        boolean isSouthBorderDot = false;
        boolean isWestBorderDot = false;
        boolean isEastBorderDot = false;
        int x = d.getX();
        int y = d.getY();

        for (Dot chainedDot : chainOfDots){
            if (chainedDot.getX() == x && chainedDot.getY() < y) isEastBorderDot = true;
            if (chainedDot.getX() == x && chainedDot.getY() > y) isWestBorderDot = true;
            if (chainedDot.getY() == y && chainedDot.getX() < x) isNorthBorderDot = true;
            if (chainedDot.getY() == y && chainedDot.getX() > x) isSouthBorderDot = true;
        }
        return isEastBorderDot && isNorthBorderDot && isSouthBorderDot && isWestBorderDot;
    }
    private static void drawBase(Base base){
        Cycle cycleToDraw = base.getCycle();
        DotNode dotNode = cycleToDraw.getDotNode();
        List<Dot> sortedListOfDotsWithinACycle = new ArrayList<>();
        sortedListOfDotsWithinACycle.add(dotNode.d);
        while (dotNode.next != null){
            sortedListOfDotsWithinACycle.add(dotNode.next.d);
            dotNode = dotNode.next;
        }

        BoardSquare[][] board = Settings.gameSettings.getBoardSquares();

        Dot d;

        Dot previousD;
        Dot nextD;
        for (int i = 0; i < sortedListOfDotsWithinACycle.size(); i++) {
            if (i != 0) previousD = sortedListOfDotsWithinACycle.get(i - 1);
            else previousD = sortedListOfDotsWithinACycle.get(sortedListOfDotsWithinACycle.size() - 1);

            if (i != sortedListOfDotsWithinACycle.size() - 1) nextD = sortedListOfDotsWithinACycle.get(i +1);
            else nextD = sortedListOfDotsWithinACycle.get(0);

            d = sortedListOfDotsWithinACycle.get(i);
            //System.out.println("current dot: " + d.toString());
            //System.out.println("previous dot: " + previousD.toString());
            //System.out.println("next dot: " + nextD.toString());
            board[d.getX()][d.getY()].addConnection(previousD);
            board[d.getX()][d.getY()].addConnection(nextD);
            board[d.getX()][d.getY()].repaint();
        }

    }

    private void updatePoints(int activePlayer){
        int activePlayerPointsCount = 0;
        for (Base b : basesOfPlayer[activePlayer]){
            activePlayerPointsCount += b.getPointsCount();
        }
        if (activePlayer == 0) Settings.gameSettings.getP1().setPoints(activePlayerPointsCount);
        else Settings.gameSettings.getP2().setPoints(activePlayerPointsCount);
    }



    public void boardTest(){
        Dot enemyDot = new Dot(11,10,1);
        addDot( enemyDot);

        addDot( new Dot(10,10,0));
        addDot( new Dot(11,11,0));
        addDot( new Dot(12,10,0));
        Dot addedDot =  new Dot(11,9,0);
        addDot( addedDot);

        Cycle newCycle = aNewCycleCreatedByDot(addedDot);
        System.out.println( newCycle == null);

        addDot( new Dot(20,20,0));
        addDot( new Dot(19,21,0));
        addDot( new Dot(18,22,0));
        addDot( new Dot(19,23,0));
        addDot( new Dot(20,23,0));
        addDot( new Dot(21,22,0));
        addedDot =  new Dot(21,21,0);
        addDot( addedDot );

        //zbedne kropki testowe
        addDot( new Dot(20,21,0));
        addDot( new Dot(20,19,0));
        addDot( new Dot(19,22,0));
        addDot( new Dot(20,21,0));
        addDot( new Dot(19,24,0));
        addDot( new Dot(20,24,0));
        newCycle = aNewCycleCreatedByDot(addedDot);
        if( newCycle != null) {
            System.out.println( "cycle found!");
            newCycle.printCycle();
        }

    }

}


