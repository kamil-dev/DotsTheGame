package main.java.controller;

import main.java.model.Settings;
import main.java.view.BoardSquare;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardSquareMouseListener extends MouseAdapter {
    private BoardSquare bs;
    private Settings settings;
    private JLabel scoreLabel;

    public BoardSquareMouseListener(BoardSquare bs, JLabel scoreLabel) {
        this.bs = bs;
        this.settings = Settings.GAME_SETTINGS;
        this.scoreLabel = scoreLabel;
    }

    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
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
    }
}
