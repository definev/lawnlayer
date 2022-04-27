package lawnlayer.gameObject;

import java.util.ArrayList;
import java.util.Random;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.EnemyMoveDirection;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.MoveDirection;
import processing.core.PApplet;

public class Worm extends GameObject {
    public Worm(PApplet app) {
        super(app, "Worm");
        needToCheckCollision = true;
    }

    public static final Character symbol = 'w';

    public EnemyMoveDirection moveDirection = EnemyMoveDirection.topLeft;
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
        var point = points.get(0);
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

            if (point.isEdge()) {
                moveDirection = GameUtils.getOppositeMove(moveDirection);
            } else {
                var game = (App) app;

                var bottomValid = false;
                var bottomLast = point.move(MoveDirection.down);
                var upValid = false;
                var upLast = point.move(MoveDirection.up);
                var leftValid = false;
                var leftLast = point.move(MoveDirection.left);
                var rightValid = false;
                var rightLast = point.move(MoveDirection.right);

                if (!bottomLast.isOutOfBounds()) {
                    var pixel = game.masterMap.get(bottomLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        bottomValid = true;
                    }
                }
                if (!upLast.isOutOfBounds()) {
                    var pixel = game.masterMap.get(upLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        upValid = true;
                    }
                }
                if (!leftLast.isOutOfBounds()) {
                    var pixel = game.masterMap.get(leftLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        leftValid = true;
                    }
                }
                if (!rightLast.isOutOfBounds()) {
                    var pixel = game.masterMap.get(rightLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        rightValid = true;
                    }
                }
                if (bottomValid && rightValid) {
                    moveDirection = GameUtils.getOppositeMove(moveDirection);
                } else if (bottomValid && leftValid) {
                    moveDirection = GameUtils.getOppositeMove(moveDirection);
                } else if (upValid && rightValid) {
                    moveDirection = GameUtils.getOppositeMove(moveDirection);
                } else if (upValid && leftValid) {
                    moveDirection = GameUtils.getOppositeMove(moveDirection);
                } else if (bottomValid || upValid) {
                    switch (moveDirection) {
                        case topLeft:
                            moveDirection = EnemyMoveDirection.topRight;
                            break;
                        case bottomLeft:
                            moveDirection = EnemyMoveDirection.bottomRight;
                            break;
                        case topRight:
                            moveDirection = EnemyMoveDirection.topLeft;
                            break;
                        case bottomRight:
                            moveDirection = EnemyMoveDirection.bottomLeft;
                            break;
                    }
                } else if (rightValid || leftValid) {
                    switch (moveDirection) {
                        case topLeft:
                            moveDirection = EnemyMoveDirection.bottomLeft;
                            break;
                        case bottomLeft:
                            moveDirection = EnemyMoveDirection.topLeft;
                            break;
                        case topRight:
                            moveDirection = EnemyMoveDirection.bottomRight;
                            break;
                        case bottomRight:
                            moveDirection = EnemyMoveDirection.topRight;
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void drawCoors() {
        for (Coordinate coor : coors) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.worm, transformedCoor.x, transformedCoor.y, 20, 20);
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
