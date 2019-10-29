

import main.java.model.Board;
import main.java.model.Dot;
import main.java.model.Player;
import org.junit.Test;

import java.awt.*;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BoardTest {
    Player p1 = new Player(Color.BLACK,100);
    Player p2 = new Player(Color.WHITE,100);
    List<Dot> listOfChainedDots;
    Board b = new Board(5);
    Dot[][] matrix = new Dot[][]{
            new Dot[]{null, null, null, null, null},
            new Dot[]{null, new Dot(1,1,p1),new Dot(1,2,p2), null, null},
            new Dot[]{new Dot(2,0,p1), new Dot(2,1,p2), new Dot(2,3,p1), null, null},
            new Dot[]{new Dot(3,0,p1), new Dot(3,1,p2), new Dot(3,3,p1), null, null},
            new Dot[]{null, new Dot(4, 1, p1), null, null, null},
    };


    @Test
    public void shouldReturnTwoLockedDots() {
        b.setMatrixOfDots(matrix);
        listOfChainedDots = Arrays.asList(new Dot(1,1,p1), new Dot(2,0,p1),
                new Dot(2,2,p1),new Dot(3,0,p1),new Dot(3,2,p1),new Dot(4,1,p1));
        List<Dot> findings = b.listOfOpponentsLockedDots(listOfChainedDots);
        assertEquals(2,findings.size());

    }

    @Test
    public void shouldReturnTrueForALockedDot() {

        listOfChainedDots = Arrays.asList(new Dot(1,1,p1), new Dot(2,0,p1),
                new Dot(2,2,p1),new Dot(3,1,p1));
        assertTrue(Board.isLocked(listOfChainedDots,new Dot(2,1,p2)));
    }
}