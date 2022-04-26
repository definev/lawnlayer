package lawnlayer.gameObject;

import java.util.ArrayList;
import java.util.Random;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.EnemyMoveDirection;
import lawnlayer.utils.GameUtils;
import processing.core.PApplet;

public class Beetle extends GameObject {
    public Beetle(PApplet app) {
        super(app, "Beetle");
        needToCheckCollision = true;
    }

    public static final Character symbol = 'b';

    EnemyMoveDirection moveDirection = EnemyMoveDirection.topLeft;
    Integer frameCount = 0;
    Integer speed = 20;

    private void onFrameUpdate() {
        var canRefresh = frameCount % App.FPS == App.FPS - 1;
        if (canRefresh) {
            frameCount = 0;
        } else {
            frameCount += 1;
        }
    }

    @Override
    protected void initCoor() {
        coors.add(((App) app).masterMap.randomizeLocation());
        moveDirection = EnemyMoveDirection.values()[new Random().nextInt(EnemyMoveDirection.values().length)];
    }

    @Override
    void onCollision(GameObject object, ArrayList<Coordinate> points) {
        if (object.debugName == "Wall" || object.debugName == "Grass") {
            var last = coors.get(0);
            coors = new ArrayList<>();
            switch (moveDirection) {
                case topLeft:
                    coors.add(new Coordinate(last.x + 1, last.y + 1));
                    break;
                case topRight:
                    coors.add(new Coordinate(last.x - 1, last.y + 1));
                    break;
                case bottomRight:
                    coors.add(new Coordinate(last.x - 1, last.y - 1));
                    break;
                case bottomLeft:
                    coors.add(new Coordinate(last.x + 1, last.y - 1));
                    break;
            }
            var moveDirections =
                    EnemyMoveDirection.values();
            ArrayList<EnemyMoveDirection> remainDirection = new ArrayList<>();
            for (EnemyMoveDirection direction : moveDirections) {
                if (direction != moveDirection) {
                    remainDirection.add(direction);
                }
            }
            moveDirection = remainDirection.get((new Random()).nextInt(remainDirection.size()));
        }
        if (object.debugName == "Grass") {
            for (Coordinate point : points) {
                object.coors.remove(point);
            }
        }
    }

    @Override
    protected void drawCoors() {
        for (Coordinate coor : coors) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.beetle, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }

    @Override
    public void draw() {
        drawCoors();
        onWormMove();
        collisionCheck();
        onFrameUpdate();
    }

    private void onWormMove() {
        if (frameCount % speed == 0) {
            var last = coors.get(0);
            coors = new ArrayList<>();
            switch (moveDirection) {
                case topLeft:
                    coors.add(new Coordinate(last.x - 1, last.y - 1));
                    break;
                case topRight:
                    coors.add(new Coordinate(last.x + 1, last.y - 1));
                    break;
                case bottomRight:
                    coors.add(new Coordinate(last.x + 1, last.y + 1));
                    break;
                case bottomLeft:
                    coors.add(new Coordinate(last.x - 1, last.y + 1));
                    break;
            }
        }
    }
}
