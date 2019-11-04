package main.java.model.dataStructures;

public class Dot {
        private int x;
        private int y;
        private int hashCode;
        private int ownerId;
        private boolean isInsideBase;


    public Dot(int x, int y, int ownerId) {
        this.x = x;
        this.y = y;
        this.hashCode = this.x *1000 + this.y;
        this.ownerId = ownerId;
        isInsideBase = false;
    }

    public boolean isInsideBase(){
        return isInsideBase;
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

    public void setAsLocked() {
        isInsideBase = true;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return this.hashCode() == o.hashCode();
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
