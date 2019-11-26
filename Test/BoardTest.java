import main.java.model.Board;
import main.java.model.Player;
import main.java.model.Settings;
import main.java.model.dataStructures.Dot;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class BoardTest {
    private Board b;

    @BeforeClass
    public static void beforeClassSetup(){
        Player p1 = new Player(0,Color.WHITE,1000);
        Player p2 = new Player(1, Color.BLACK, 1000);
        Settings.setGameSettings(p1,p2,1000,10);
    }

    @Before
    public void setUp(){
        b = new Board(10);
        b.addDot(new Dot(4, 1, 0), false);
        b.addDot(new Dot(1,2,1), false);
        b.addDot(new Dot(1,1,0), false);
        b.addDot(new Dot(2,1,1), false);
        b.addDot(new Dot(2,0,0), false);
        b.addDot(new Dot(1,4,1), false);
        b.addDot(new Dot(2,2,0), false);
        b.addDot(new Dot(3,1,1), false);
        b.addDot(new Dot(3,0,0), false);
        b.addDot(new Dot(2,5,1), false);
    }

    @Test
    public void shouldLock2DotsOfPlayerTwo() {

        b.addDot(new Dot(3,2,0), false);

        assertEquals(2,Settings.gameSettings.getP1().getPoints());
        assertEquals(0,Settings.gameSettings.getP2().getPoints());
    }

    @Test
    public void shouldLock1DotOfPlayerOne() {

        b.addDot(new Dot(4,2,0), false);
        b.addDot(new Dot(3,2,1), false);
        b.addDot(new Dot(4,3,0), false);
        b.addDot(new Dot(2,3,1),false);

        assertEquals(0,Settings.gameSettings.getP1().getPoints());
        assertEquals(1,Settings.gameSettings.getP2().getPoints());
    }

    @Test
    public void shouldLock2CyclesSimultaniously(){
        b = new Board(10);
        b.addDot(new Dot(2,0, 0), false);
        b.addDot(new Dot(2,1, 1), false);
        b.addDot(new Dot(3,1, 0), false);
        b.addDot(new Dot(3,2, 1), false);
        b.addDot(new Dot(3,3, 0), false);
        b.addDot(new Dot(4,3, 1), false);
        b.addDot(new Dot(2,4, 0), false);
        b.addDot(new Dot(2,3, 1), false);
        b.addDot(new Dot(1,1,0), false);
        b.addDot(new Dot(4,2,1), false);
        b.addDot(new Dot(1,3,0), false);
        b.addDot(new Dot(4,1,1), false);
        b.addDot(new Dot(2,2,0), false);

        assertEquals(2,Settings.gameSettings.getP1().getPoints());
        assertEquals(0,Settings.gameSettings.getP2().getPoints());
    }

    @Test
    public void shouldBeAbleToLockPlayerTwoBase(){
        b.addDot(new Dot(0,2,0), false);
        b.addDot(new Dot(3,2,1), false);
        b.addDot(new Dot(2,4,0), false);
        b.addDot(new Dot(2,3,1), false); //player 2 creates his own base

        b.addDot(new Dot(3,3, 0), false);
        b.addDot(new Dot(0,3,1), false);
        b.addDot(new Dot(4,2,0), false);
        b.addDot(new Dot(4,3,1), false);
        b.addDot(new Dot(1,3,0), false); // base is retaken

        assertEquals(4,Settings.gameSettings.getP1().getPoints());
        assertEquals(0,Settings.gameSettings.getP2().getPoints());

    }
}