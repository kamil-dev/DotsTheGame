package main.java.view;


import main.java.controller.MyMouseListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuFrame extends JFrame {
    private int width = 500;
    private int height = 525;

    public MenuFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        createPanel();
    }

    private void createPanel(){
        JPanel menuPanel = new JPanel();
        add(menuPanel,BorderLayout.CENTER);
        Color c1 = new Color(204, 204, 255);
        Color c2 = new Color(0, 51, 153);

        menuPanel.setBackground(c1);
        BoxLayout layout = new BoxLayout(menuPanel,BoxLayout.Y_AXIS);
        menuPanel.setLayout(layout);
        menuPanel.setBorder(new EmptyBorder(new Insets(100, 180, 150, 180)));

        Font font = new Font("Arial",Font.BOLD,20);

        try {
            BufferedImage gameNameIcon = ImageIO.read(new File("src\\main\\resources\\picturetopeople.org-d3b02807efef49a5e8992c90051993385db6615ff94978b17a.png"));
            JLabel gameNameLabel = new JLabel(new ImageIcon(gameNameIcon.getScaledInstance(140,60,Image.SCALE_SMOOTH)));
            menuPanel.add(gameNameLabel);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        } catch (IOException e) {
            System.err.println("The game name icon could not be read");
        }

        JLabel play = new JLabel();
        play.setText("      Play");
        play.setFont(font);
        play.addMouseListener(new MyMouseListener(play,this));
        play.setForeground(c2);

        JLabel loadGame = new JLabel();
        loadGame.setText("Load Game");
        loadGame.setFont(font);
        loadGame.addMouseListener(new MyMouseListener(loadGame,this));
        loadGame.setForeground(c2);

        JLabel settings = new JLabel();
        settings.setText("   Settings");
        settings.setFont(font);
        settings.addMouseListener(new MyMouseListener(settings,this));
        settings.setForeground(c2);

        menuPanel.add(play);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(loadGame);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(settings);
    }


}
