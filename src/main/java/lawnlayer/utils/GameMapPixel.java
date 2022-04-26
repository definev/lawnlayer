package lawnlayer.utils;

import java.util.ArrayList;

enum PixelState {
    vertical,
    horizontal,
    idle,
    full,
    topLeft,
    topRight,
    bottomLeft,
    bottomRight,
}

public class GameMapPixel {
    public PixelState state;
    public Character symbol;

    public GameMapPixel(Character symbol, PixelState state) {
        this.symbol = symbol;
        this.state = state;

        topLeftCoors[0] = new Coordinate(1, 0);
        topLeftCoors[1] = new Coordinate(1, 1);
        topLeftCoors[2] = new Coordinate(0, 1);

        topRightCoors[0] = new Coordinate(1, 0);
        topRightCoors[1] = new Coordinate(1, 1);
        topRightCoors[2] = new Coordinate(2, 1);

        bottomLeftCoors[0] = new Coordinate(1, 1);
        bottomLeftCoors[1] = new Coordinate(0, 1);
        bottomLeftCoors[2] = new Coordinate(1, 2);

        bottomRightCoors[0] = new Coordinate(1, 1);
        bottomRightCoors[1] = new Coordinate(2, 1);
        bottomRightCoors[2] = new Coordinate(1, 2);

        horizontalCoors[0] = new Coordinate(0, 1);
        horizontalCoors[1] = new Coordinate(1, 1);
        horizontalCoors[2] = new Coordinate(2, 1);

        verticalCoors[0] = new Coordinate(1, 0);
        verticalCoors[1] = new Coordinate(1, 1);
        verticalCoors[2] = new Coordinate(1, 2);
    }

    static private Coordinate[] topLeftCoors = new Coordinate[3];
    static private Coordinate[] topRightCoors = new Coordinate[3];
    static private Coordinate[] bottomLeftCoors = new Coordinate[3];
    static private Coordinate[] bottomRightCoors = new Coordinate[3];
    static private Coordinate[] horizontalCoors = new Coordinate[3];
    static private Coordinate[] verticalCoors = new Coordinate[3];

    static private boolean validPosition(Character symbol, ArrayList<ArrayList<Character>> map, Coordinate[] coors) {
        for (Coordinate coor : coors) {
            if (map.get(coor.y).get(coor.x) != symbol) {
                return false;
            }
        }
        return true;
    }

    static public GameMapPixel fromRawMap(ArrayList<ArrayList<Character>> map) {
        PixelState state = null;
        boolean isFull = true;
        Character symbol = ' ';

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (map.get(i).get(j) != ' ') {
                    symbol = map.get(i).get(j);
                    break;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (map.get(i).get(j) != symbol) {
                    isFull = false;
                    break;
                }
            }
        }

        if (isFull) {
            state = PixelState.full;
        } else {
            if (validPosition(symbol, map, topLeftCoors)) {
                state = PixelState.topLeft;
            }
            if (validPosition(symbol, map, bottomLeftCoors)) {
                state = PixelState.bottomLeft;
            }
            if (validPosition(symbol, map, topRightCoors)) {
                state = PixelState.topRight;
            }
            if (validPosition(symbol, map, bottomRightCoors)) {
                state = PixelState.bottomRight;
            }
            if (validPosition(symbol, map, horizontalCoors)) {
                state = PixelState.horizontal;
            }
            if (validPosition(symbol, map, verticalCoors)) {
                state = PixelState.vertical;
            }

            if (state == null && map.get(1).get(1) == symbol) {
                state = PixelState.idle;
            }
            if (state == null) {
                state = PixelState.full;
            }
        }


        return new GameMapPixel(symbol, state);
    }

    public static void updateMap(ArrayList<ArrayList<Character>> newMasterMap, Coordinate coordinate, Character symbol) {
        var initX = coordinate.x * 3;
        var initY = coordinate.y * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newMasterMap.get(initY + i).set(initX + j, symbol);
            }
        }
    }

    public static GameMapPixel getPixel(ArrayList<ArrayList<Character>> newMasterMap, Coordinate coordinate) {
        var initX = coordinate.x * 3;
        var initY = coordinate.y * 3;

        GameMapPixel pixel = null;
        ArrayList<ArrayList<Character>> characters = new ArrayList();

        for (int i = 0; i < 3; i++) {
            ArrayList<Character> character = new ArrayList();
            for (int j = 0; j < 3; j++) {
                character.add(' ');
            }
            characters.add(character);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                characters.get(i).set(j, newMasterMap.get(initY + i).get(initX + j));
            }
        }

        return GameMapPixel.fromRawMap(characters);
    }

    public static void updateMap(ArrayList<ArrayList<Character>> newMasterMap, Coordinate coordinate, GameMapPixel pixel) {
        var initX = coordinate.x * 3;
        var initY = coordinate.y * 3;

        switch (pixel.state) {
            case full:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, pixel.symbol);
                    }
                }
                return;
            case idle:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, ' ');
                    }
                }
                newMasterMap.get(initY + 1).set(initX + 1, pixel.symbol);
            case topLeft:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, ' ');
                    }
                }
                newMasterMap.get(initY + 0).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 0, pixel.symbol);
                return;
            case topRight:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, ' ');
                    }
                }
                newMasterMap.get(initY + 0).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 2, pixel.symbol);
                return;
            case bottomLeft:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, ' ');
                    }
                }
                newMasterMap.get(initY + 1).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 2).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 0, pixel.symbol);
                return;
            case bottomRight:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, ' ');
                    }
                }
                newMasterMap.get(initY + 1).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 2).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 2, pixel.symbol);
                return;
            case horizontal:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, ' ');
                    }
                }
                newMasterMap.get(initY + 1).set(initX + 0, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 2, pixel.symbol);
                return;
            case vertical:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newMasterMap.get(initY + i).set(initX + j, ' ');
                    }
                }
                newMasterMap.get(initY + 0).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 1).set(initX + 1, pixel.symbol);
                newMasterMap.get(initY + 2).set(initX + 1, pixel.symbol);
                return;
        }
    }
}
