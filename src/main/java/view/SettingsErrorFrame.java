package main.java.view;

import main.java.model.Settings;

import javax.swing.*;
import java.awt.*;

public class SettingsErrorFrame extends JFrame {
    private int width = 300;
    private int height = 225;

    public SettingsErrorFrame(){
        setSize(width,height);
        setLocation(400,400);
        setIconImage(new ImageIcon("src/main/resources/1320183166943884936_128.png").getImage());
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        setTitle("ERROR");
        this.createPanel();

    }

    private void createPanel(){
        JTextPane textPane = new JTextPane();
        textPane.setText("Incorrect settings!\n" +
                "Please make sure that:\n" +
                "1. Both player's names are not empty\n" +
                "2. Players picked colors are not the same\n" +
                "3. Board size is an integer between 10 and 40\n" +
                "4. Timer is higher than 0");
        textPane.setBackground(Settings.gameSettings.getGlobalTheme().getBackgroundColor());
        textPane.setForeground(Settings.gameSettings.getGlobalTheme().getForegroundColor());
        textPane.setEditable(false);
        add(textPane, BorderLayout.CENTER);
    }
}
