package main.java.model;

import main.java.model.dataStructures.*;
import main.java.view.BoardSquare;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Board {
    private Dot[][] matrixOfDots;
    private int size;
    private int dotNb = 1;
    Set<Dot> freeDotSpaces = new HashSet<>();

    private int activePlayer = 0; //  opposing player nb is: 1 - activePlayer
    private int[] pointsPlayer = new int[2];

    private Set<Cycle>[] cyclesOfPlayers = new Set[2];
    private Set<Base>[] basesOfPlayers = new Set[2];


    public Board(int size) {
        this.matrixOfDots = new Dot[size][size];
        this.cyclesOfPlayers[0] = new HashSet<>();
        this.cyclesOfPlayers[1] = new HashSet<>();
        this.basesOfPlayers[0] = new HashSet<>();
        this.basesOfPlayers[1] = new HashSet<>();
        this.size = size;
        for(int i = 0; i<size ; i++){
            for(int j = 0; j<size; j++){
                freeDotSpaces.add(new Dot(i,j,-1));
            }
        }
        //this.freeDotSpacesCount = size * size;
        this.activePlayer = 0;
    }

    public void addDot(int x, int y){
        addDot(new Dot(x,y,activePlayer));
    }

    public void addDot(Dot d){
        if(!isPlacingADotPossible(d))
            return;
        matrixOfDots[d.getX()][d.getY()] = d;
        this.freeDotSpaces.remove(d);
        this.dotNb++;
        boolean baseCreated = false;
        Cycle newCycle = aNewCycleCreatedByDot(d);
        if (newCycle != null) {
            extendCycle(newCycle);
            System.out.println("extended cycle:");
            newCycle.printCycle();
            shrinkCycleToBordersWithBases(newCycle);
            System.out.println("shrank to bases:");
            newCycle.printCycle();
            if (newCycle.contains(d)) {
                replaceOldCyclesWithANewOne(newCycle, activePlayer);
                if (isBase(newCycle, d, activePlayer)) {
                    Base base = createBase(newCycle, activePlayer);
                    basesOfPlayers[activePlayer].add(base);
                    drawBase(base);
                    baseCreated = true;
                }
                newCycle = aSecondCycleCreatedByDot(d, newCycle);        // case of the second cycle created by dot
                if (newCycle != null) {
                    extendCycle(newCycle);
                    shrinkCycleToBordersWithBases(newCycle);
                    if (newCycle.contains(d)) {
                        replaceOldCyclesWithANewOne(newCycle, activePlayer);
                        if (isBase(newCycle, d, activePlayer)) {
                            Base base = createBase(newCycle, activePlayer);
                            basesOfPlayers[activePlayer].add(base);
                            drawBase(base);
                            baseCreated = true;
                        }
                    }
                }
            }
        }
        if( !baseCreated &&
                null != (newCycle = getAnEmptyOpponentCycleContainingDot(d, activePlayer))
        ) {
            Base base = createBase(newCycle, 1 - activePlayer);
            drawBase(base);
            baseCreated = true;
        }
        if(baseCreated) {
            updatePoints();
            System.out.println("Current score:" + this.pointsPlayer[0]+ "-" + this.pointsPlayer[1]);
        }
        this.activePlayer = 1 - this.activePlayer;
    }





    public int getSize() {
        return size;
    }

    public Dot getDot(int x, int y){
        if(x< 0 || y<0 || x>=this.getSize() || y>=this.getSize())
            return null;
        return matrixOfDots[x][y];
    }
    public Dot getDot(int x, int y, int activePlayer){
        Dot d = getDot(x,y);
        if (d== null || d.getOwnerId() != activePlayer)
            return null;
        return d;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setMatrixOfDots(Dot[][] b){
        this.matrixOfDots = b;
    }




    /// A NEW CYCLE CREATED SECTION

    private Cycle aNewCycleCreatedByDot(Dot addedDot){
        int x = addedDot.getX();
        int y = addedDot.getY();
        int ownerId = addedDot.getOwnerId();
        Dot[] path = new Dot[(dotNb+1)/2+8];
        AtomicInteger pathIndex = new AtomicInteger(0);
        HashSet<Dot> visited = new HashSet<>();
        if (findCyclePath(addedDot,addedDot, x,x,y,y, path, pathIndex, visited, ownerId)){ // find path from addedDot to addedDot i.e. a cycle
            System.out.println("cycle found!");
            for (int i = 0; i< pathIndex.get(); i++){
                Dot ddd = path[i];
                System.out.println(""+ddd.getX() +", " + ddd.getY());
            }
            System.out.println("end of cycle");
            return new Cycle(this, ownerId, path, pathIndex);
        }
        return null;
    }

    private Cycle aSecondCycleCreatedByDot(Dot addedDot, Cycle firstCycle){
        int x = addedDot.getX();
        int y = addedDot.getY();
        int ownerId = addedDot.getOwnerId();
        Dot[] path = new Dot[(dotNb+3)/2];
        AtomicInteger pathIndex = new AtomicInteger(0);
        HashSet<Dot> visited = new HashSet<>();
        visited.addAll(firstCycle.getDotsSet());
        visited.remove(addedDot);
        if (findCyclePath(addedDot,addedDot, x,x,y,y, path, pathIndex, visited, ownerId)){  // find path from addedDot to addedDot i.e. a cycle
            System.out.println("second cycle found!");
            for (int i = 0; i< pathIndex.get(); i++){
                Dot ddd = path[i];
                System.out.println(""+ddd.getX() +", " + ddd.getY());
            }
            return new Cycle(this, ownerId, path,pathIndex);
        }
        return null;
    }

    /* Method finds path from currentDot to toDot
       path is stored in path[]
       where path[0] is a starting dot
       path[pathIndex.get()-1] is the last dot on path (to Dot)
     */
    private boolean findCyclePath(Dot currentDot, Dot toDot, int xmin, int xmax, int ymin, int ymax,
                                  Dot[] path, AtomicInteger pathIndex, Set<Dot> visited, int ownerId){
        //System.out.println(" " +currentDot.getX() +" " + currentDot.getY());

        if ( currentDot.equals(toDot)) {
            if (pathIndex.get()!= 0) {        // check for not allowing a neighbor to come back to toDot at start
                if (xmax - xmin < 2 && ymax - ymin < 2)
                    return false;
                return true;
            }
        }
        else
            visited.add(currentDot);

        path[pathIndex.get()] = currentDot;
        pathIndex.set(pathIndex.get() + 1);

        if(applyFindCyclePathToNeighboringDots(currentDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited, ownerId))
            return true;

        // if path to toDot through currentDot (considering previous Dots on path) does not exist
        // Remove current Dot from path and mark it as unvisited
        if(visited.remove(currentDot))
            pathIndex.set(pathIndex.get() -1);
        return false;
    }

    private boolean applyFindCyclePathToNeighboringDots(Dot currentDot, Dot toDot, int xmin, int xmax, int ymin, int ymax,
                                                        Dot[] path, AtomicInteger pathIndex, Set<Dot> visited, int ownerId){
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

        Dot neighbouringDot;
        for (int i=x-1; i<=x+1; i++){
            for(int j = y-1; j<= y+1 ; j++){
                if( i == x && j == y)
                    continue;
                neighbouringDot = getDot(i,j,ownerId);
                if( isAvailable(neighbouringDot,visited) && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited, ownerId) )
                    return true;
            }
        }
        return false;
    }

    /// A NEW CYCLE CREATED SECTION


    /// EXTEND CYCLE SECTION

    private void extendCycle(Cycle cycle){
        DotNode dn = cycle.getDotNode();
        int ownerId = cycle.getDotNode().d.getOwnerId();
        while (dn != null){
            Dot[] path = new Dot[(dotNb+1)/2]; // size == nb of player's dots
            AtomicInteger pathIndex = new AtomicInteger(0);
            HashSet<Dot> visited = new HashSet<>();
            if(findPathExtendingCycle(cycle, dn.d, path, pathIndex, visited, ownerId)) {
                System.out.println(" From node:" + path[0].getX()+"'" + path[0].getY());
                System.out.println(" Replacement Path:");
                for(int i = 0 ; i < pathIndex.get(); i++){
                    Dot d = path[i];
                    System.out.println("x:" + d.getX()+" y:" + d.getY());
                }
                Dot lastDot = path[pathIndex.get()-1];
                LinkedList<Dot> oldPath = cycle.replacePath(dn, path, pathIndex.get());
                if(oldPath.size() > 0) {
                    boolean hasInsideAnyOfOldDots = false;
                    for(Dot d: oldPath){
                        if (cycle.hasInside(d))
                        {
                            hasInsideAnyOfOldDots = true;
                            break;
                        }
                    }
                    if(!hasInsideAnyOfOldDots){
                        DotNode lastDn = cycle.findDnWithDot(lastDot);
                        if(lastDn.next == null)
                            while (!oldPath.isEmpty()){
                                lastDn = lastDn.next = new DotNode(oldPath.removeLast(),null);
                            }
                    }
                }
                dn = cycle.getDotNode();
            }
            else
                dn = dn.next;
        }
    }

    private boolean findPathExtendingCycle(Cycle cycle, Dot startingDot, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited, int ownerId){
        visited.add(startingDot);
        path[pathIndex.get()] = startingDot;
        pathIndex.set(pathIndex.get() + 1);
        if(applyFindPathToCycleToNeighboringDotsOutsideCycle(cycle,startingDot,path, pathIndex, visited, ownerId))
            return true;

        return false;
    }

    private boolean applyFindPathToCycleToNeighboringDotsOutsideCycle(Cycle cycle, Dot startingDot, Dot[] path, AtomicInteger pathIndex,
                                                                      Set<Dot> visited, int ownerId){
        int x = startingDot.getX();
        int y = startingDot.getY();

        Dot neighbouringDot;
        for (int i=x-1; i<=x+1; i++){
            for(int j = y-1; j<= y+1 ; j++){
                if( i == x && j == y)
                    continue;
                neighbouringDot = getDot(i,j, ownerId);
                if( isAvailableForOutsidePath(startingDot, neighbouringDot, startingDot, cycle, visited, pathIndex.get(), ownerId)
                        && findPathToCycle(neighbouringDot, startingDot, cycle, path, pathIndex, visited, ownerId) )
                    return true;
            }
        }
        return false;
    }

    private boolean findPathToCycle(Dot currentDot, Dot fromDot,Cycle toCycle,Dot[] path, AtomicInteger pathIndex,
                                    Set<Dot> visited, int ownerId){
        visited.add(currentDot);
        path[pathIndex.get()] = currentDot;
        pathIndex.set(pathIndex.get() + 1);

        if ( toCycle.contains(currentDot))
            return true;

        if(applyFindPathToCycleViaNeighboringDots(currentDot, fromDot, toCycle, path, pathIndex, visited, ownerId))
            return true;

        pathIndex.set(pathIndex.get() -1);
        visited.remove(currentDot);
        return false;
    }

    private boolean applyFindPathToCycleViaNeighboringDots(Dot currentDot, Dot fromDot, Cycle toCycle, Dot[] path, AtomicInteger pathIndex,
                                                           Set<Dot> visited, int ownerId){
        int x = currentDot.getX();
        int y = currentDot.getY();

        Dot neighbouringDot;
        for (int i=x-1; i<=x+1; i++){
            for(int j = y-1; j<= y+1 ; j++){
                if( i == x && j == y)
                    continue;
                neighbouringDot = getDot(i,j, ownerId);
                if( isAvailableForOutsidePath(currentDot, neighbouringDot, fromDot, toCycle, visited, pathIndex.get(), ownerId)
                        && findPathToCycle(neighbouringDot, fromDot, toCycle, path, pathIndex, visited, ownerId) )
                    return true;
            }
        }
        return false;
    }

    /// EXTEND CYCLE SECTION


    /// SHRINK CYCLE TO BORDERS WITH BASES SECTION

    private void shrinkCycleToBordersWithBases(Cycle cycle){
        DotNode dn = cycle.getDotNode();
        List<Base> basesContainingD;
        int ownerId = cycle.getDotNode().d.getOwnerId();
        Set<Base> bases = new HashSet<>(basesOfPlayers[ownerId]);

        while (dn != null){
            basesContainingD = getBasesFromASetContainingDot(dn.d, bases);
            //System.out.println("Bases of player" + activePlayer + " count:" + bases.size());
            boolean baseDeleted = false;
            for(Base base: basesContainingD){
                if(base != null && dn.next!=null && cycle.doesContainACycle(base.getCycle())) {
                    baseDeleted = true;
//                    System.out.println("player"+activePlayer+" cut base:");
//                    base.getCycle().printCycle();
//                    for(Dot d : base.getCycle().getDotsSet())
//                        System.out.println("d"+d.getX()+" " +d .getY());
//                    System.out.println("from cycle:");
//                    cycle.printCycle();
                    cycle.cutBase(dn, base);
//                    System.out.println("Cycle after cut:");
//                    cycle.printCycle();
//                    for(Dot d : cycle.getDotsSet())
//                        System.out.println("d.x:" + d.getX()+" d.y:"+d.getY());
                    bases.remove(base); // to nie koniecznie musi byc zawsze poprawne
                }
            }
            if(!baseDeleted)
                dn = dn.next;
        }
        cycle.recomputeMinAndMaxCoordinatesAndResetDotsSet();
    }

    /// SHRINK CYCLE TO BORDERS WITH BASES SECTION

    ///

    private void replaceOldCyclesWithANewOne(Cycle newCycle, int ownerId){
        Set<Cycle> cyclesOfActivePlayer = cyclesOfPlayers[ownerId];
        Set<Cycle> cyclesToRemove = new HashSet<>();
        for(Cycle c: cyclesOfActivePlayer) {
            if (newCycle.doesOverlapWithCycleOrDoesContainIt(c))
                cyclesToRemove.add(c);
        }
        cyclesOfActivePlayer.removeAll(cyclesToRemove);
        cyclesOfActivePlayer.add(newCycle);
    }

    ///

    /// CREATE BASE

    private Base createBase(Cycle cycle, int baseOwner) {
        System.out.println("Base created!");
        cycle.printCycle();
        cyclesOfPlayers[activePlayer].remove(cycle);
        Base newBase = new Base(cycle, this, baseOwner);
        for (int playerId = 0; playerId < 2; playerId++) {                     // delete surrounded Bases and Cycles
            Set<Cycle> cyclesOfPlayer = cyclesOfPlayers[playerId];
            Set<Cycle> cyclesToRemove = new HashSet<>();
            for (Cycle c : cyclesOfPlayer) {
                if (cycle.doesContainACycle(c))
                    cyclesToRemove.add(c);
                else if (cycle.doesOverlapWithCycle(c)) {
                    if (shrinkCycleIfItWasReduced(c, cycle) == null)
                        cyclesToRemove.add(c);
                }
            }
            cyclesOfPlayer.removeAll(cyclesToRemove);
            Set<Base> basesOfPlayer = basesOfPlayers[playerId];
            Set<Base> basesToRemove = new HashSet<>();
            for (Base base : basesOfPlayer){
                if(newBase.doesContainACycle(base.getCycle()))
                    basesToRemove.add(base);
            }
            basesOfPlayer.removeAll(basesToRemove);
        }
        int pointsCount = 0;
        for (int x = cycle.getXmin() + 1; x < cycle.getXmax(); x++)
            for (int y = cycle.getYmin() + 1; y < cycle.getYmax(); y++) {
                Dot d = getDot(x, y);
                if (d == null && newBase.hasInside(d = new Dot(x, y, -1))){
                    freeDotSpaces.remove(d);
                }
                else if(cycle.hasInside(d)) {
                    d.markAsInsideBase();
                    if (d.getOwnerId() == baseOwner)
                        pointsCount--;
                    else if(d.getOwnerId() == 1 - baseOwner)
                        pointsCount++;
                }
            }
        newBase.setPointsCount(pointsCount);
        basesOfPlayers[activePlayer].add(newBase);
        System.out.println( "playerBaseCount: " + basesOfPlayers[baseOwner].size());
        // policz punkty: wersja ambitna lub (malo kosztowny) brute force: zlicz pkty z baz kazdego z graczy
        System.out.println(""+ freeDotSpaces.size() +" remain free");
        return newBase;
    }

    private Cycle shrinkCycleIfItWasReduced(Cycle cycleToBeShrinked, Cycle base){
        Cycle newCycle = null;
        for(Dot d : cycleToBeShrinked.getDotsSet()){
            if (base.hasOutside(d))
                if( null != (newCycle = aNewCycleCreatedByDot(d)) )
                    break;
        }
        if(newCycle != null) {
            extendCycle(newCycle);
            shrinkCycleToBordersWithBases(newCycle);
        }
        return newCycle;
    }

    /// CREATE BASE


    /// AUXILIARY METHODS

    private boolean isPlacingADotPossible(Dot d){       // checking if d is a border of a cycle is not needed
        if(matrixOfDots[d.getX()][d.getY()] != null)
            return false;
        for (Base b: basesOfPlayers[1-activePlayer])
            if ( b.hasInside(d))
                return false;
        for (Base b: basesOfPlayers[activePlayer])
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

    private boolean isAvailableForOutsidePath(Dot prevDot, Dot neighbouringDot, Dot fromDot, Cycle cycle, Set<Dot> visited, int pathInd, int ownerId){
        if( !isAvailableExcludingFromDot(neighbouringDot, fromDot, visited)
                || (pathInd == 1 && cycle.contains(neighbouringDot))
                || cycle.hasInside(neighbouringDot))
            return false;
        int xPrev = prevDot.getX();
        int yPrev = prevDot.getY();
        int xNeib = neighbouringDot.getX();
        int yNeib = neighbouringDot.getY();
        int pxGreater = xPrev - xNeib;
        int pyGreater = yPrev - yNeib;
        if( Math.abs(pxGreater) + Math.abs(pyGreater) >= 2){
            Dot crossPoint1 = pxGreater > 0 ? getDot(xPrev - 1, yPrev, ownerId) : getDot(xPrev + 1, yPrev, ownerId);
            Dot crossPoint2 = pyGreater > 0 ? getDot(xPrev, yPrev - 1, ownerId) : getDot(xPrev, yPrev + 1, ownerId);
            if(crossPoint1 != null && crossPoint2 != null && cycle.contains(crossPoint1) && cycle.contains(crossPoint2)){
                DotNode crossPoint1Dn = cycle.findDnWithDot(crossPoint1);
                DotNode crossPoint2Dn = cycle.findDnWithDot(crossPoint2);
                if(crossPoint1Dn.next == crossPoint2Dn || crossPoint2Dn.next == crossPoint1Dn)
                    return false;
            }
        }
        return true;
    }


    private List<Base> getBasesFromASetContainingDot(Dot d, Set<Base> bases){
        LinkedList<Base> basesList = new LinkedList<>();
        for (Base b : bases){
            if (b.contains(d))
                basesList.add(b);
        }
        return basesList;
    }

    private boolean isBase(Cycle newCycle, Dot d, int activePlayer){
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
        for (Base b : basesOfPlayers[activePlayer]) {
            if(b.contains(d))
                return b;
        }
        return null;
    }

    private Cycle getAnEmptyOpponentCycleContainingDot(Dot dot, int activePlayer){
        return getCycleFromASetHavingADotInside(dot, cyclesOfPlayers[1-activePlayer]);
    }

    private void updatePoints(){
        for(int playerId = 0; playerId<2; playerId++){
            int playerPointCount = 0;
            for(Base base : basesOfPlayers[playerId]){
                playerPointCount += base.getPointsCount();
            }
            this.pointsPlayer[playerId] = playerPointCount;
        }
        Settings.GAME_SETTINGS.getP1().setPoints(this.pointsPlayer[0]);
        Settings.GAME_SETTINGS.getP2().setPoints(this.pointsPlayer[1]);
    }

    /// AUXILIARY METHODS




    private static void drawBase(Base base){
        Cycle cycleToDraw = base.getCycle();
        DotNode dotNode = cycleToDraw.getDotNode();
        List<Dot> sortedListOfDotsWithinACycle = new ArrayList<>();
        sortedListOfDotsWithinACycle.add(dotNode.d);
        while (dotNode.next != null){
            sortedListOfDotsWithinACycle.add(dotNode.next.d);
            dotNode = dotNode.next;
        }

        BoardSquare[][] board = Settings.GAME_SETTINGS.getBoardSquares();

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


//
//    public void boardTest(){
//        Dot enemyDot = new Dot(11,10,1);
//        addDot( enemyDot);
//
//        addDot( new Dot(10,10,0));
//        addDot( new Dot(11,11,0));
//        addDot( new Dot(12,10,0));
//        Dot addedDot =  new Dot(11,9,0);
//        addDot( addedDot);
//
//        Cycle newCycle = aNewCycleCreatedByDot(addedDot);
//        System.out.println( newCycle == null);
//
//        addDot( new Dot(20,20,0));
//        addDot( new Dot(19,21,0));
//        addDot( new Dot(18,22,0));
//        addDot( new Dot(19,23,0));
//        addDot( new Dot(20,23,0));
//        addDot( new Dot(21,22,0));
//        addedDot =  new Dot(21,21,0);
//        addDot( addedDot );
//
//        //zbedne kropki testowe
//        addDot( new Dot(20,21,0));
//        addDot( new Dot(20,19,0));
//        addDot( new Dot(19,22,0));
//        addDot( new Dot(20,21,0));
//        addDot( new Dot(19,24,0));
//        addDot( new Dot(20,24,0));
//        newCycle = aNewCycleCreatedByDot(addedDot);
//        if( newCycle != null) {
//            System.out.println( "cycle found!");
//            newCycle.printCycle();
//        }
//
//    }

}


