package main.java.model.dataStructures;

public interface ICycle {
    boolean contains(Dot d);
    boolean hasInside(Dot d);
    boolean hasOutside(Dot d);
}
