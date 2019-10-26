package main.java.model;

import java.awt.*;

public class Player {
    private Color color;
    private int remainingTime;

    public Player(Color color, int remainingTime) {
        this.color = color;
        this.remainingTime = remainingTime;
    }

    public Color getColor() {
        return color;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}
