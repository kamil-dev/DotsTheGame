package main.java.view;

import main.java.controller.MyMouseListener;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame extends JFrame {
    private int width = 500;
    private int height = 525;

    public SettingsFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        createPanel();
    }

    private void createPanel(){
        JPanel settingsPanel = new JPanel();
        add(settingsPanel, BorderLayout.CENTER);
        Color c1 = new Color(204, 204, 255);
        Color c2 = new Color(0, 51, 153);

        settingsPanel.setBackground(c1);
    }

}
