package lawnlayer.utils.gameMap;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import lawnlayer.App;
import lawnlayer.gameObject.enemy.AntEnemy;
import lawnlayer.gameObject.enemy.BeetleEnermy;
import lawnlayer.gameObject.powerup.SlowEnemyPowerUp;
import lawnlayer.gameObject.enemy.WormEnemy;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.gameObject.staticObj.Grass;
import lawnlayer.gameObject.Player;
import lawnlayer.gameObject.staticObj.Wall;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.direction.EnemyMoveDirection;
import lawnlayer.utils.direction.MoveDirection;

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

    public GameMapPixel get(Coordinate coordinatedinate) {
        return masterMap.get(coordinatedinate.y).get(coordinatedinate.x);
    }

    public Coordinate randomizeLocation() {
        Random rand = new Random();
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
            ArrayList<Character> line = new ArrayList<Character>();
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
                GameMapPixel pixel = GameMapPixel.getPixel(maps, new Coordinate(j, i));
                line.add(pixel);
            }
            masterMap.add(line);
        }

        return new GameMap(masterMap);
    }

    private void load(String path) {
    }

    public GameMap clone() {
        GameMap newMap = new GameMap();
        ArrayList<ArrayList<GameMapPixel>> newMasterMap = new ArrayList<ArrayList<GameMapPixel>>();

        for (int i = 0; i < masterMap.size(); i += 1) {
            ArrayList<GameMapPixel> characters = masterMap.get(i);
            ArrayList<GameMapPixel> line = new ArrayList<GameMapPixel>();
            line.addAll(characters);
            newMasterMap.add(line);
        }

        newMap.masterMap = newMasterMap;

        return newMap;
    }

    public void fill(BaseGameObject object) {
        switch (object.className) {
            case "Player":
                if (object.coordinatedinates.size() == 1) {
                    masterMap.get(object.coordinatedinates.get(0).y).set(object.coordinatedinates.get(0).x, new GameMapPixel(Player.symbol, PixelState.full));
                    return;
                }
                for (int i = 0; i < object.coordinatedinates.size(); i++) {
                    Coordinate coordinate = object.coordinatedinates.get(i);
                    Coordinate prev = null;
                    Coordinate next = null;
                    ArrayList<GameMapPixel> map = masterMap.get(coordinate.y);

                    if (i > 0) {
                        prev = object.coordinatedinates.get(i - 1);
                    }
                    if (i < object.coordinatedinates.size() - 1) {
                        next = object.coordinatedinates.get(i + 1);
                    }

                    if (i == 0) {
                        prev = predictDirection(coordinate, GameUtils.getDirection(MoveDirection.left, coordinate, next));
                    }
                    if (i == object.coordinatedinates.size() - 1) {
                        next = predictDirection(coordinate, GameUtils.getDirection(MoveDirection.left, coordinate, prev));
                    }

                    MoveDirection prevDirection = GameUtils.getDirection(MoveDirection.left, prev, coordinate);
                    if (next == null) {
                        if (prevDirection == MoveDirection.left) {
                            map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.full));
                        }
                        masterMap.set(coordinate.y, map);
                        return;
                    }
                    MoveDirection nextDirection = GameUtils.getDirection(prevDirection, coordinate, next);

                    if (prevDirection == MoveDirection.left && nextDirection == MoveDirection.up) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.topRight));
                    }
                    if (prevDirection == MoveDirection.left && nextDirection == MoveDirection.down) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.bottomRight));
                    }
                    if (prevDirection == MoveDirection.right && nextDirection == MoveDirection.up) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.topLeft));
                    }
                    if (prevDirection == MoveDirection.right && nextDirection == MoveDirection.down) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.bottomLeft));
                    }
                    if (prevDirection == MoveDirection.down && nextDirection == MoveDirection.left) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.topLeft));
                    }
                    if (prevDirection == MoveDirection.down && nextDirection == MoveDirection.right) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.topRight));
                    }
                    if (prevDirection == MoveDirection.up && nextDirection == MoveDirection.left) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.bottomLeft));
                    }
                    if (prevDirection == MoveDirection.up && nextDirection == MoveDirection.right) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.bottomRight));
                    }
                    if (prevDirection == MoveDirection.up && nextDirection == MoveDirection.up) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.vertical));
                    }
                    if (prevDirection == MoveDirection.down && nextDirection == MoveDirection.down) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.vertical));
                    }
                    if (prevDirection == MoveDirection.left && nextDirection == MoveDirection.left) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.horizontal));
                    }
                    if (prevDirection == MoveDirection.right && nextDirection == MoveDirection.right) {
                        map.set(coordinate.x, new GameMapPixel(Player.symbol, PixelState.horizontal));
                    }

                    masterMap.set(coordinate.y, map);
                }
                break;
            case "WormEnemy":
                for (Coordinate coordinate : object.coordinatedinates) {
                    ArrayList<GameMapPixel> map = masterMap.get(coordinate.y);
                    WormEnemy worm = (WormEnemy) object;

                    switch (worm.moveDirection) {
                        case topLeft:
                            map.set(coordinate.x, new GameMapPixel(WormEnemy.symbol, PixelState.topLeft));
                            break;
                        case topRight:
                            map.set(coordinate.x, new GameMapPixel(WormEnemy.symbol, PixelState.topRight));
                            break;
                        case bottomLeft:
                            map.set(coordinate.x, new GameMapPixel(WormEnemy.symbol, PixelState.bottomLeft));
                            break;
                        case bottomRight:
                            map.set(coordinate.x, new GameMapPixel(WormEnemy.symbol, PixelState.bottomRight));
                            break;
                    }
                }
                break;
            case "BeetleEnemy":
                for (Coordinate coordinate : object.coordinatedinates) {
                    ArrayList<GameMapPixel> map = masterMap.get(coordinate.y);
                    BeetleEnermy beetle = (BeetleEnermy) object;

                    switch (beetle.moveDirection) {
                        case topLeft:
                            map.set(coordinate.x, new GameMapPixel(BeetleEnermy.symbol, PixelState.topLeft));
                            break;
                        case topRight:
                            map.set(coordinate.x, new GameMapPixel(BeetleEnermy.symbol, PixelState.topRight));
                            break;
                        case bottomLeft:
                            map.set(coordinate.x, new GameMapPixel(BeetleEnermy.symbol, PixelState.bottomLeft));
                            break;
                        case bottomRight:
                            map.set(coordinate.x, new GameMapPixel(BeetleEnermy.symbol, PixelState.bottomRight));
                            break;
                    }
                }
                break;
            case "Grass":
                for (Coordinate coordinate : object.coordinatedinates) {
                    ArrayList<GameMapPixel> map = masterMap.get(coordinate.y);
                    map.set(coordinate.x, new GameMapPixel(Grass.symbol, PixelState.full));
                    masterMap.set(coordinate.y, map);
                }
                break;
            case "Wall":
                for (Coordinate coordinate : object.coordinatedinates) {
                    ArrayList<GameMapPixel> map = masterMap.get(coordinate.y);
                    map.set(coordinate.x, new GameMapPixel(Wall.symbol, PixelState.full));
                    masterMap.set(coordinate.y, map);
                }
                break;
            case "AntEnemy":
                for (Coordinate coordinate : object.coordinatedinates) {
                    ArrayList<GameMapPixel> map = masterMap.get(coordinate.y);
                    AntEnemy ant = (AntEnemy) object;
                    switch (ant.direction) {
                        case up:
                            map.set(coordinate.x, new GameMapPixel(AntEnemy.symbol, PixelState.topLeft));
                            break;
                        case down:
                            map.set(coordinate.x, new GameMapPixel(AntEnemy.symbol, PixelState.bottomLeft));
                            break;
                        case left:
                            map.set(coordinate.x, new GameMapPixel(AntEnemy.symbol, PixelState.topRight));
                            break;
                        case right:
                            map.set(coordinate.x, new GameMapPixel(AntEnemy.symbol, PixelState.bottomRight));
                            break;
                    }
                    masterMap.set(coordinate.y, map);
                }
                break;
            case "SlowEnemyPowerUp":
                for (Coordinate coordinate : object.coordinatedinates) {
                    ArrayList<GameMapPixel> map = masterMap.get(coordinate.y);
                    map.set(coordinate.x, new GameMapPixel(SlowEnemyPowerUp.symbol, PixelState.full));
                    masterMap.set(coordinate.y, map);
                }
                break;

        }
    }

    private Coordinate predictDirection(Coordinate curr, MoveDirection avoidDirection) {
        Coordinate prev = null;
        if (avoidDirection != MoveDirection.left) {
            Coordinate leftCoor = new Coordinate(curr.x - 1, curr.y);
            GameMapPixel leftPixel = get(leftCoor);
            if (leftPixel.symbol == Wall.symbol || leftPixel.symbol == Grass.symbol) {
                prev = leftCoor;
            }
        }
        if (avoidDirection != MoveDirection.right) {
            Coordinate rightCoor = new Coordinate(curr.x + 1, curr.y);
            GameMapPixel rightPixel = get(rightCoor);
            if (rightPixel.symbol == Wall.symbol || rightPixel.symbol == Grass.symbol) {
                prev = rightCoor;
            }
        }
        if (avoidDirection != MoveDirection.up) {
            Coordinate upCoor = new Coordinate(curr.x, curr.y - 1);
            GameMapPixel upPixel = get(upCoor);
            if (upPixel.symbol == Wall.symbol || upPixel.symbol == Grass.symbol) {
                prev = upCoor;
            }
        }
        if (avoidDirection != MoveDirection.down) {
            Coordinate downCoor = new Coordinate(curr.x, curr.y + 1);
            GameMapPixel downPixel = get(downCoor);
            if (downPixel.symbol == Wall.symbol || downPixel.symbol == Grass.symbol) {
                prev = downCoor;
            }
        }

        return prev;
    }

    public void relativeFloodFill(Coordinate coordinatedinate, MoveDirection direction) {
        ArrayList<ArrayList<Character>> firstRawMap = getRawMap();
        ArrayList<ArrayList<Character>> secondRawMap = getRawMap();
        Coordinate absoluteCoordinate = new Coordinate(coordinatedinate.x * 3, coordinatedinate.y * 3);

        GameMapPixel pixel = masterMap.get(coordinatedinate.y).get(coordinatedinate.x);

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

        FloodFillResult firstResult = floodFill(firstRawMap, startFirstCoordinate.x, startFirstCoordinate.y);
        FloodFillResult secondResult = floodFill(secondRawMap, startSecondCoordinate.x, startSecondCoordinate.y);


        transformRawMap(firstRawMap, Player.symbol, Grass.symbol);
        transformRawMap(secondRawMap, Player.symbol, Grass.symbol);

        GameMap firstMasterMap = fromRawMap(firstResult.result);
        GameMap secondMasterMap = fromRawMap(secondResult.result);

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


        Integer cmp = firstMasterMap.compareTo(secondMasterMap);

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

    private FloodFillResult floodFill(ArrayList<ArrayList<Character>> character, Integer x, Integer y) {
        ArrayList<ArrayList<Character>> replicateCharacter = new ArrayList<>();
        for (int i = 0; i < character.size(); i++) {
            replicateCharacter.add(new ArrayList<>());
            for (int j = 0; j < character.get(i).size(); j++) {
                replicateCharacter.get(i).add(character.get(i).get(j));
            }
        }

        Vector<Coordinate> queue = new Vector<>();
        queue.add(new Coordinate(x, y));
        if (character.get(y).get(x) == WormEnemy.symbol) {
            return new FloodFillResult(false, replicateCharacter);
        }
        character.get(y).set(x, Player.symbol);

        while (queue.size() > 0) {
            Coordinate last = queue.get(queue.size() - 1);
            queue.remove(queue.size() - 1);
            if (isValid(character, last.x - 1, last.y)) {
                if (character.get(last.y).get(last.x - 1) == WormEnemy.symbol || character.get(last.y).get(last.x - 1) == BeetleEnermy.symbol) {
                    return new FloodFillResult(false, replicateCharacter);
                }
                character.get(last.y).set(last.x - 1, Player.symbol);
                queue.add(new Coordinate(last.x - 1, last.y));
            }
            if (isValid(character, last.x + 1, last.y)) {
                if (character.get(last.y).get(last.x + 1) == WormEnemy.symbol || character.get(last.y).get(last.x + 1) == BeetleEnermy.symbol) {
                    return new FloodFillResult(false, replicateCharacter);
                }
                character.get(last.y).set(last.x + 1, Player.symbol);
                queue.add(new Coordinate(last.x + 1, last.y));
            }
            if (isValid(character, last.x, last.y - 1)) {
                if (character.get(last.y - 1).get(last.x) == WormEnemy.symbol || character.get(last.y - 1).get(last.x) == BeetleEnermy.symbol) {
                    return new FloodFillResult(false, replicateCharacter);
                }
                character.get(last.y - 1).set(last.x, Player.symbol);
                queue.add(new Coordinate(last.x, last.y - 1));
            }
            if (isValid(character, last.x, last.y + 1)) {
                if (character.get(last.y + 1).get(last.x) == WormEnemy.symbol || character.get(last.y + 1).get(last.x) == BeetleEnermy.symbol) {
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

    public ArrayList<BaseGameObject> parse(App app) {
        ArrayList<BaseGameObject> objects = new ArrayList<BaseGameObject>();
        Wall wall = null;
        Grass grass = null;
        Player player = null;
        ArrayList<BaseGameObject> enemies = new ArrayList<>();
        ArrayList<BaseGameObject> powerUps = new ArrayList<>();
        for (int i = 0; i < masterMap.size(); i += 1) {
            for (int j = 0; j < masterMap.get(0).size(); j += 1) {
                GameMapPixel character = masterMap.get(i).get(j);
                if (character.symbol == Player.symbol) {
                    if (player == null) {
                        player = new Player(app);
                    }
                    player.addCoordinate(new Coordinate(j, i));
                }
                if (character.symbol == Grass.symbol) {
                    if (grass == null) {
                        grass = new Grass(app);
                    }
                    grass.addCoordinate(new Coordinate(j, i));
                }
                if (character.symbol == Wall.symbol) {
                    if (wall == null) {
                        wall = new Wall(app);
                    }
                    wall.addCoordinate(new Coordinate(j, i));
                }
                if (character.symbol == WormEnemy.symbol) {
                    WormEnemy worm = new WormEnemy(app);
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
                    worm.coordinatedinates = new ArrayList<>();
                    worm.addCoordinate(new Coordinate(j, i));
                    enemies.add(worm);
                }
                if (character.symbol == BeetleEnermy.symbol) {
                    BeetleEnermy beetle = new BeetleEnermy(app);
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
                    beetle.coordinatedinates = new ArrayList<>();
                    beetle.addCoordinate(new Coordinate(j, i));
                    enemies.add(beetle);
                }
                if (character.symbol == AntEnemy.symbol) {
                    if (wall == null) {
                        wall = new Wall(app);
                    }
                    wall.addCoordinate(new Coordinate(j, i));
                    AntEnemy ant = new AntEnemy(app);
                    switch (character.state) {
                        case topLeft:
                            ant.direction = MoveDirection.up;
                            break;
                        case bottomLeft:
                            ant.direction = MoveDirection.down;
                            break;
                        case topRight:
                            ant.direction = MoveDirection.left;
                            break;
                        case bottomRight:
                            ant.direction = MoveDirection.right;
                            break;
                    }
                    ant.coordinatedinates = new ArrayList<>();
                    ant.addCoordinate(new Coordinate(j, i));
                    enemies.add(ant);
                }
                if (character.symbol == SlowEnemyPowerUp.symbol) {
                    SlowEnemyPowerUp powerUp = new SlowEnemyPowerUp(app);
                    powerUp.coordinatedinates = new ArrayList<>();
                    powerUp.addCoordinate(new Coordinate(j, i));
                    powerUps.add(powerUp);
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
        ArrayList<ArrayList<Character>> newMasterMap = new ArrayList<ArrayList<Character>>();
        for (int i = 0; i < GameUtils.MAP_HEIGHT * 3; i++) {
            ArrayList<Character> line = new ArrayList<Character>();
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
                ArrayList<Character> map = characters.get(i);
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
                ArrayList<GameMapPixel> map = masterMap.get(i);
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
