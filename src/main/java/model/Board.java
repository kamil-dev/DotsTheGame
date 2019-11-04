package main.java.model;

import main.java.model.dataStructures.*;

import java.util.*;

/*
    ==========================
     CLASS IN CONSTRUCTION!!!!!
    ==========================
 */
public class Board {
    private Dot[][] matrixOfDots;
    private int size;

    private int activePlayer = 0; //  opposing player nb is: 1 - activePlayer
    private int pointsPlayer0 = 0;
    private int pointsPlayer1 = 0;

    private Set<Cycle>[] cyclesOfPlayer = new Set[2];
    private Set<Base>[] basesOfPlayer = new Set[2];

    public Board(int size) {
        this.matrixOfDots = new Dot[size][size];
        this.cyclesOfPlayer[0] = new HashSet<>();
        this.cyclesOfPlayer[1] = new HashSet<>();
        this.basesOfPlayer[0] = new HashSet<>();
        this.basesOfPlayer[1] = new HashSet<>();
        this.size = size;
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


    // == in construction ==
    // to raczej trzeba inaczej zrobic: https://www.geeksforgeeks.org/print-all-the-cycles-in-an-undirected-graph/
    Cycle ANewCycleCreated(Dot d){
        Cycle cycle = null;
        Stack<Dot> stack = new Stack<>();
        Set<Dot> visited = new HashSet<Dot>();
        pushNeighboringDotsOnStack(stack, d, visited, activePlayer);
//        while (!stack.empty()){
//            Dot visitedDot = stack.pop();
//            if( visitedDot.equals(d))
//            {
//                cycle = new Cycle(stack);
//                if( cycle.getXmax() - cycle.getXmin() < 2 ||
//                        cycle.getYmax() - cycle.getYmin() < 2)
//                    stack = recoverStack(cycle);
//            }
//            visited.add(visitedDot);
//        }
        return cycle;
    }


    private void pushNeighboringDotsOnStack(Stack stack, Dot d, Set<Dot> visited, int activePlayer){ // assuming size >2
        int x = d.getX();
        int y = d.getY();
        if(x > 0){
            pushDotOnStack(stack,x-1,y, activePlayer, visited);
            if(y > 0)
                pushDotOnStack(stack, x - 1, y - 1, activePlayer, visited);
            if(y < size -1)
                pushDotOnStack(stack, x - 1, y + 1, activePlayer, visited);
        }
        if(x < size -1){
            pushDotOnStack(stack,x+1,y, activePlayer, visited);
            if(y > 0)
                pushDotOnStack(stack,x + 1,y - 1, activePlayer, visited);
            if(y < size -1)
                pushDotOnStack(stack,x + 1,y + 1, activePlayer, visited);
        }
        if (y > 0)
            pushDotOnStack(stack,x,y - 1, activePlayer, visited);
        if (y < size -1)
            pushDotOnStack(stack,x,y + 1, activePlayer, visited);
    }

    private boolean pushDotOnStack(Stack<Dot> stack, int x, int y, int activePlayer, Set<Dot> visited){
        Dot dot = getDot(x,y);
        if(dot == null || dot.getOwnerId()!= activePlayer || visited.contains(dot))
            return false;
        stack.push(dot);
        return true;
    }


    boolean isNewCycleABase(Cycle newCycle, Dot d, int activePlayer){
        if (newCycle.contains(d)){
            for(int x = newCycle.getXmin(); x<newCycle.getXmax() ; x++)
                for ( int y = newCycle.getYmin() ; y< newCycle.getYmax(); y++){
                    Dot dot = getDot(x,y);
                    if(dot.getOwnerId() != activePlayer && newCycle.hasInside(dot) )
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

    Cycle getAnEmptyOpponentCycleContainingDot(Dot dot, int activePlayer){
        return getCycleContainingDotFromASet(dot, cyclesOfPlayer[1-activePlayer]);
    }


    // koncepcja 1 przechowujesz puste cykle graczy
    // koncepcja 2 nie przechowujesz pustych cykli graczy

    // == fill the commented areas ==
    private Base createBase(Cycle cycle, int owner){
        Base base = new Base(cycle, this, owner);
        // Koncepcja 1 : zmien odpowiednio puste cykle graczy

        // usun zamkniete wewnatrz bazy
        // policz punkty: wersja ambitna lub (malo kosztowny) brute force: zlicz pkty z baz kazdego z graczy
        return base;
    }

    // == fill the commented areas ==
    public void addDot(Dot d){
        if(!isPlacingADotPossible(d))
            return;
        matrixOfDots[d.getX()][d.getY()] = d;
        Cycle newCycle = ANewCycleCreated(d);
        if (newCycle != null && newCycle.extendCycle().contains(d)){
            // konc1
            //      jeśli newCycle rozszerza istniejacy wczesniej cykl usun stary cykl z cyclesOfPlayer[activePlayer]
            //      cyclesOfPlayer[activePlayer].add(newCycle)
            if (isNewCycleABase(newCycle,d, activePlayer)) {
                basesOfPlayer[activePlayer].add(createBase(newCycle,  activePlayer));
                return;
                // konc1 jesli przechowujesz cykle i ich czesc jest w nowopowstalej Base shrink_cycles()
                // konc2 jesli nie przechowujesz cykli nic nie rob
            }
        }
        // konc 1:
        if( (newCycle = getAnEmptyOpponentCycleContainingDot(d, activePlayer)) != null )
            createBase(newCycle, 1 - activePlayer);
        // konc 2: znajdz wszystkie cykle przeciwnika w których się zawiera Dot d i stwórz baze z najbardziej zewnetrznego
        //       nie lubie tego rozwiazania bo czesto sie wywoluje i cos tam kosztuje.
    }

    public Dot getDot(int x, int y){
        return matrixOfDots[x][y];
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
//    public static boolean isSourrounded(List<Dot> chainOfDots, Dot d){
//        boolean isNorthBorderDot = false;
//        boolean isSouthBorderDot = false;
//        boolean isWestBorderDot = false;
//        boolean isEastBorderDot = false;
//        int x = d.getX();
//        int y = d.getY();
//
//        for (Dot chainedDot : chainOfDots){
//            if (chainedDot.getX() == x && chainedDot.getY() < y) isEastBorderDot = true;
//            if (chainedDot.getX() == x && chainedDot.getY() > y) isWestBorderDot = true;
//            if (chainedDot.getY() == y && chainedDot.getX() < x) isNorthBorderDot = true;
//            if (chainedDot.getY() == y && chainedDot.getX() > x) isSouthBorderDot = true;
//        }
//        return isEastBorderDot && isNorthBorderDot && isSouthBorderDot && isWestBorderDot;
//    }

    
}


