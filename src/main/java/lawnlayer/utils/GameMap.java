package lawnlayer.utils;

import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

import lawnlayer.App;
import lawnlayer.gameObject.Beetle;
import lawnlayer.gameObject.Worm;
import lawnlayer.gameObject.GameObject;
import lawnlayer.gameObject.Grass;
import lawnlayer.gameObject.Player;
import lawnlayer.gameObject.Wall;

public class GameMap {
    protected ArrayList<ArrayList<GameMapPixel>> masterMap = new ArrayList<>();


    public GameMap() {
        clear();
    }

    public GameMap(ArrayList<ArrayList<GameMapPixel>> masterMap) {
        this.masterMap = masterMap;
    }

    public GameMap(String path) {
        load(path);
    }

    public GameMapPixel get(Coordinate coordinate) {
        return masterMap.get(coordinate.y).get(coordinate.x);
    }

    public Coordinate randomizeLocation() {
        var rand = new Random();
        ArrayList<Coordinate> emptyList = new ArrayList<>();

        for (int i = 0; i < masterMap.size(); i++) {
            for (int j = 0; j < masterMap.get(0).size(); j++) {
                if (masterMap.get(i).get(j).symbol == ' ') {
                    emptyList.add(new Coordinate(j, i));
                }
            }
        }
        return emptyList.get(rand.nextInt(emptyList.size()));
    }

    public ArrayList<ArrayList<Character>> getRawMap() {
        ArrayList<ArrayList<Character>> maps = new ArrayList<>();
        for (int i = 0; i < GameUtils.MAP_HEIGHT * 3; i++) {
            var line = new ArrayList<Character>();
            for (int j = 0; j < GameUtils.MAP_WIDTH * 3; j++) {
                line.add(' ');
            }
            maps.add(line);
        }

        for (int i = 0; i < GameUtils.MAP_HEIGHT; i++) {
            for (int j = 0; j < GameUtils.MAP_WIDTH; j++) {
                GameMapPixel.updateMap(maps, new Coordinate(j, i), masterMap.get(i).get(j));
            }
        }

        return maps;
    }

    public static GameMap fromRawMap(ArrayList<ArrayList<Character>> maps) {
        ArrayList<ArrayList<GameMapPixel>> masterMap = new ArrayList<>();

        for (int i = 0; i < GameUtils.MAP_HEIGHT; i++) {
            ArrayList<GameMapPixel> line = new ArrayList<>();
            for (int j = 0; j < GameUtils.MAP_WIDTH; j++) {
                var pixel = GameMapPixel.getPixel(maps, new Coordinate(j, i));
                line.add(pixel);
            }
            masterMap.add(line);
        }

        return new GameMap(masterMap);
    }

    private void load(String path) {
    }

    public GameMap clone() {
        var newMap = new GameMap();
        var newMasterMap = new ArrayList<ArrayList<GameMapPixel>>();

        for (int i = 0; i < masterMap.size(); i += 1) {
            var characters = masterMap.get(i);
            var line = new ArrayList<GameMapPixel>();
            line.addAll(characters);
            newMasterMap.add(line);
        }

        newMap.masterMap = newMasterMap;

        return newMap;
    }

