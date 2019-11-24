package main.java.model;

import main.java.model.dataStructures.*;
import main.java.view.BoardSquare;
import main.java.view.EndFrame;
import main.java.view.EndGameCause;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Board implements Serializable {
    private Dot[][] matrixOfDots;
    private int size;
    private int dotNb = 1;
    private Set<Dot> freeDotSpaces = new HashSet<>();

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
        System.out.println("Added dot:"+ d.getX()+","+d.getY());
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

    public Set<Dot> getFreeDotSpaces() {
        return freeDotSpaces;
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
        Dot[] path = new Dot[(dotNb+3)/2];
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
                if( isNeighborAvailableAsPath(currentDot, neighbouringDot, path, pathIndex, ownerId, visited)
                        && findCyclePath(neighbouringDot, toDot, xmin, xmax, ymin, ymax, path, pathIndex, visited, ownerId) )
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
                LinkedList<DotNode> oldPath = cycle.replacePath(dn, path, pathIndex.get());
                if(oldPath.size() > 0) {
                    boolean hasInsideAnyOfOldDots = false;
                    for(DotNode oldPathDn: oldPath){
                        if (cycle.hasInside(oldPathDn.d))
                        {
                            hasInsideAnyOfOldDots = true;
                            break;
                        }
                    }
                    if(!hasInsideAnyOfOldDots){
                        DotNode lastDn = cycle.findDnWithDot(lastDot);
//                        dn.next = oldPath.getFirst().next;
//                        //if(lastDn.next == null)
                        while ( oldPath.size()>0){
                            lastDn = lastDn.next = new DotNode(oldPath.removeLast().d,null);
                        }
                        cycle.recomputeMinAndMaxCoordinatesAndResetDotsSet();
                    }
                }
                dn = cycle.getDotNode();
            }
            else
                dn = dn.next;
        }
        cycle.recomputeMinAndMaxCoordinatesAndResetDotsSet();
    }

    private boolean findPathExtendingCycle(Cycle cycle, Dot startingDot, Dot[] path, AtomicInteger pathIndex, Set<Dot> visited, int ownerId){
        visited.add(startingDot);
        path[pathIndex.get()] = startingDot;
        pathIndex.set(1);
        if(apply_FindPathToCycle_To_NeighboringDotsOutsideCycle(cycle, startingDot, path, pathIndex, visited, ownerId))
            return true;

        return false;
    }

    private boolean apply_FindPathToCycle_To_NeighboringDotsOutsideCycle(Cycle cycle, Dot startingDot, Dot[] path, AtomicInteger pathIndex,
                                                                         Set<Dot> visited, int ownerId){
        int x = startingDot.getX();
        int y = startingDot.getY();

        Dot neighbouringDot;
        for (int i=x-1; i<=x+1; i++){
            for(int j = y-1; j<= y+1 ; j++){
                if( i == x && j == y)
                    continue;
                neighbouringDot = getDot(i,j, ownerId);
                if( isNeighborAvailableAsOutsidePath(startingDot, neighbouringDot, startingDot, cycle, visited, pathIndex.get(), ownerId)
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
                if( isNeighborAvailableAsOutsidePath(currentDot, neighbouringDot, fromDot, toCycle, visited, pathIndex.get(), ownerId)
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
                if(base != null && dn.next!=null && base.contains(dn.next.d) && cycle.doesContainACycle(base.getCycle())) {
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
                    Settings.gameSettings.getBoardSquares()[d.getX()][d.getY()].setState(3);
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
        basesOfPlayers[baseOwner].add(newBase);
        System.out.println( "playerBaseCount: " + basesOfPlayers[baseOwner].size());
        //for(int i = 0; i<basesOfPlayers)
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

    private boolean isNeighborAvailableAsPath(Dot prevDot, Dot neighbouringDot, Dot[] path, AtomicInteger pathIndex, int ownerId, Set<Dot> visited){
        if(!isAvailable_NecessaryCondition(neighbouringDot, visited))
            return false;
        return doesntCutAnEdge(prevDot, neighbouringDot, path, pathIndex, ownerId);
    }

    private boolean isAvailable_NecessaryCondition(Dot neighbouringDot, Set<Dot> visited){
        return neighbouringDot!= null
                && !neighbouringDot.isInsideBase()
                && !visited.contains(neighbouringDot);
    }

    private boolean isNeighborAvailableAsOutsidePath(Dot prevDot, Dot neighbouringDot, Dot fromDot, Cycle cycle, Set<Dot> visited, int pathInd, int ownerId){
        if( !isAvailable_NecessaryCondition(neighbouringDot,visited)
                || neighbouringDot.equals(fromDot)
                || ( pathInd == 1 && cycle.contains(neighbouringDot) )
                || cycle.hasInside(neighbouringDot))
            return false;
        return doesntCutAnEdge(prevDot,neighbouringDot,cycle,ownerId); // tu też powinno sprawdzac path[]
    }

    private boolean doesntCutAnEdge(Dot prevDot, Dot neighbouringDot, Cycle cycle, int ownerId){
        return doesntCutTheBaseBorder(prevDot,neighbouringDot,ownerId)
                && doesntCutCurrentPath(prevDot, neighbouringDot, cycle, ownerId);
    }

    private boolean doesntCutAnEdge(Dot prevDot, Dot neighbouringDot, Dot[] path, AtomicInteger pathIndex, int ownerId){
        return doesntCutTheBaseBorder(prevDot,neighbouringDot,ownerId)
                && doesntCutCurrentPath(prevDot, neighbouringDot, path, pathIndex, ownerId);
    }

    private boolean doesntCutTheBaseBorder(Dot prevDot, Dot dot, int ownerId){
        for(Base base : basesOfPlayers[ownerId]){
            if (base.contains(prevDot) && base.contains(dot)){
                if(!areNeighborsInBase(prevDot,dot,base))
                    return false;
            }
        }
        return true;
    }

    private boolean areNeighborsInBase(Dot prevDot, Dot dot, Base base){
        DotNode prevDn = base.getDotNodeWithDot(prevDot);
        DotNode dotDn = base.getDotNodeWithDot(dot);
        return base.getCycle().areNeighbours(prevDn,dotDn);
    }

    private boolean doesntCutCurrentPath(Dot prevDot, Dot neighbouringDot, Dot[] path, AtomicInteger pathIndex, int ownerId){
        Dot[] crossPoints = getCrossPoints(prevDot,neighbouringDot,ownerId);
        if(crossPoints != null){
            for (int i = 0; i< pathIndex.get(); i++ ){
                if(path[i].equals(crossPoints[0])){
                    if(i+1 < pathIndex.get() && path[i+1].equals(crossPoints[1]))
                        return false;
                    break;
                }
                if(path[i].equals(crossPoints[1])){
                    if(i+1 < pathIndex.get() && path[i+1].equals(crossPoints[0]))
                        return false;
                    break;
                }
            }
        }
        return true;
    }

    private Dot[] getCrossPoints(Dot prevDot, Dot neighbouringDot, int ownerId){
        int xPrev = prevDot.getX();
        int yPrev = prevDot.getY();
        int xNeib = neighbouringDot.getX();
        int yNeib = neighbouringDot.getY();
        int pxGreater = xPrev - xNeib;
        int pyGreater = yPrev - yNeib;
        if( Math.abs(pxGreater) + Math.abs(pyGreater) >= 2){
            Dot crossPoint1 = pxGreater > 0 ? getDot(xPrev - 1, yPrev, ownerId) : getDot(xPrev + 1, yPrev, ownerId);
            Dot crossPoint2 = pyGreater > 0 ? getDot(xPrev, yPrev - 1, ownerId) : getDot(xPrev, yPrev + 1, ownerId);
            if (crossPoint1 == null || crossPoint2 == null)
                return null;
            Dot[] crossPoints = {crossPoint1,crossPoint2};
            return  crossPoints;
        }
        return null;
    }

    private boolean doesntCutCurrentPath(Dot prevDot, Dot neighbouringDot, Cycle cycle, int ownerId){
        Dot[] crossPoints = getCrossPoints(prevDot,neighbouringDot,ownerId);
        if(crossPoints != null){
            if(cycle.contains(crossPoints[0]) && cycle.contains(crossPoints[1])){
                DotNode crossPoint1Dn = cycle.findDnWithDot(crossPoints[0]);
                DotNode crossPoint2Dn = cycle.findDnWithDot(crossPoints[1]);
                if(cycle.getNext(crossPoint1Dn) == crossPoint2Dn || cycle.getNext(crossPoint2Dn) == crossPoint1Dn)
                    return false;
            }
        }
        return true;
    }

    private int findDotIndInPath(Dot d, Dot[] path, AtomicInteger pathIndex){

        return -1;
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
        Settings.gameSettings.getP1().setPoints(this.pointsPlayer[0]);
        Settings.gameSettings.getP2().setPoints(this.pointsPlayer[1]);
        System.out.println("Update activated");
        System.out.println(Settings.gameSettings.getP1().getPoints() + " : " + Settings.gameSettings.getP2().getPoints());

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
}


