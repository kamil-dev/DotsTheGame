package main.java.model;

import main.view.BoardFrame;
import main.view.LoadGameFrame;
import main.view.MenuFrame;
import main.view.SettingsFrame;

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
        } else if (label.getText().trim().equals("Settings")){
            frame.setVisible(false);
            SettingsFrame settingsFrame = new SettingsFrame();
        } else if (label.getText().trim().equals("Load Game")){
            frame.setVisible(false);
            LoadGameFrame settingsFrame = new LoadGameFrame();
        } else if (label.getText().trim().equals("Cancel")){
            frame.setVisible(false);
            MenuFrame menuFrame = new MenuFrame();
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
