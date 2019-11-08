package main.java.view;

import javax.swing.*;
import java.awt.*;

public class MyErrorFrame extends JFrame {
    private int width = 300;
    private int height = 225;

    public MyErrorFrame(){
        setSize(width,height);
        setLocation(400,400);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        this.createPanel();

    }

    private void createPanel(){
        JPanel errorPanel = new JPanel(new BorderLayout());
        add(errorPanel,BorderLayout.CENTER);
        JTextArea textField = new JTextArea();
        textField.setText("Incorrect settings!\n" +
                "Please make sure that:\n" +
                "1. Both player's names are not empty\n" +
                "2. Players picked colors are not the same\n" +
                "3. Board size is an integer between 10 and 40\n" +
                "4. Timer is a positive integer");
        errorPanel.add(textField, BorderLayout.CENTER);
    }
}
