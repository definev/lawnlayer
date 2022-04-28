package lawnlayer.gameObject.enemy;

import java.util.ArrayList;
import java.util.Random;

import lawnlayer.App;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.gameObject.staticObj.Grass;
import lawnlayer.gameObject.staticObj.Wall;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.direction.EnemyMoveDirection;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.direction.MoveDirection;

public class WormEnemy extends BaseGameObject {
    public WormEnemy(App app) {
        super(app, "WormEnemy");
        checkCollision = true;
    }

    public static final Character symbol = 'w';

    public EnemyMoveDirection moveDirection = EnemyMoveDirection.topLeft;
    Integer frameCount = 0;
    Integer speed = 52;
    Integer slowdown = 0;
    public void setSlowdown(Integer slowdown) {
        this.slowdown = slowdown;
    }

    private boolean canUpdate() {
        if (speed - slowdown == 0) return false;
        return frameCount % (GameUtils.FPS - (speed - slowdown)) == 0;
    }

    private void onFrameUpdate() {
        var canRefresh = frameCount % GameUtils.FPS == GameUtils.FPS - 1;
        if (canRefresh) {
            frameCount = 0;
        } else {
            frameCount += 1;
        }
    }

    @Override
    protected void initCoordinate() {
        coordinatedinates.add((app).masterMap.randomizeLocation());
        moveDirection = EnemyMoveDirection.values()[new Random().nextInt(EnemyMoveDirection.values().length)];
    }

    @Override
    protected void onCollision(BaseGameObject object, ArrayList<Coordinate> points) {
        var point = points.get(0);
        if (object.className == "Wall" || object.className == "Grass") {
            var last = coordinatedinates.get(0);

            coordinatedinates = new ArrayList<>();
            switch (moveDirection) {
                case topLeft:
                    coordinatedinates.add(new Coordinate(last.x + 1, last.y + 1));
                    break;
                case topRight:
                    coordinatedinates.add(new Coordinate(last.x - 1, last.y + 1));
                    break;
                case bottomRight:
                    coordinatedinates.add(new Coordinate(last.x - 1, last.y - 1));
                    break;
                case bottomLeft:
                    coordinatedinates.add(new Coordinate(last.x + 1, last.y - 1));
                    break;
            }

            if (point.isEdge()) {
                moveDirection = GameUtils.getOppositeMove(moveDirection);
            } else {
                var game = app;

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
    protected void drawCoordinates() {
        for (Coordinate coordinate : coordinatedinates) {
            var transformedCoor = GameUtils.transformCoor(coordinate);
            app.image(App.worm, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }

    @Override
    public void draw() {
        drawCoordinates();
        onWormMove();
        onCollisionCheck();
        onFrameUpdate();
    }

    private void onWormMove() {
        if (canUpdate()) {
            var last = coordinatedinates.get(0);
            coordinatedinates = new ArrayList<>();
            switch (moveDirection) {
                case topLeft:
                    coordinatedinates.add(new Coordinate(last.x - 1, last.y - 1));
                    break;
                case topRight:
                    coordinatedinates.add(new Coordinate(last.x + 1, last.y - 1));
                    break;
                case bottomRight:
                    coordinatedinates.add(new Coordinate(last.x + 1, last.y + 1));
                    break;
                case bottomLeft:
                    coordinatedinates.add(new Coordinate(last.x - 1, last.y + 1));
                    break;
            }
        }
    }
}
