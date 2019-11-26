package main.java.view;

import main.java.controller.LoadGameListener;
import main.java.controller.NavigateMouseListener;
import main.java.model.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class LoadGameFrame extends JFrame {
    private static final String PATH = new File("").getAbsolutePath()+"/";
    private int width = 500;
    private int height = 525;

    public LoadGameFrame(){
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("src/main/resources/1320183166943884936_128.png").getImage());
        setVisible(true);
        setResizable(false);
        setTitle("LOAD GAME");
        createFileChooser();
    }

    private void createFileChooser(){
        JFileChooser fileChooser = new JFileChooser(PATH);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .txt files", "txt");
        fileChooser.addChoosableFileFilter(restrict);
        fileChooser.addActionListener(new LoadGameListener(fileChooser, this));

        add(fileChooser, BorderLayout.CENTER);
    }


}
