package main.java.model;

import main.java.model.dataStructures.Base;
import main.java.model.dataStructures.Cycle;
import main.java.model.dataStructures.Dot;
import main.java.model.dataStructures.DotNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardFrame extends JFrame {
    private int width = 500;
    private int height = 525;
    private Settings settings;
    private boolean isPlayersOneTurn = true;

    public BoardFrame() {
        settings = Settings.GAME_SETTINGS;
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
                BoardSquare bs = new BoardSquare(i,j);
                board[i][j] = bs;
                boardPanel.add(bs);
                bs.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (isPlayersOneTurn) {
                            if (bs.getState() == 0) {
                                settings.getBoard().addDot(new Dot(bs.getRow(), bs.getColumn(), settings.getP1().id));
                                bs.setState(1);
                                isPlayersOneTurn = false;
                            }
                        }
                        else {
                            if (bs.getState() == 0) {
                                settings.getBoard().addDot(new Dot(bs.getRow(), bs.getColumn(), settings.getP1().id));
                                bs.setState(2);
                                isPlayersOneTurn = true;
                            }
                        }
                        repaint();
                    }
                });
            }
        }
        settings.setBoardSquares(board);
        add(boardPanel,BorderLayout.CENTER);
        repaint();
    }

    private void drawBase(Base base){
        Cycle cycleToDraw = base.getCycle();
        DotNode dotNode = cycleToDraw.getDotNode();
        Dot dot = dotNode.d;

        while (dotNode != null){


            dotNode = dotNode.next;
        }


    }


}
