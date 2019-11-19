package main.java.view;

import main.java.controller.SaveGameListener;
import main.java.model.Settings;

import javax.swing.*;
import java.awt.*;

public class SaveGameFrame extends JFrame {
    private int width = 300;
    private int height = 225;
    private Color foregroundColor, backgroundColor;
    private Font font;

    public SaveGameFrame(){
        backgroundColor = Settings.gameSettings.getGlobalTheme().getBackgroundColor();
        foregroundColor = Settings.gameSettings.getGlobalTheme().getForegroundColor();
        setIconImage(new ImageIcon("src/main/resources/1320183166943884936_128.png").getImage());
        font = Settings.gameSettings.getGlobalTheme().getFontLarge();
        setSize(width,height);
        setLocation(300,200);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        setTitle("SAVE GAME");
        createPanel();
    }

    public void createPanel(){
        JPanel savePanel = new JPanel();
        add(savePanel, BorderLayout.CENTER);
        savePanel.setBackground(backgroundColor);
        savePanel.setLayout(null);

        JLabel fileNameLabel = new JLabel();
        fileNameLabel.setText("File Name:");
        fileNameLabel.setFont(font);
        fileNameLabel.setForeground(foregroundColor);
        fileNameLabel.setBounds(50,50,200,20);
        fileNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setText("file name (wordly chars only)");
        fileNameTextField.setBounds(50,100, 200,20);
        fileNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        fileNameTextField.addActionListener(new SaveGameListener(this, fileNameTextField));

        savePanel.add(fileNameLabel);
        savePanel.add(fileNameTextField);

    }

}
