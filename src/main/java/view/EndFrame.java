package main.java.view;

import main.java.model.Player;
import main.java.model.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EndFrame extends JFrame {
    private int width = 500;
    private int height = 425;
    private Player winner;
    private EndGameCause cause;
    private Color backgroundColor;
    private Color foregroundColor;
    private Font font;

    public EndFrame(Player winner, EndGameCause cause){
        font = new Font("Comic Sans MS",Font.BOLD,16);
        foregroundColor = Settings.gameSettings.getGlobalTheme().getForegroundColor();
        backgroundColor = Settings.gameSettings.getGlobalTheme().getBackgroundColor();
        setIconImage(new ImageIcon("src/main/resources/1320183166943884936_128.png").getImage());
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        this.winner = winner;
        this.cause = cause;
        setTitle("END OF GAME");
        createPanel();
    }

    public void createPanel(){
        JPanel endPanel = new JPanel();
        endPanel.setLayout(null);
        add(endPanel, BorderLayout.CENTER);

        JLabel congratsLabel = new JLabel("Congratulations!");
        congratsLabel.setForeground(foregroundColor);
        congratsLabel.setFont(font);
        congratsLabel.setBounds(150,50,200,20);
        congratsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel winnersNameLabel = new JLabel(winner.getName() + " has won the game");
        winnersNameLabel.setForeground(foregroundColor);
        winnersNameLabel.setFont(font);
        winnersNameLabel.setBounds(100,100,300,20);
        winnersNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel messageLabel = new JLabel();
        messageLabel.setText(cause.getText());
        messageLabel.setForeground(foregroundColor);
        messageLabel.setFont(font);
        messageLabel.setBounds(100,150,300,20);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel scoreLabel = new JLabel();
        scoreLabel.setText("Final score was: " + Settings.gameSettings.getP1().getPoints() + " : " + Settings.gameSettings.getP2().getPoints());
        scoreLabel.setForeground(foregroundColor);
        scoreLabel.setFont(font);
        scoreLabel.setBounds(100,200,300,20);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            BufferedImage gameNameIcon = ImageIO.read(new File("src\\main\\resources\\picturetopeople.org-d3b02807efef49a5e8992c90051993385db6615ff94978b17a.png"));
            JLabel gameNameLabel = new JLabel(new ImageIcon(gameNameIcon.getScaledInstance(140,60,Image.SCALE_SMOOTH)));
            gameNameLabel.setBounds(180, 250, 140,60);
            endPanel.add(gameNameLabel);
        } catch (IOException e) {
            System.err.println("The game name icon could not be read");
        }

        endPanel.setBackground(backgroundColor);
        endPanel.add(congratsLabel);
        endPanel.add(winnersNameLabel);
        endPanel.add(messageLabel);
        endPanel.add(scoreLabel);

    }

}
