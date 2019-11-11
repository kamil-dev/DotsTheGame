package main.java.view;

import main.java.model.Player;

import javax.swing.*;
import java.awt.*;

public class EndFrame extends JFrame {
    private int width = 500;
    private int height = 525;

    public EndFrame(Player winner){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        createPanel();
    }

    public void createPanel(){
        JPanel endPanel = new JPanel();
        add(endPanel, BorderLayout.CENTER);
        Color c1 = new Color(204, 204, 255);
        Color c2 = new Color(0, 51, 153);

        endPanel.setBackground(c1);
    }

}
