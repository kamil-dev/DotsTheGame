package main.java.model.dataStructures;

public class Dot {
    private int x;
    private int y;
    private int hashCode;
    private int ownerId;
    private boolean isInsideBase;

        // max size of a board ensuring unique hashcodes set to 1000
    public Dot(int x, int y, int ownerId) {
        this.x = x;
        this.y = y;
        this.hashCode = this.x *1000 + this.y;
        this.ownerId = ownerId;
        isInsideBase = false;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOwnerId() {
        return ownerId;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass())
            return false;
        return this.hashCode() == o.hashCode();
    }

    public boolean isInsideBase(){
        return isInsideBase;
    }

    public void markAsInsideBase(){
        this.isInsideBase = true;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") ";
    }
}
