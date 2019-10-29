package main.java.model;

import java.awt.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return remainingTime == player.remainingTime &&
                Objects.equals(color, player.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, remainingTime);
    }
}
