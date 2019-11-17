
package main.java.model;

import main.java.view.MenuFrame;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        java.net.URL url = ClassLoader.getSystemResource("src\\main\\resources\\iconfinder_time_clock_107185.png");

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run(){
                //Default settings - can be changed.
                Settings.setGameSettings(new Player(0,Color.RED,1800, "Player 1",true),
                        new Player(1,Color.BLUE,1800, "Player 2",false),1800,25);
                MenuFrame menuFrame = new MenuFrame();
            }
        });
    }
}
