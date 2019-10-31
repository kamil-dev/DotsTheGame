package main.java.model;

import java.util.Objects;

public class Dot {
        private int x;
        private int y;
        private Player owner;
        private boolean isLocked;


    public Dot(int x, int y, Player owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        isLocked = false;
    }

    public boolean isLocked(){
        return isLocked;
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

    public void setAsLocked() {
        isLocked = true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dot dot = (Dot) o;
        return x == dot.x &&
                y == dot.y &&
                Objects.equals(owner, dot.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, owner);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") ";
    }
}
