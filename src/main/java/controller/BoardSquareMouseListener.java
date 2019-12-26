package main.java.controller;

import main.java.model.Player;
import main.java.model.Settings;
import main.java.view.BoardSquare;
import main.java.view.EndFrame;
import main.java.view.EndGameCause;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class BoardSquareMouseListener extends MouseAdapter implements Serializable {
    private BoardSquare bs;
    private Settings settings;
    private JLabel scoreLabel;
    private JFrame boardFrame;

    public BoardSquareMouseListener(BoardSquare bs, JLabel scoreLabel, JFrame boardFrame) {
        this.bs = bs;
        this.settings = Settings.gameSettings;
        this.scoreLabel = scoreLabel;
        this.boardFrame = boardFrame;
    }

    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(bs.getState() == 3)
            return;
        if (settings.getP1().isActive()) {
            if (bs.getState() == 0) {
                settings.getBoard().addDot(bs.getRow(), bs.getColumn());
                bs.setState(1);
                settings.getP1().setActive(false);
                settings.getP2().setActive(true);
            }
        }
        else {
            if (bs.getState() == 0) {
                settings.getBoard().addDot(bs.getRow(), bs.getColumn());
                bs.setState(2);
                settings.getP1().setActive(true);
                settings.getP2().setActive(false);
            }
        }
        bs.repaint();
        scoreLabel.setText("" + settings.getP1().getPoints() + " : " + settings.getP2().getPoints());
        if(settings.getBoard().getFreeDotSpaces().size()==0){
            boardFrame.setVisible(false);
            Player p = Settings.gameSettings.getP1();
            Player p2 = Settings.gameSettings.getP2();
            if(p.getPoints() < p2.getPoints())
                p = p2;
            EndFrame endFrame = new EndFrame(p, EndGameCause.NO_AVAILABLE_MOVE );
        }

    }
}
