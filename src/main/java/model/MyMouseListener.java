package main.java.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener {
    private JLabel label;
    private JFrame frame;

    public MyMouseListener(JLabel label, JFrame frame){
        this.label = label;
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (label.getText().trim().equals("Play")){
            frame.setVisible(false);
            BoardFrame boardFrame = new BoardFrame();

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        label.setFont(new Font("Arial",Font.BOLD,22));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        label.setFont(new Font("Arial",Font.BOLD,20));
    }
}
