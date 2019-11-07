package main;

import main.java.model.Player;
import main.java.model.Settings;
import main.view.MenuFrame;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run(){
                //Default settings - can be changed.
                Settings.setGameSettings(new Player(0,Color.RED,5000, "Player 1"),new Player(1,Color.BLUE,5000, "Player 2"),5000,25);
                MenuFrame menuFrame = new MenuFrame();
            }
        });
    }
}