    public void fill(GameObject object) {
        switch (object.debugName) {
            case "Player":
                if (object.coors.size() == 1) {
                    masterMap.get(object.coors.get(0).y).set(object.coors.get(0).x, new GameMapPixel(Player.symbol, PixelState.full));
                    return;
                }
                for (int i = 0; i < object.coors.size(); i++) {
                    var coor = object.coors.get(i);
                    Coordinate prev = null;
                    Coordinate next = null;
                    var map = masterMap.get(coor.y);

                    if (i > 0) {
                        prev = object.coors.get(i - 1);
                    }
                    if (i < object.coors.size() - 1) {
                        next = object.coors.get(i + 1);
                    }

                    if (i == 0) {
                        prev = predictDirection(coor, GameUtils.getDirection(coor, next));
                    }
                    if (i == object.coors.size() - 1) {
                        next = predictDirection(coor, GameUtils.getDirection(coor, prev));
                    }

                    var prevDirection = GameUtils.getDirection(prev, coor);
                    if (next == null) {
                        if (prevDirection == MoveDirection.left) {
                            map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.full));
                        }
                        masterMap.set(coor.y, map);
                        return;
                    }
                    var nextDirection = GameUtils.getDirection(coor, next);

                    if (prevDirection == MoveDirection.left && nextDirection == MoveDirection.up) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.topRight));
                    }
                    if (prevDirection == MoveDirection.left && nextDirection == MoveDirection.down) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.bottomRight));
                    }
                    if (prevDirection == MoveDirection.right && nextDirection == MoveDirection.up) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.topLeft));
                    }
                    if (prevDirection == MoveDirection.right && nextDirection == MoveDirection.down) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.bottomLeft));
                    }
                    if (prevDirection == MoveDirection.down && nextDirection == MoveDirection.left) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.topLeft));
                    }
                    if (prevDirection == MoveDirection.down && nextDirection == MoveDirection.right) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.topRight));
                    }
                    if (prevDirection == MoveDirection.up && nextDirection == MoveDirection.left) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.bottomLeft));
                    }
                    if (prevDirection == MoveDirection.up && nextDirection == MoveDirection.right) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.bottomRight));
                    }
                    if (prevDirection == MoveDirection.up && nextDirection == MoveDirection.up) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.vertical));
                    }
                    if (prevDirection == MoveDirection.down && nextDirection == MoveDirection.down) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.vertical));
                    }
                    if (prevDirection == MoveDirection.left && nextDirection == MoveDirection.left) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.horizontal));
                    }
                    if (prevDirection == MoveDirection.right && nextDirection == MoveDirection.right) {
                        map.set(coor.x, new GameMapPixel(Player.symbol, PixelState.horizontal));
                    }

                    masterMap.set(coor.y, map);
                }
                break;
            case "Worm":
                for (Coordinate coor : object.coors) {
                    var map = masterMap.get(coor.y);
                    var worm = (Worm) object;

                    switch (worm.moveDirection) {
                        case topLeft:
                            map.set(coor.x, new GameMapPixel(Worm.symbol, PixelState.topLeft));
                            break;
                        case topRight:
                            map.set(coor.x, new GameMapPixel(Worm.symbol, PixelState.topRight));
                            break;
                        case bottomLeft:
                            map.set(coor.x, new GameMapPixel(Worm.symbol, PixelState.bottomLeft));
                            break;
                        case bottomRight:
                            map.set(coor.x, new GameMapPixel(Worm.symbol, PixelState.bottomRight));
                            break;
                    }
                }
                break;
            case "Beetle":
                for (Coordinate coor : object.coors) {
                    var map = masterMap.get(coor.y);
                    var beetle = (Beetle) object;

                    switch (beetle.moveDirection) {
                        case topLeft:
                            map.set(coor.x, new GameMapPixel(Beetle.symbol, PixelState.topLeft));
                            break;
                        case topRight:
                            map.set(coor.x, new GameMapPixel(Beetle.symbol, PixelState.topRight));
                            break;
                        case bottomLeft:
                            map.set(coor.x, new GameMapPixel(Beetle.symbol, PixelState.bottomLeft));
                            break;
                        case bottomRight:
                            map.set(coor.x, new GameMapPixel(Beetle.symbol, PixelState.bottomRight));
                            break;
                    }
                }
                break;
            case "Grass":
                for (Coordinate coor : object.coors) {
                    var map = masterMap.get(coor.y);
                    map.set(coor.x, new GameMapPixel(Grass.symbol, PixelState.full));
                    masterMap.set(coor.y, map);
                }
                break;
            case "Wall":
                for (Coordinate coor : object.coors) {
                    var map = masterMap.get(coor.y);
                    map.set(coor.x, new GameMapPixel(Wall.symbol, PixelState.full));
                    masterMap.set(coor.y, map);
                }
                break;
        }
    }

    private Coordinate predictDirection(Coordinate curr, MoveDirection avoidDirection) {
        Coordinate prev = null;
        if (avoidDirection != MoveDirection.left) {
            var leftCoor = new Coordinate(curr.x - 1, curr.y);
            var leftPixel = get(leftCoor);
            if (leftPixel.symbol == Wall.symbol || leftPixel.symbol == Grass.symbol) {
                prev = leftCoor;
            }
        }
        if (avoidDirection != MoveDirection.right) {
            var rightCoor = new Coordinate(curr.x + 1, curr.y);
            var rightPixel = get(rightCoor);
            if (rightPixel.symbol == Wall.symbol || rightPixel.symbol == Grass.symbol) {
                prev = rightCoor;
            }
        }
        if (avoidDirection != MoveDirection.up) {
            var upCoor = new Coordinate(curr.x, curr.y - 1);
            var upPixel = get(upCoor);
            if (upPixel.symbol == Wall.symbol || upPixel.symbol == Grass.symbol) {
                prev = upCoor;
            }
        }
        if (avoidDirection != MoveDirection.down) {
            var downCoor = new Coordinate(curr.x, curr.y + 1);
            var downPixel = get(downCoor);
            if (downPixel.symbol == Wall.symbol || downPixel.symbol == Grass.symbol) {
                prev = downCoor;
            }
        }

        return prev;
    }

    public void relativeFloodFill(Coordinate coordinate, MoveDirection direction) {
        var firstRawMap = getRawMap();
        var secondRawMap = getRawMap();
        var absoluteCoordinate = new Coordinate(coordinate.x * 3, coordinate.y * 3);

        var pixel = masterMap.get(coordinate.y).get(coordinate.x);

        Coordinate startFirstCoordinate = null;
        Coordinate startSecondCoordinate = null;

        switch (pixel.state) {
            case bottomLeft:
                startFirstCoordinate = new Coordinate(absoluteCoordinate.x, absoluteCoordinate.y + 2);
                startSecondCoordinate = new Coordinate(absoluteCoordinate.x + 2, absoluteCoordinate.y);
                break;
            case bottomRight:
                startFirstCoordinate = new Coordinate(absoluteCoordinate.x + 2, absoluteCoordinate.y + 2);
                startSecondCoordinate = new Coordinate(absoluteCoordinate.x, absoluteCoordinate.y);
                break;
            case topLeft:
                startFirstCoordinate = new Coordinate(absoluteCoordinate.x, absoluteCoordinate.y);
                startSecondCoordinate = new Coordinate(absoluteCoordinate.x + 2, absoluteCoordinate.y + 2);
                break;
            case topRight:
                startFirstCoordinate = new Coordinate(absoluteCoordinate.x + 2, absoluteCoordinate.y);
                startSecondCoordinate = new Coordinate(absoluteCoordinate.x, absoluteCoordinate.y + 2);
                break;
            case horizontal:
                startFirstCoordinate = new Coordinate(absoluteCoordinate.x + 1, absoluteCoordinate.y);
                startSecondCoordinate = new Coordinate(absoluteCoordinate.x + 1, absoluteCoordinate.y + 2);
                break;
            case vertical:
                startFirstCoordinate = new Coordinate(absoluteCoordinate.x, absoluteCoordinate.y + 1);
                startSecondCoordinate = new Coordinate(absoluteCoordinate.x + 2, absoluteCoordinate.y + 1);
                break;
            case full:
                startFirstCoordinate = new Coordinate(absoluteCoordinate.x + 1, absoluteCoordinate.y + 1);
                startSecondCoordinate = new Coordinate(absoluteCoordinate.x + 1, absoluteCoordinate.y + 1);
                break;
                
        }

        var firstResult = loopFloodFill(firstRawMap, startFirstCoordinate.x, startFirstCoordinate.y);
        var secondResult = loopFloodFill(secondRawMap, startSecondCoordinate.x, startSecondCoordinate.y);


        transformRawMap(firstRawMap, Player.symbol, Grass.symbol);
        transformRawMap(secondRawMap, Player.symbol, Grass.symbol);

        var firstMasterMap = fromRawMap(firstResult.result);
        var secondMasterMap = fromRawMap(secondResult.result);

        if (!firstResult.isSuccess) {
            if (secondMasterMap.evaluatePercent(Grass.symbol) > 80) {
                masterMap = firstMasterMap.masterMap;
            } else {
                masterMap = secondMasterMap.masterMap;
            }
            return;
        }
        if (!secondResult.isSuccess) {
            if (firstMasterMap.evaluatePercent(Grass.symbol) > 80) {
                masterMap = secondMasterMap.masterMap;
            } else {
                masterMap = firstMasterMap.masterMap;
            }
            return;
        }


        var cmp = firstMasterMap.compareTo(secondMasterMap);

        if (cmp == 0) {
            masterMap = firstMasterMap.masterMap;
        } else if (cmp == 1) {
            masterMap = secondMasterMap.masterMap;
        } else {
            masterMap = firstMasterMap.masterMap;
        }
    }

    private boolean isValid(ArrayList<ArrayList<Character>> character, Integer x, Integer y) {
        if (character.get(y).get(x) == Grass.symbol || character.get(y).get(x) == Wall.symbol || character.get(y).get(x) == Player.symbol)
            return false;
        if (x <= 2 || x >= GameUtils.MAP_WIDTH * 3 - 3) return false;
        if (y <= 2 || y >= GameUtils.MAP_HEIGHT * 3 - 3) return false;
        return true;
    }

    private FloodFillResult loopFloodFill(ArrayList<ArrayList<Character>> character, Integer x, Integer y) {
        ArrayList<ArrayList<Character>> replicateCharacter = new ArrayList<>();
        for (int i = 0; i < character.size(); i++) {
            replicateCharacter.add(new ArrayList<>());
            for (int j = 0; j < character.get(i).size(); j++) {
                replicateCharacter.get(i).add(character.get(i).get(j));
            }
        }

        Vector<Coordinate> queue = new Vector<>();
        queue.add(new Coordinate(x, y));
        if (character.get(y).get(x) == Worm.symbol) {
            return new FloodFillResult(false, replicateCharacter);
        }
        character.get(y).set(x, Player.symbol);

        while (queue.size() > 0) {
            var last = queue.get(queue.size() - 1);
            queue.remove(queue.size() - 1);
            if (isValid(character, last.x - 1, last.y)) {
                if (character.get(last.y).get(last.x - 1) == Worm.symbol || character.get(last.y).get(last.x - 1) == Beetle.symbol) {
                    return new FloodFillResult(false, replicateCharacter);
                }
                character.get(last.y).set(last.x - 1, Player.symbol);
                queue.add(new Coordinate(last.x - 1, last.y));
            }
            if (isValid(character, last.x + 1, last.y)) {
                if (character.get(last.y).get(last.x + 1) == Worm.symbol || character.get(last.y).get(last.x + 1) == Beetle.symbol) {
                    return new FloodFillResult(false, replicateCharacter);
                }
                character.get(last.y).set(last.x + 1, Player.symbol);
                queue.add(new Coordinate(last.x + 1, last.y));
            }
            if (isValid(character, last.x, last.y - 1)) {
                if (character.get(last.y - 1).get(last.x) == Worm.symbol || character.get(last.y - 1).get(last.x) == Beetle.symbol) {
                    return new FloodFillResult(false, replicateCharacter);
                }
                character.get(last.y - 1).set(last.x, Player.symbol);
                queue.add(new Coordinate(last.x, last.y - 1));
            }
            if (isValid(character, last.x, last.y + 1)) {
                if (character.get(last.y + 1).get(last.x) == Worm.symbol || character.get(last.y + 1).get(last.x) == Beetle.symbol) {
                    return new FloodFillResult(false, replicateCharacter);
                }
                character.get(last.y + 1).set(last.x, Player.symbol);
                queue.add(new Coordinate(last.x, last.y + 1));
            }
        }

        return new FloodFillResult(true, character);
    }

    // Recursive way make JVM overflow (This method is unusable)
