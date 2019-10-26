package main.java.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardFrame extends JFrame {
    private int width = 500;
    private int height = 525;
    private Settings settings;
    private boolean isPlayersOneTurn = true;

    public BoardFrame(Settings s) {
        settings = s;
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false);
        createBoard();

    }

    private void createBoard(){
        int size = settings.getBoardSize();
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(size,size));
        BoardSquare[][] board = new BoardSquare[size][size];
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size ; j++) {
                BoardSquare bs = new BoardSquare(i,j,settings);
                board[i][j] = bs;
                boardPanel.add(bs);
                bs.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (isPlayersOneTurn) {
                            bs.setState(1);
                            isPlayersOneTurn = false;
                        }
                        else {
                            bs.setState(2);
                            isPlayersOneTurn = true;
                        }
                        repaint();
                    }
                });
            }
        }
        add(boardPanel,BorderLayout.CENTER);
        repaint();

    }

}
