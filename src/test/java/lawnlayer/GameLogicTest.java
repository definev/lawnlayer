package lawnlayer;


import lawnlayer.utils.Coordinate;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.direction.MoveDirection;
import lawnlayer.utils.gameMap.GameMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

public class GameLogicTest {
    @Test
    public void floodFillTest() throws IOException {
        GameMap testcase = GameMap.load("testcase.txt");
        GameMap expect = GameMap.load("expect.txt");
        testcase.relativeFloodFill(new Coordinate(62, 5));

        ArrayList<ArrayList<Character>> testcaseMap = testcase.getRawMap();
        ArrayList<ArrayList<Character>> expectMap = expect.getRawMap();

        for (int i = 0; i < GameUtils.MAP_HEIGHT * 3; i += 1) {
            for (int j = 0; j < GameUtils.MAP_WIDTH * 3; j += 1) {
                assertEquals(testcaseMap.get(i).get(j), expectMap.get(i).get(j));
            }
        }
    }

    @Test
    public void coordinateTest() {
        Coordinate coor = new Coordinate(10, 20);
        Coordinate left = coor.move(MoveDirection.left);
        assertEquals(left.x, 9);
        assertEquals(left.y, 20);

        Coordinate right = coor.move(MoveDirection.right);
        assertEquals(right.x, 11);
        assertEquals(right.y, 20);

        Coordinate up = coor.move(MoveDirection.up);
        assertEquals(up.x, 10);
        assertEquals(up.y, 19);

        Coordinate down = coor.move(MoveDirection.down);
        assertEquals(down.x, 10);
        assertEquals(down.y, 21);

        Coordinate outOfBound = coor.move(MoveDirection.left, 11);
        assertEquals(outOfBound.isOutOfBounds(), true);
    }
}
