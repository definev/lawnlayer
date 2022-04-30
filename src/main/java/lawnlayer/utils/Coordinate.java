package lawnlayer.utils;


import java.util.Objects;

import lawnlayer.utils.direction.MoveDirection;

public class Coordinate {
    public final Integer x;
    public final Integer y;

    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate move(MoveDirection direction) {
        switch (direction) {
            case up:
                return new Coordinate(x, y - 1);
            case down:
                return new Coordinate(x, y + 1);
            case left:
                return new Coordinate(x - 1, y);
            case right:
                return new Coordinate(x + 1, y);
            case none:
                return this;
        }

        return this;
    }

    public Coordinate move(MoveDirection direction, Integer times) {
        Coordinate coordinate = this;
        for (int i = 0; i < times; i++) {
            coordinate = coordinate.move(direction);
        }

        return coordinate;
    }

    public Boolean isOutOfBounds() {
        return x < 0 || x >= GameUtils.MAP_WIDTH || y < 0 || y >= GameUtils.MAP_HEIGHT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x.equals(that.x) && y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean isEdge() {
        if ((x == 0 || x == GameUtils.MAP_WIDTH - 1) && (y == 0 || y == GameUtils.MAP_HEIGHT - 1)) return true;
        return false;
    }
}
