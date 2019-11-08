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
        settingsPanel.setLayout(null);
        Color c1 = new Color(204, 204, 255);
        Color c2 = new Color(0, 51, 153);
        Font fontLarge = new Font("Arial",Font.BOLD,18);
        Font fontNormal = new Font("Arial",Font.PLAIN,16);

        settingsPanel.setBackground(c1);

        JLabel playerOneLabel = new JLabel("Player One");
        playerOneLabel.setBounds(50,20, 100,20);
        playerOneLabel.setFont(fontLarge);
        playerOneLabel.setForeground(c2);

        JLabel playerTwoLabel = new JLabel("Player Two");
        playerTwoLabel.setBounds( width - 150,20, 100,20);
        playerTwoLabel.setFont(fontLarge);
        playerTwoLabel.setForeground(c2);
        playerTwoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel nameLabel = new JLabel("name");
        nameLabel.setForeground(c2);
        nameLabel.setFont(fontNormal);
        nameLabel.setBounds(width - 300, 60, 100, 20);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField playerOneNameTextField = new JTextField();
        playerOneNameTextField.setBounds(50,60,100,20);

        JTextField playerTwoNameTextField = new JTextField();
        playerTwoNameTextField.setBounds(width - 150,60,100,20);

        JLabel colorLabel = new JLabel("color");
        colorLabel.setForeground(c2);
        colorLabel.setFont(fontNormal);
        colorLabel.setBounds(width - 300, 100, 100, 20);
        colorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        settingsPanel.add(playerOneLabel);
        settingsPanel.add(playerTwoLabel);
        settingsPanel.add(playerOneNameTextField);
        settingsPanel.add(nameLabel);
        settingsPanel.add(playerTwoNameTextField);
        settingsPanel.add(colorLabel);
    }

}
