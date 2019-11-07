package main.java.dots;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run(){
                //Default settings - can be changed.
                Settings.setGameSettings(new Player(0,Color.RED,5000),new Player(1,Color.BLUE,5000),5000,25);
                SettingsFrame settingsFrame = new SettingsFrame();
            }
        });
    }
}
