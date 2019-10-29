package main.java.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Board {
    private Dot[][] matrixOfDots;

    public Board(int size) {
        this.matrixOfDots = new Dot[size][size];
    }

    public void addDot(Dot d){
        matrixOfDots[d.getX()][d.getY()] = d;
        if (isClousurePossible(d)) {
            Stack<Dot> stackOfChainedDots = new Stack<>();
            stackOfChainedDots.push(d);
            stackOfChainedDots = findClousure(stackOfChainedDots, d);
            if (stackOfChainedDots != null){
                System.out.println("Yes we can");
            }
        }
    }

    public void setMatrixOfDots(Dot[][] b){
        this.matrixOfDots = b;
    }


    private boolean isClousurePossible(Dot d){
        List<Dot> adjacentDots = this.findAdjacent(d);
        return !(adjacentDots.size() < 2 || areAdjacent(adjacentDots.get(0),adjacentDots.get(1))) ;
    }

    private static boolean areAdjacent(Dot dot1, Dot dot2){
        return (Math.abs(dot1.getX()-dot2.getX()) <= 1 && Math.abs(dot1.getY()-dot2.getX()) <= 1);
    }

    private List<Dot> findAdjacent(Dot d){
        List<Dot> adjacentDots = new LinkedList<>();
        int x = d.getX();
        int y = d.getY();
        for (int i = (x == 0) ? 0 : x - 1; i <= ((x == matrixOfDots.length - 1) ? x : x + 1 ) ; i++) {
            for (int j = (y == 0) ? 0 : y - 1; j <= ((y == matrixOfDots.length - 1) ? y : y + 1 ) ; j++) {
                if (!(i == x && j == y) && matrixOfDots[i][j] != null) {
                    if (matrixOfDots[i][j].getOwner().equals(d.getOwner()))
                        adjacentDots.add(matrixOfDots[i][j]);
                }
            }
        }
        return adjacentDots;
    }

    public Stack<Dot> findClousure(Stack<Dot> listOfChainedDots, Dot lastDot){
        if (listOfChainedDots.isEmpty()) return null;
        if (listOfChainedDots.size() > 4 && listOfChainedDots.get(0).equals(listOfChainedDots.peek())){
            List<Dot> opponentsLockedDots = listOfOpponentsLockedDots(listOfChainedDots);
            if (opponentsLockedDots != null) return listOfChainedDots;
        }
        List<Dot> adjacentToTheLast = this.findAdjacent(lastDot);
        if (this.findAdjacent(lastDot).size() < 2){
            listOfChainedDots.pop();
            findClousure(listOfChainedDots,listOfChainedDots.peek());
        } else {
            for (Dot d : adjacentToTheLast){
                if (!(listOfChainedDots.contains(d) && !d.equals(listOfChainedDots.get(0)))) {
                    listOfChainedDots.push(d);
                    findClousure(listOfChainedDots,d);
                }
            }
        }
        return null;
    }

    public List<Dot> listOfOpponentsLockedDots(List<Dot> chainOfDots){
        List<Dot> lockedDots = new LinkedList<>();
        int minX = chainOfDots.get(0).getX();
        int maxX = chainOfDots.get(0).getX();
        int minY = chainOfDots.get(0).getY();
        int maxY = chainOfDots.get(0).getY();
        for (int i = 1; i < chainOfDots.size() ; i++) {
            minX = Math.min(minX,chainOfDots.get(i).getX());
            maxX = Math.max(maxX,chainOfDots.get(i).getX());
            minY = Math.min(minY,chainOfDots.get(i).getY());
            maxY = Math.max(maxY,chainOfDots.get(i).getY());
        }

        if (maxX - minX < 2 || maxY - minY < 2) return null;
        for (int i = minX+1; i < maxX; i++) {
            for (int j = minY+1; j < maxY ; j++) {
                if (!matrixOfDots[i][j].getOwner().equals(chainOfDots.get(0).getOwner()))
                    if (isLocked(chainOfDots, matrixOfDots[i][j])) lockedDots.add(matrixOfDots[i][j]);
            }
        }

        return lockedDots;
    }

    public static boolean isLocked(List<Dot> chainOfDots, Dot d){
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

}


