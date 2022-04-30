package lawnlayer.gameObject;

import java.util.ArrayList;
import java.util.Random;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.EnemyMoveDirection;
import lawnlayer.utils.GameMapPixel;
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
    Integer speed = 52;
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    private boolean canUpdate() {
        if (speed == 0) return false;
        return frameCount % (App.FPS - speed) == 0;
    }

    private void onFrameUpdate() {
        boolean canRefresh = frameCount % App.FPS == App.FPS - 1;
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
        Coordinate point = points.get(0);
        if (object.debugName == "Wall" || object.debugName == "Grass") {
            Coordinate last = coors.get(0);

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
                App game = (App) app;

                Boolean bottomValid = false;
                Coordinate bottomLast = point.move(MoveDirection.down);
                Boolean upValid = false;
                Coordinate upLast = point.move(MoveDirection.up);
                Boolean leftValid = false;
                Coordinate leftLast = point.move(MoveDirection.left);
                Boolean rightValid = false;
                Coordinate rightLast = point.move(MoveDirection.right);

                if (!bottomLast.isOutOfBounds()) {
                    GameMapPixel pixel = game.masterMap.get(bottomLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        bottomValid = true;
                    }
                }
                if (!upLast.isOutOfBounds()) {
                    GameMapPixel pixel = game.masterMap.get(upLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        upValid = true;
                    }
                }
                if (!leftLast.isOutOfBounds()) {
                    GameMapPixel pixel = game.masterMap.get(leftLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        leftValid = true;
                    }
                }
                if (!rightLast.isOutOfBounds()) {
                    GameMapPixel pixel = game.masterMap.get(rightLast);
                    if (pixel.symbol == Wall.symbol || pixel.symbol == Grass.symbol) {
                        rightValid = true;
                    }
                }
                if (bottomValid && rightValid && moveDirection == EnemyMoveDirection.bottomRight) {
                    moveDirection = GameUtils.getOppositeMove(moveDirection);
                } else if (bottomValid && leftValid && moveDirection == EnemyMoveDirection.bottomLeft) {
                    moveDirection = GameUtils.getOppositeMove(moveDirection);
                } else if (upValid && rightValid && moveDirection != EnemyMoveDirection.topLeft) {
                    moveDirection = GameUtils.getOppositeMove(moveDirection);
                } else if (upValid && leftValid && moveDirection != EnemyMoveDirection.topRight) {
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
            Coordinate transformedCoor = GameUtils.transformCoor(coor);
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
        if (canUpdate()) {
            Coordinate last = coors.get(0);
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
