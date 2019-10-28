package main.java.model;

public class Board {
    private Dot[][] b;

    public Board(int size) {
        this.b = new Dot[size][size];
    }

    public void addDot(Dot d){
        b[d.getX()][d.getY()] = d;
        if (isClousurePossible(d)) {
            isClousure();
        }
    }

    private boolean isClousurePossible(Dot d){
        int x = d.getX();
        int y = d.getY();
        Dot firstMatch = null;
        Dot secondMatch = null;
        int countOfMatches = 0;
        for (int i = (x == 0) ? 0 : x - 1; i <= ((x == b.length - 1) ? x : x + 1 ) ; i++) {
            for (int j = (y == 0) ? 0 : y - 1; j <= ((y == b.length - 1) ? y : y + 1 ) ; j++) {
                if (i == x && j == y);
                else if (b[i][j] != null) {
                    if (b[i][j].getOwner().equals(d.getOwner())) {
                        if (secondMatch == null && firstMatch != null) secondMatch = b[i][j];
                        if (firstMatch == null) firstMatch = b[i][j];
                        countOfMatches++;
                        if (countOfMatches > 2) return true;
                    }
                }
            }
        }
        return (countOfMatches < 2) ? false : !areAdjacent(firstMatch,secondMatch);
    }

    private static boolean areAdjacent(Dot dot1, Dot dot2){
        return (Math.abs(dot1.getX()-dot2.getX()) <= 1 && Math.abs(dot1.getY()-dot2.getX()) <= 1);
    }

    public static boolean isClousure(){
        return false;
    }

}
