package main.java.view;


import main.java.controller.BoardSquareMouseListener;
import main.java.controller.MyMouseListener;
import main.java.model.Player;
import main.java.model.Settings;
import main.java.model.dataStructures.Base;
import main.java.model.dataStructures.Cycle;
import main.java.model.dataStructures.Dot;
import main.java.model.dataStructures.DotNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardFrame extends JFrame {
    private int width = 800;
    private int height = 525;
    private Settings settings;
    private BoardSquare[][] board;
    private Color c1 = new Color(204, 204, 255);
    private Color c2 = new Color(0, 51, 153);
    private JLabel playerOneTimerLabel, playerTwoTimerLabel;
    private JLabel scoreLabel = new JLabel();
    private Timer timer;

    public BoardFrame() {
        settings = Settings.GAME_SETTINGS;
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        boardPanel.setBackground(c1);
        boardPanel.setLayout(new GridLayout(size,size));
        board = new BoardSquare[size][size];
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size ; j++) {
                BoardSquare bs = new BoardSquare(i,j);
                board[i][j] = bs;
                boardPanel.add(bs);
                bs.addMouseListener(new BoardSquareMouseListener(bs, scoreLabel));
//                bs.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        super.mouseClicked(e);
//                        if (isPlayersOneTurn) {
//                            if (bs.getState() == 0) {
//                                settings.getBoard().addDot(bs.getRow(), bs.getColumn());
//                                bs.setState(1);
//                                isPlayersOneTurn = false;
//                                settings.getP1().setActive(false);
//                                settings.getP2().setActive(true);
//                            }
//                        }
//                        else {
//                            if (bs.getState() == 0) {
//                                settings.getBoard().addDot(bs.getRow(), bs.getColumn());
//                                bs.setState(2);
//                                isPlayersOneTurn = true;
//                                settings.getP1().setActive(true);
//                                settings.getP2().setActive(false);
//                            }
//                        }
//                        repaint();
//                    }
//                });
            }
        }
        settings.setBoardSquares(board);
        add(boardPanel,BorderLayout.CENTER);
        repaint();
    }

    private void createInfoPanel(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(c1);
        infoPanel.setPreferredSize(new Dimension(300,500));
        add(infoPanel, BorderLayout.EAST);

        Font font = new Font("Arial",Font.BOLD,18);

        JLabel playerOneLabel = new JLabel();
        playerOneLabel.setText(settings.getP1().getName());
        playerOneLabel.setFont(font);
        playerOneLabel.setForeground(settings.getP1().getColor());
        playerOneLabel.setBounds(20,20, 100,20);

        JLabel playerTwoLabel = new JLabel();
        playerTwoLabel.setText(settings.getP2().getName());
        playerTwoLabel.setFont(font);
        playerTwoLabel.setForeground(settings.getP2().getColor());
        playerTwoLabel.setBounds((int)infoPanel.getPreferredSize().getWidth() - 140,20, 100,20);
        playerTwoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        scoreLabel.setForeground(c2);
        scoreLabel.setFont(font);
        scoreLabel.setText("" + settings.getP1().getPoints() + " : " + settings.getP2().getPoints());
        scoreLabel.setBounds(130,20, 60,20);


        JLabel saveLabel = new JLabel();
        saveLabel.setForeground(c2);
        saveLabel.setFont(font);
        saveLabel.setText("Save Game");
        saveLabel.addMouseListener(new MyMouseListener(saveLabel, this));
        saveLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        saveLabel.setBounds((int)infoPanel.getPreferredSize().getWidth() - 160,(int)infoPanel.getPreferredSize().getHeight() - 80, 140,25);

        JLabel resignLabel = new JLabel();
        resignLabel.setForeground(Color.RED);
        resignLabel.setFont(font);
        resignLabel.setText("Resign");
        resignLabel.addMouseListener(new MyMouseListener(resignLabel, this));
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

        Font timerFont = new Font("Arial", Font.ITALIC, 16);


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
        Player p1 = Settings.GAME_SETTINGS.getP1();
        Player p2 = Settings.GAME_SETTINGS.getP2();

        @Override
        public void actionPerformed(ActionEvent e) {
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


//    private void drawBase(Base base){
//        Cycle cycleToDraw = base.getCycle();
//        DotNode dotNode = cycleToDraw.getDotNode();
//        List<Dot> sortedListOfDotsWithinACycle = new ArrayList<>();
//        sortedListOfDotsWithinACycle.add(dotNode.d);
//        while (dotNode.next != null){
//            sortedListOfDotsWithinACycle.add(dotNode.next.d);
//            dotNode = dotNode.next;
//        }
//
//        Dot d;
//        Dot previousD;
//        Dot nextD;
//        for (int i = 0; i < sortedListOfDotsWithinACycle.size() - 1; i++) {
//            if (i != 0) previousD = sortedListOfDotsWithinACycle.get(i - 1);
//            else previousD = sortedListOfDotsWithinACycle.get(sortedListOfDotsWithinACycle.size() - 1);
//
//            if (i != sortedListOfDotsWithinACycle.size() - 1) nextD = sortedListOfDotsWithinACycle.get(i +1);
//            else nextD = sortedListOfDotsWithinACycle.get(0);
//
//            d = sortedListOfDotsWithinACycle.get(i);
//            board[d.getX()][d.getY()].addConnection(previousD);
//            board[d.getX()][d.getY()].addConnection(nextD);
//            board[d.getX()][d.getY()].repaint();
//        }
//
//    }


}
