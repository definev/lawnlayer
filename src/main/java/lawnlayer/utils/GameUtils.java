package lawnlayer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lawnlayer.App;
import lawnlayer.gameObject.GameObject;
import lawnlayer.gameObject.Grass;
import lawnlayer.gameObject.Player;
import lawnlayer.gameObject.Wall;
import processing.core.PApplet;

public class GameUtils {
    public static final Integer MAP_HEIGHT = 32;
    public static final Integer MAP_WIDTH = 64;

    public static ArrayList<GameObject> load(PApplet app, String path) throws IOException {
        var boards = new ArrayList<ArrayList<Character>>();
        File file = new File(path);

        // Creating an object of BufferedReader class
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String st;
        // Condition holds true till
        // there is character in a string
        while ((st = br.readLine()) != null) {
            ArrayList<Character> board = new ArrayList();

            for (int j = 0; j < st.length(); j++) {
                board.add(j, st.charAt(j));
            }
            boards.add(board);
        }

        var objects = new ArrayList<GameObject>();

        for (Integer i = 0; i < boards.size(); i += 1) {
            for (Integer j = 0; j < boards.get(0).size(); j += 1) {
                var character = boards.get(i).get(j);
                if (character == Wall.symbol) {
                    Wall concrete = null;
                    for (GameObject object : objects) {
                        if (object instanceof Wall) {
                            concrete = (Wall) object;
                            object.addCoor(new Coordinate(j, i));
                            break;
                        }
                    }
                    if (concrete != null) continue;
                    concrete = new Wall(app);
                    concrete.addCoor(new Coordinate(j, i));
                    objects.add(concrete);
                }
                if (character == Grass.symbol) {
                    Grass concrete = null;
                    for (GameObject object : objects) {
                        if (object instanceof Grass) {
                            concrete = (Grass) object;
                            object.addCoor(new Coordinate(j, i));
                            break;
                        }
                    }
                    if (concrete != null) continue;
                    concrete = new Grass(app);
                    concrete.addCoor(new Coordinate(j, i));
                    objects.add(concrete);
                }
            }
        }

        return objects;
    }

    public static Coordinate transformCoor(Coordinate coor) {
        return new Coordinate(coor.x * App.SPRITE_SIZE, coor.y * App.SPRITE_SIZE + 80);
    }

    public static MoveDirection getDirection(MoveDirection lastMovedirection, Coordinate before, Coordinate after) {
        if (before == null || after == null) return lastMovedirection;
        if (before.x.equals(after.x)) {
            if (before.y < after.y) {
                return MoveDirection.down;
            } else {
                return MoveDirection.up;
            }
        } else {
            if (before.x < after.x) {
                return MoveDirection.right;
            } else {
                return MoveDirection.left;
            }
        }
    }

    public static void logMap(ArrayList<ArrayList<Character>> map) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(0).size(); j++) {
                System.out.print(map.get(i).get(j));
            }
            System.out.println();
        }
    }

    public static EnemyMoveDirection getOppositeMove(EnemyMoveDirection direction) {
        switch (direction) {
            case topLeft:
                return EnemyMoveDirection.bottomRight;
            case topRight:
                return EnemyMoveDirection.bottomLeft;
            case bottomLeft:
                return EnemyMoveDirection.topRight;
            case bottomRight:
                return EnemyMoveDirection.topLeft;
        }
        return direction;
    }
}
