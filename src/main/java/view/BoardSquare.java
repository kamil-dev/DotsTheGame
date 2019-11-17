package main.java.view;

import main.java.model.Settings;
import main.java.model.dataStructures.Dot;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class BoardSquare extends JPanel implements Serializable {
    private int column;
    private int row;
    private Settings settings;
    private int state = 0; // 0 - for no dot, 1 - for player's one dot, 2 - for player's two dot
    private List<Dot> connections = new LinkedList<>();

    public BoardSquare(int row, int column) {
        this.row = row;
        this.column = column;
        this.settings = Settings.gameSettings;
    }

    public void setConnections(List<Dot> connections) {
        this.connections.addAll(connections);
    }
    public void addConnection(Dot connectedDot){
        this.connections.add(connectedDot);
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

        Graphics2D graphics2D = (Graphics2D)g;
        graphics2D.setStroke(new BasicStroke(2));
        Color c;
        if (state != 0) {
            if (state == 1) c = settings.getP1().getColor(); else c = settings.getP2().getColor();

            graphics2D.setColor(c);
            graphics2D.fillOval(width/2-4,height/2-4,8,8);

            if (connections != null){
                for (Dot d : connections){
                    if (d.getX() < row && d.getY() < column) graphics2D.drawLine(0,0,width/2,height/2); //ok
                    if (d.getX() < row && d.getY() == column) graphics2D.drawLine(width/2,0,width/2,height/2);
                    if (d.getX() < row && d.getY() > column) graphics2D.drawLine(width,0,width/2,height/2);
                    if (d.getX() == row && d.getY() > column) graphics2D.drawLine(width,height/2,width/2,height/2);
                    if (d.getX() > row && d.getY() > column) graphics2D.drawLine(width,height,width/2,height/2); //ok
                    if (d.getX() > row && d.getY() == column) graphics2D.drawLine(width/2,height,width/2,height/2);
                    if (d.getX() > row && d.getY() < column) graphics2D.drawLine(0,height,width/2,height/2);
                    if (d.getX() == row && d.getY() < column) graphics2D.drawLine(0,height/2,width/2,height/2);
                }
            }
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
