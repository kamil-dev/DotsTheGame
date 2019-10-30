package main.java.model;

import java.util.List;

public class Enclousure {
    public static Enclousure LAST_ENCLOUSURE = null;
    private List<Dot> outerDots;
    private List<Dot> innerDots;

    private Enclousure(List<Dot> outerDots, List<Dot> innerDots) {
        this.outerDots = outerDots;
        this.innerDots = innerDots;
    }

    public static void setEnclousure(List<Dot> outerDots, List<Dot> innerDots){
        if (Enclousure.LAST_ENCLOUSURE == null) Enclousure.LAST_ENCLOUSURE = new Enclousure(outerDots, innerDots);
    }

    public List<Dot> getOuterDots() {
        return outerDots;
    }

    public List<Dot> getInnerDots() {
        return innerDots;
    }
}
