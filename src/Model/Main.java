package Model;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run(){
                MenuFrame menuFrame = new MenuFrame();
                menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuFrame.setVisible(true);
            }
        });
    }
}
