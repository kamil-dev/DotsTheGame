package main.java.view;


import main.java.controller.BoardSquareMouseListener;
import main.java.controller.NavigateMouseListener;
import main.java.model.Player;
import main.java.model.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardFrame extends JFrame {
    private int width = 800;
    private int height = 525;
    private Settings settings;
    private BoardSquare[][] board;
    private Color backgroundColor;
    private Color foregroundColor;
    private Font font;
    private JLabel playerOneTimerLabel, playerTwoTimerLabel;
    private JLabel scoreLabel;
    private Timer timer;

    public BoardFrame() {
        settings = Settings.gameSettings;
        backgroundColor = settings.getGlobalTheme().getBackgroundColor();
        foregroundColor = settings.getGlobalTheme().getForegroundColor();
        setIconImage(new ImageIcon("src/main/resources/1320183166943884936_128.png").getImage());
        font = settings.getGlobalTheme().getFontLarge();
        scoreLabel = new JLabel();
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                super.windowIconified(e);
                timer.stop();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                super.windowDeiconified(e);
                timer.start();
            }
        });
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        setTitle("DOTS");
        createBoard();
        createInfoPanel();
    }


    private void createBoard(){
        int size = settings.getBoardSize();
        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(backgroundColor);
        boardPanel.setLayout(new GridLayout(size,size));
        if (!Settings.gameSettings.isLoaded()) {
            board = new BoardSquare[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    BoardSquare bs = new BoardSquare(i, j);
                    board[i][j] = bs;
                    boardPanel.add(bs);
                    bs.addMouseListener(new BoardSquareMouseListener(bs, scoreLabel));
                }
            }
            settings.setBoardSquares(board);
        } else {
            for (int i = 0; i < size ; i++) {
                for (int j = 0; j < size ; j++) {
                    boardPanel.add(Settings.gameSettings.getBoardSquares()[i][j]);
                }
            }
        }
        add(boardPanel,BorderLayout.CENTER);
        repaint();
    }



    private void createInfoPanel(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(backgroundColor);
        infoPanel.setPreferredSize(new Dimension(300,500));
        add(infoPanel, BorderLayout.EAST);

        JLabel playerOneLabel = new JLabel();
        playerOneLabel.setText(settings.getP1().getName());
        playerOneLabel.setFont(font);
        playerOneLabel.setForeground(settings.getP1().getColor());
        playerOneLabel.setBounds(20,20, 100,25);

        JLabel playerTwoLabel = new JLabel();
        playerTwoLabel.setText(settings.getP2().getName());
        playerTwoLabel.setFont(font);
        playerTwoLabel.setForeground(settings.getP2().getColor());
        playerTwoLabel.setBounds((int)infoPanel.getPreferredSize().getWidth() - 120,20, 100,25);
        playerTwoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        scoreLabel.setForeground(foregroundColor);
        scoreLabel.setFont(settings.getGlobalTheme().getFontLarge());
        scoreLabel.setText("" + settings.getP1().getPoints() + " : " + settings.getP2().getPoints());
        scoreLabel.setBounds(130,20, 60,20);

        JLabel saveLabel = new JLabel();
        saveLabel.setForeground(foregroundColor);
        saveLabel.setFont(font);
        saveLabel.setText("Save Game");
        saveLabel.addMouseListener(new NavigateMouseListener(saveLabel, this));
        saveLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        saveLabel.setBounds((int)infoPanel.getPreferredSize().getWidth() - 160,(int)infoPanel.getPreferredSize().getHeight() - 80, 140,25);

        JLabel resignLabel = new JLabel();
        resignLabel.setForeground(Color.RED);
        resignLabel.setFont(font);
        resignLabel.setText("Resign");
        resignLabel.addMouseListener(new NavigateMouseListener(resignLabel, this));
        resignLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        resignLabel.setBounds((int)infoPanel.getPreferredSize().getWidth() - 160,(int)infoPanel.getPreferredSize().getHeight() - 50, 140,25);

         try {
             BufferedImage clockIcon = ImageIO.read(new File("src\\main\\resources\\iconfinder_time_clock_107185.png"));
             JLabel clockLabel = new JLabel(new ImageIcon(clockIcon.getScaledInstance(60,60,Image.SCALE_SMOOTH)));
             clockLabel.setBounds(120,40, 60,60);
             infoPanel.add(clockLabel);
         } catch (IOException e) {
             System.err.println("The clock icon could not be read");
         }

        Font timerFont = new Font("Arial", Font.ITALIC, 15);

        playerOneTimerLabel = new JLabel();
        playerOneTimerLabel.setText(timerIntoString(settings.getP1().getRemainingTime()));
        playerOneTimerLabel.setFont(timerFont);
        playerOneTimerLabel.setForeground(settings.getP1().getColor());
        playerOneTimerLabel.setBounds(20,60, 120,20);

        timer = new Timer(1000, new ClockListener());
        timer.start();

        playerTwoTimerLabel = new JLabel();
        playerTwoTimerLabel.setText(timerIntoString(settings.getP2().getRemainingTime()));
        playerTwoTimerLabel.setFont(timerFont);
        playerTwoTimerLabel.setForeground(settings.getP2().getColor());
        playerTwoTimerLabel.setBounds((int)infoPanel.getPreferredSize().getWidth() - 140,60, 120,20);
        playerTwoTimerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        infoPanel.add(playerOneLabel);
        infoPanel.add(playerTwoLabel);
        infoPanel.add(playerOneTimerLabel);
        infoPanel.add(playerTwoTimerLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(resignLabel);
        infoPanel.add(saveLabel);

    }

    private class ClockListener implements ActionListener {
        Player p1 = Settings.gameSettings.getP1();
        Player p2 = Settings.gameSettings.getP2();



        @Override
        public void actionPerformed(ActionEvent e) {
            scoreLabel.setText("" + settings.getP1().getPoints() + " : " + settings.getP2().getPoints());
            if (p1.isActive()) {
                p1.setRemainingTime(p1.getRemainingTime() - 1);
                playerOneTimerLabel.setText(timerIntoString(p1.getRemainingTime()));
                if (p1.getRemainingTime() <= 0) {
                    setVisible(false);
                    EndFrame endFrame = new EndFrame(p2, EndGameCause.TIMER );
                    timer.stop();
                }
            } else {
                p2.setRemainingTime(p2.getRemainingTime() - 1);
                playerTwoTimerLabel.setText(timerIntoString(p2.getRemainingTime()));
                if (p2.getRemainingTime() <= 0) {
                    setVisible(false);
                    EndFrame endFrame = new EndFrame(p1, EndGameCause.TIMER );
                    timer.stop();
                }
            }
        }
    }

    private static String timerIntoString(int timeInSeconds){
        int sec = timeInSeconds % 60;
        int min = ((timeInSeconds - sec)/60) % 60;
        int hours = (((timeInSeconds - sec)/60) - min)/60;
        return "" + (hours>0 ? hours : 0) + "h : " + min + "m : " + sec + "s";

    }
}
