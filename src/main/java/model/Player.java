package main.java.model;

import java.awt.*;
import java.util.Objects;

public class Player {
    int id;
    private String name;
    private Color color;
    private int remainingTime;
    private int points;

    public Player(int id, Color color, int remainingTime) {
        this(id,color,remainingTime, String.valueOf(id));
    }

    public Player(int id, Color color, int remainingTime, String name) {
        this.id = id;
        this.color = color;
        this.remainingTime = remainingTime;
        this.points = 0;
        this.name = name;
    }

    public int getId() {return this.id;}

    public Color getColor() {
        return color;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getPoints() {
        return points;
    }

    public String getName(){
        return name;
    }

    public void setPoints(int points) {
        this.points = points;
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
