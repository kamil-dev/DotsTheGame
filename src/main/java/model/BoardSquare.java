package main.java.model;

import javax.swing.*;
import java.awt.*;

public class BoardSquare extends JPanel {
    private int column;
    private int row;
    private Settings settings;
    private int state = 0; // 0 - for no dot, 1 - for player's one dot, 2 - for player's two dot;

    public BoardSquare(int row, int column) {
        this.row = row;
        this.column = column;
        this.settings = Settings.GAME_SETTINGS;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = super.getWidth();
        int height = super.getHeight();

        g.setColor(new Color(255, 255, 204));
        g.fillRect(0,0,width,height);
        g.setColor(Color.BLACK);
        g.drawLine(width/2,0,width/2,height);
        g.drawLine(0,height/2,width,height/2);

        int lastRowAndColIndex = settings.getBoardSize()-1;
        if (row == 0) g.drawLine(0,0,width,0);
        if (column == 0) g.drawLine(0,0,0,height);
        if (row == lastRowAndColIndex) g.drawLine(0,height,width,height);
        if (column == lastRowAndColIndex) g.drawLine(width,0,width,height);

        if (state == 1) {
            g.setColor(settings.getP1().getColor());
            g.fillOval(width/2-4,height/2-4,8,8);
        }
        if (state == 2) {
            g.setColor(settings.getP2().getColor());
            g.fillOval(width/2-4,height/2-4,8,8);
        }

    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void setState(int state){
        if (this.state != 0) return;
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
