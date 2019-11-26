package main.java.view;


import main.java.controller.NavigateMouseListener;
import main.java.model.Settings;

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
    private Color backgroundColor;
    private Color foregroundColor;
    private Font font;

    public MenuFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        setIconImage(new ImageIcon("src/main/resources/1320183166943884936_128.png").getImage());
        backgroundColor = Settings.gameSettings.getGlobalTheme().getBackgroundColor();
        foregroundColor = Settings.gameSettings.getGlobalTheme().getForegroundColor();
        font = Settings.gameSettings.getGlobalTheme().getFontLarge();
        setTitle("MENU");
        createPanel();
    }

    private void createPanel(){
        JPanel menuPanel = new JPanel();
        add(menuPanel,BorderLayout.CENTER);

        menuPanel.setBackground(backgroundColor);
        BoxLayout layout = new BoxLayout(menuPanel,BoxLayout.Y_AXIS);
        menuPanel.setLayout(layout);
        menuPanel.setBorder(new EmptyBorder(new Insets(100, 180, 150, 180)));

        try {
            BufferedImage gameNameIcon = ImageIO.read(new File("src\\main\\resources\\picturetopeople.org-d3b02807efef49a5e8992c90051993385db6615ff94978b17a.png"));
            JLabel gameNameLabel = new JLabel(new ImageIcon(gameNameIcon.getScaledInstance(140,60,Image.SCALE_SMOOTH)));
            menuPanel.add(gameNameLabel);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        } catch (IOException e) {
            System.err.println("The game name icon could not be read");
        }

        JLabel play = new JLabel();
        play.setText("     Play");
        play.setFont(font);
        play.addMouseListener(new NavigateMouseListener(play,this));
        play.setForeground(foregroundColor);

        JLabel loadGame = new JLabel();
        loadGame.setText(" Load Game");
        loadGame.setFont(font);
        loadGame.addMouseListener(new NavigateMouseListener(loadGame,this));
        loadGame.setForeground(foregroundColor);

        JLabel settings = new JLabel();
        settings.setText("   Settings");
        settings.setFont(font);
        settings.addMouseListener(new NavigateMouseListener(settings,this));
        settings.setForeground(foregroundColor);

        menuPanel.add(play);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(loadGame);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(settings);
    }


}
