package main.java.controller;

import main.java.model.Settings;
import main.java.view.BoardFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class LoadGameListener extends AbstractAction {
    private JFrame loadFrame;
    private JTextField textField;
    private String directoryPath = "src/main/saves/";

    public LoadGameListener(JFrame loadFrame, JTextField textField) {
        this.loadFrame = loadFrame;
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String filename = textField.getText().trim();
        String path;
        if (filename.endsWith(".txt")) path = directoryPath + filename;
        else path = directoryPath + filename + ".txt";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Settings.setGameSettings((Settings)ois.readObject());
        } catch (FileNotFoundException e1) {
            System.err.println("File has not been found");
        } catch (IOException e2) {
            System.err.println("Input & Output exception");
        } catch (ClassNotFoundException e3) {
            System.err.println("Class Settings could not been found");
        }
        loadFrame.setVisible(false);
        BoardFrame boardFrame = new BoardFrame();
    }
}
