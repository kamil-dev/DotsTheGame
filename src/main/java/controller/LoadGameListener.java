package main.java.controller;

import main.java.model.Settings;
import main.java.view.BoardFrame;
import main.java.view.MenuFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class LoadGameListener extends AbstractAction {
    private JFrame frame;
    private JFileChooser fileChooser;

    public LoadGameListener(JFileChooser fileChooser, JFrame frame) {
        this.fileChooser = fileChooser;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("CancelSelection")) {
            frame.setVisible(false);
            MenuFrame menuFrame = new MenuFrame();
        } else if (e.getActionCommand().equals("ApproveSelection")) {
            System.out.println(e.getActionCommand());
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Settings.setGameSettings((Settings)ois.readObject());
            } catch (FileNotFoundException e1) {
                System.err.println("File has not been found");
            } catch (IOException e2) {
                System.err.println("Input & Output exception");
            } catch (ClassNotFoundException e3) {
                System.err.println("Class Settings could not been found");
            }
            frame.setVisible(false);
            BoardFrame boardFrame = new BoardFrame();
        }
    }
}
