package main.java.model;

import main.java.model.dataStructures.Dot;
import main.java.model.dataStructures.DotNode;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

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