//    private void floodFill(ArrayList<ArrayList<Character>> characters, Integer x, Integer y) {
//        if (x <= 2 || x >= 188 || y <= 2 || y >= 92)
//            return;
//        if (characters.get(y).get(x) == Player.symbol || characters.get(y).get(x) == Grass.symbol)
//            return;
//        var line = characters.get(y);
//        line.set(x, Player.symbol);
//        characters.set(y, line);
//
//        floodFill(characters, x + 1, y);
//        floodFill(characters, x - 1, y);
//        floodFill(characters, x, y + 1);
//        floodFill(characters, x, y - 1);
//    }

    public Integer evaluatePercent(Character symbol) {
        Integer percent = 0;
        for (int i = 0; i < masterMap.size(); i++) {
            for (int j = 0; j < masterMap.get(0).size(); j++) {
                if (masterMap.get(i).get(j).symbol == symbol) {
                    percent += 1;
                }
            }
        }

        return Math.round((percent.floatValue() / ((GameUtils.MAP_WIDTH - 1) * (GameUtils.MAP_HEIGHT - 1))) * 100 / 95 * 100);
    }

    public Integer compareTo(GameMap map) {
        if (this.evaluatePercent(Grass.symbol) > map.evaluatePercent(Grass.symbol)) {
            return 1;
        } else if (this.evaluatePercent(Grass.symbol) == map.evaluatePercent(Grass.symbol)) {
            return 0;
        } else {
            return -1;
        }
    }

    public ArrayList<GameObject> parse(App app) {
        var objects = new ArrayList<GameObject>();
        Wall wall = null;
        Grass grass = null;
        Player player = null;
        ArrayList<GameObject> enemies = new ArrayList<>();
        for (int i = 0; i < masterMap.size(); i += 1) {
            for (int j = 0; j < masterMap.get(0).size(); j += 1) {
                var character = masterMap.get(i).get(j);
                if (character.symbol == Player.symbol) {
                    if (player == null) {
                        player = new Player(app);
                    }
                    player.addCoor(new Coordinate(j, i));
                }
                if (character.symbol == Grass.symbol) {
                    if (grass == null) {
                        grass = new Grass(app);
                    }
                    grass.addCoor(new Coordinate(j, i));
                }
                if (character.symbol == Wall.symbol) {
                    if (wall == null) {
                        wall = new Wall(app);
                    }
                    wall.addCoor(new Coordinate(j, i));
                }
                if (character.symbol == Worm.symbol) {
                    var worm = new Worm(app);
                    switch (character.state) {
                        case topLeft:
                            worm.moveDirection = EnemyMoveDirection.topLeft;
                            break;
                        case topRight:
                            worm.moveDirection = EnemyMoveDirection.topRight;
                            break;
                        case bottomLeft:
                            worm.moveDirection = EnemyMoveDirection.bottomLeft;
                            break;
                        case bottomRight:
                            worm.moveDirection = EnemyMoveDirection.bottomRight;
                            break;
                    }
                    worm.coors = new ArrayList<>();
                    worm.addCoor(new Coordinate(j, i));
                    enemies.add(worm);
                }
                if (character.symbol == Beetle.symbol) {
                    var beetle = new Beetle(app);
                    switch (character.state) {
                        case topLeft:
                            beetle.moveDirection = EnemyMoveDirection.topLeft;
                            break;
                        case topRight:
                            beetle.moveDirection = EnemyMoveDirection.topRight;
                            break;
                        case bottomLeft:
                            beetle.moveDirection = EnemyMoveDirection.bottomLeft;
                            break;
                        case bottomRight:
                            beetle.moveDirection = EnemyMoveDirection.bottomRight;
                            break;
                    }
                    beetle.coors = new ArrayList<>();
                    beetle.addCoor(new Coordinate(j, i));
                    enemies.add(beetle);
                }
            }
        }

        if (wall != null) objects.add(wall);
        if (grass != null) objects.add(grass);
        if (player != null) objects.add(player);
        objects.addAll(enemies);

        return objects;
    }

    public void clear() {
        var newMasterMap = new ArrayList<ArrayList<Character>>();
        for (int i = 0; i < GameUtils.MAP_HEIGHT * 3; i++) {
            var line = new ArrayList<Character>();
            for (int j = 0; j < GameUtils.MAP_WIDTH * 3; j++) {
                line.add(' ');
            }
            newMasterMap.add(line);
        }
        for (int i = 0; i < GameUtils.MAP_HEIGHT; i++) {
            char symbol = ' ';
            for (int j = 0; j < GameUtils.MAP_WIDTH; j++) {
                if ((j == 0 || j == GameUtils.MAP_WIDTH - 1) && symbol == ' ') {
                    symbol = 'X';
                } else {
                    symbol = ' ';
                }
                if (i == 0 || i == GameUtils.MAP_HEIGHT - 1) symbol = 'X';

                GameMapPixel.updateMap(newMasterMap, new Coordinate(j, i), symbol);
            }
        }
        masterMap = GameMap.fromRawMap(newMasterMap).masterMap;
    }

    public void transformRawMap(ArrayList<ArrayList<Character>> characters, Character from, Character to) {
        for (int i = 0; i < characters.size(); i++) {
            for (int j = 0; j < characters.get(0).size(); j++) {
                var map = characters.get(i);
                if (map.get(j) == from) {
                    map.set(j, to);
                    characters.set(i, map);
                }
            }
        }
    }


    public void transform(Character from, Character to) {
        for (int i = 0; i < masterMap.size(); i++) {
            for (int j = 0; j < masterMap.get(0).size(); j++) {
                var map = masterMap.get(i);
                if (map.get(j).symbol == from) {
                    map.set(j, new GameMapPixel(to, PixelState.full));
                    masterMap.set(i, map);
                }
            }
        }
    }

    public void transform(Coordinate point, Character symbol) {
        masterMap.get(point.y).set(point.x, new GameMapPixel(symbol, PixelState.full));
    }
}
