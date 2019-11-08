package main.java.view;

import main.java.controller.MyMouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoadGameFrame extends JFrame {
    private int width = 500;
    private int height = 525;

    public LoadGameFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        createForm();
    }

    private void createForm(){
        JPanel settingsPanel = new JPanel();
        add(settingsPanel, BorderLayout.CENTER);
        Color c1 = new Color(204, 204, 255);
        Color c2 = new Color(0, 51, 153);

        settingsPanel.setBackground(c1);
        BoxLayout layout = new BoxLayout(settingsPanel,BoxLayout.Y_AXIS);
        settingsPanel.setLayout(layout);
        settingsPanel.setBorder(new EmptyBorder(new Insets(150, 150, 150, 150)));

        Font font = new Font("Arial",Font.BOLD,20);

        JLabel fileNameLabel = new JLabel();
        fileNameLabel.setText("File Name: ");
        fileNameLabel.setFont(font);
        fileNameLabel.setForeground(c2);

        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setText("Enter file name");

        JLabel loadGameLabel = new JLabel();
        loadGameLabel.setText("Load");
        loadGameLabel.setFont(font);
        loadGameLabel.addMouseListener(new MyMouseListener(loadGameLabel,this));
        loadGameLabel.setForeground(c2);

        JLabel cancelLabel = new JLabel();
        cancelLabel.setText("Cancel");
        cancelLabel.setFont(font);
        cancelLabel.addMouseListener(new MyMouseListener(cancelLabel,this));
        cancelLabel.setForeground(c2);


        settingsPanel.add(fileNameLabel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        settingsPanel.add(fileNameTextField);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        settingsPanel.add(loadGameLabel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        settingsPanel.add(cancelLabel);

        add(settingsPanel);
    }

}
