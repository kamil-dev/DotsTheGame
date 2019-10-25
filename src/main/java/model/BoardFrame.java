package main.java.model;

import javax.swing.*;
import java.awt.*;

public class BoardFrame extends JFrame {
    private int width = 500;
    private int height = 525;

    public BoardFrame() {
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);

    }
}
