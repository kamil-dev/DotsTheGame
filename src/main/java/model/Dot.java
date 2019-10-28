package main.java.model;

public class Dot {
        private int x;
        private int y;
        private Player owner;


    public Dot(int x, int y, Player owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getOwner() {
        return owner;
    }

}
