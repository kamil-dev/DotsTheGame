
package main.java.model;

import main.java.view.MenuFrame;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run(){
                //Default settings - can be changed.
                Settings.setGameSettings(new Player(0,Color.RED,3660, "Player 1",true),
                        new Player(1,Color.BLUE,3660, "Player 2",false),5000,25);
                MenuFrame menuFrame = new MenuFrame();
            }
        });
    }
}
