package main.java.model;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run(){
                SettingsFrame settingsFrame = new SettingsFrame();

            }
        });
    }
}
