package main.java.model;

public class Settings {

    private Player p1;
    private Player p2;
    private int timer;
    private int boardSize;

    public Settings(Player p1, Player p2, int timer, int boardSize) {
        this.p1 = p1;
        this.p2 = p2;
        this.timer = timer;
        this.boardSize = boardSize;
    }

    public Settings() {
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public int getTimer() {
        return timer;
    }

    public int getBoardSize() {
        return boardSize;
    }
}
