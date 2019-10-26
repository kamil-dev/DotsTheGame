package main.java.model;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        add(settingsPanel,BorderLayout.CENTER);
        Color c1 = new Color(204, 204, 255);
        Color c2 = new Color(0, 51, 153);

        settingsPanel.setBackground(c1);
        BoxLayout layout = new BoxLayout(settingsPanel,BoxLayout.Y_AXIS);
        settingsPanel.setLayout(layout);
        settingsPanel.setBorder(new EmptyBorder(new Insets(150, 180, 150, 180)));

        Font font = new Font("Arial",Font.BOLD,20);


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

        settingsPanel.add(play);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        settingsPanel.add(loadGame);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        settingsPanel.add(settings);
    }


}
