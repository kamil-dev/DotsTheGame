package main.java.controller;

import main.java.model.Player;
import main.java.model.Settings;
import main.java.view.*;

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
        String labelText = label.getText().trim();

        if (labelText.equals("Play")){
            frame.setVisible(false);
            BoardFrame boardFrame = new BoardFrame();
        } else if (labelText.equals("Settings")){
            frame.setVisible(false);
            SettingsFrame settingsFrame = new SettingsFrame();
        } else if (labelText.equals("Load Game")){
            frame.setVisible(false);
            LoadGameFrame settingsFrame = new LoadGameFrame();
        } else if (labelText.equals("Cancel")){
            frame.setVisible(false);
            MenuFrame menuFrame = new MenuFrame();
        } else if (labelText.equals("Load")){

        } else if (labelText.equals("Save Game")){

        } else if (labelText.equals("Resign")){

        } else if (labelText.equals("Accept")){
            SettingsFrame sf = (SettingsFrame)frame;
            Integer size = sf.getBoardSize();
            Integer timer = sf.getTimer();

            if (sf.getP1Color().equals(sf.getP2Color()) ||
                    sf.getP1Name().equals("") || sf.getP2Name().equals("") ||
                    size == null || size > 40 || size < 10 || timer == null || sf.getTimer() <= 0){
                MyErrorFrame frame = new MyErrorFrame();
            } else {
                Settings.setGameSettings(new Player(1,sf.getP1Color(), timer, sf.getP1Name()), new Player(0, sf.getP2Color(), timer, sf.getP2Name()) , timer, size);
                MenuFrame menuFrame = new MenuFrame();
            }
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