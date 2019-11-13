package main.java.view;

import main.java.controller.LoadGameListener;
import main.java.controller.NavigateMouseListener;
import main.java.model.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoadGameFrame extends JFrame {
    private int width = 500;
    private int height = 525;
    private Color backgroundColor;
    private Color foregroundColor;
    private Font font;

    public LoadGameFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        backgroundColor = Settings.gameSettings.getGlobalTheme().getBackgroundColor();
        foregroundColor = Settings.gameSettings.getGlobalTheme().getForegroundColor();
        font = Settings.gameSettings.getGlobalTheme().getFontLarge();
        createForm();
    }

    private void createForm(){
        JPanel settingsPanel = new JPanel();
        add(settingsPanel, BorderLayout.CENTER);

        settingsPanel.setBackground(backgroundColor);
        BoxLayout layout = new BoxLayout(settingsPanel,BoxLayout.Y_AXIS);
        settingsPanel.setLayout(layout);
        settingsPanel.setBorder(new EmptyBorder(new Insets(180, 150, 180, 150)));

        JLabel fileNameLabel = new JLabel();
        fileNameLabel.setText("File Name: ");
        fileNameLabel.setFont(font);
        fileNameLabel.setForeground(foregroundColor);

        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setText("Enter file name to load game");
        fileNameTextField.addActionListener(new LoadGameListener(this,fileNameTextField));

        JLabel cancelLabel = new JLabel();
        cancelLabel.setText("Cancel");
        cancelLabel.setFont(font);
        cancelLabel.addMouseListener(new NavigateMouseListener(cancelLabel,this));
        cancelLabel.setForeground(foregroundColor);

        settingsPanel.add(fileNameLabel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        settingsPanel.add(fileNameTextField);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        settingsPanel.add(cancelLabel);

        add(settingsPanel);
    }

}
