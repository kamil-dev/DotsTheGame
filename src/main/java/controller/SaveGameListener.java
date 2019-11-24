package main.java.controller;

import main.java.model.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveGameListener extends AbstractAction {
    private JFrame saveFrame;
    private JTextField textField;
    private String directoryPath = new File("").getAbsolutePath()+"/";

    public SaveGameListener(JFrame saveFrame, JTextField textField) {
        this.saveFrame = saveFrame;
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String filename = textField.getText().trim();
        String path;
        if (validateString(filename)) {
            if (filename.endsWith(".txt")) path = directoryPath + filename;
            else path = directoryPath + filename + ".txt";

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
                oos.writeObject(Settings.gameSettings);
            } catch (FileNotFoundException e1) {
                System.err.println("File has not been found");
            } catch (IOException e2) {
                System.err.println("Input & Output exception");
            }
            saveFrame.setVisible(false);
        } else {
            textField.setText("wordly chars only!");
        }

    }

    public static boolean validateString(String input){
        if (input.equals("")) {
            return false;
        }
        if (input.matches("\\w+")) return true;
        return false;
    }
}
