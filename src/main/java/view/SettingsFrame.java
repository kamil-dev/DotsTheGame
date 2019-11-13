package main.java.view;

import main.java.controller.NavigateMouseListener;
import main.java.model.Settings;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame extends JFrame {
    private int width = 500;
    private int height = 525;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.WHITE, Color.BLACK};
    private JTextField timerTextField, boardSizeTextField, playerOneNameTextField, playerTwoNameTextField;
    private JComboBox<Color> playerOneComboBox, playerTwoComboBox;
    private Color backgroundColor, foregroundColor;
    private Font fontLarge, fontNormal;

    public SettingsFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        backgroundColor = Settings.gameSettings.getGlobalTheme().getBackgroundColor();
        foregroundColor = Settings.gameSettings.getGlobalTheme().getForegroundColor();
        fontLarge = Settings.gameSettings.getGlobalTheme().getFontLarge();
        fontNormal = Settings.gameSettings.getGlobalTheme().getFontNormal();
        createPanel();
    }

    private void createPanel(){
        JPanel settingsPanel = new JPanel();
        add(settingsPanel, BorderLayout.CENTER);
        settingsPanel.setLayout(null);

        settingsPanel.setBackground(backgroundColor);

        JLabel playerOneLabel = new JLabel("Player 1");
        playerOneLabel.setBounds(50,20, 100,20);
        playerOneLabel.setFont(fontLarge);
        playerOneLabel.setForeground(foregroundColor);

        JLabel playerTwoLabel = new JLabel("Player 2");
        playerTwoLabel.setBounds( width - 150,20, 100,20);
        playerTwoLabel.setFont(fontLarge);
        playerTwoLabel.setForeground(foregroundColor);
        playerTwoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel nameLabel = new JLabel("name");
        nameLabel.setForeground(foregroundColor);
        nameLabel.setFont(fontNormal);
        nameLabel.setBounds(width - 300, 60, 100, 20);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        playerOneNameTextField = new JTextField();
        playerOneNameTextField.setBounds(50,60,100,20);

        playerTwoNameTextField = new JTextField();
        playerTwoNameTextField.setBounds(width - 150,60,100,20);

        JLabel colorLabel = new JLabel("color");
        colorLabel.setForeground(foregroundColor);
        colorLabel.setFont(fontNormal);
        colorLabel.setBounds(width - 300, 100, 100, 20);
        colorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        playerOneComboBox = new JComboBox<>(colors);
        playerOneComboBox.setBounds(50,100,100,20);
        playerOneComboBox.setSelectedIndex(0);

        playerTwoComboBox = new JComboBox<>(colors);
        playerTwoComboBox.setBounds(width - 150,100,100,20);
        playerTwoComboBox.setSelectedIndex(1);

        ComboBoxRenderer renderer = new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(50,15));
        playerOneComboBox.setRenderer(renderer);
        playerTwoComboBox.setRenderer(renderer);

        JLabel boardSizeLabel = new JLabel("Board Size:");
        boardSizeLabel.setForeground(foregroundColor);
        boardSizeLabel.setFont(fontNormal);
        boardSizeLabel.setBounds(50,height - 140,100,20);

        boardSizeTextField = new JTextField();
        boardSizeTextField.setBounds(160,height - 140,50,20);

        JLabel timerLabel = new JLabel("Timer (s):");
        timerLabel.setForeground(foregroundColor);
        timerLabel.setFont(fontNormal);
        timerLabel.setBounds(50,height - 100,100,20);

        timerTextField = new JTextField();
        timerTextField.setBounds(160,height - 100,50,20);


        JLabel acceptLabel = new JLabel("Accept");
        acceptLabel.setForeground(foregroundColor);
        acceptLabel.setFont(fontLarge);
        acceptLabel.addMouseListener(new NavigateMouseListener(acceptLabel, this));
        acceptLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        acceptLabel.setBounds(width - 150,height - 140, 100,20);

        JLabel cancelLabel = new JLabel("Cancel");
        cancelLabel.setFont(fontLarge);
        cancelLabel.setForeground(foregroundColor);
        cancelLabel.addMouseListener(new NavigateMouseListener(cancelLabel, this));
        cancelLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        cancelLabel.setBounds(width - 150,height - 100, 100,20);


        settingsPanel.add(playerOneLabel);
        settingsPanel.add(playerTwoLabel);
        settingsPanel.add(playerOneNameTextField);
        settingsPanel.add(nameLabel);
        settingsPanel.add(playerTwoNameTextField);
        settingsPanel.add(colorLabel);
        settingsPanel.add(playerOneComboBox);
        settingsPanel.add(playerTwoComboBox);
        settingsPanel.add(boardSizeLabel);
        settingsPanel.add(boardSizeTextField);
        settingsPanel.add(timerLabel);
        settingsPanel.add(timerTextField);
        settingsPanel.add(acceptLabel);
        settingsPanel.add(cancelLabel);

    }

    public Color getP1Color(){
        return (Color)playerOneComboBox.getSelectedItem();
    }

    public Color getP2Color(){
        return (Color)playerTwoComboBox.getSelectedItem();
    }

    public String getP1Name(){
        return playerOneNameTextField.getText();
    }

    public String getP2Name(){
        return playerTwoNameTextField.getText();
    }

    public Integer getBoardSize() {
        try {
            return Integer.parseInt(boardSizeTextField.getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer getTimer() {
        try {
            return Integer.parseInt(timerTextField.getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }



}
