package lawnlayer.gameObject;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lawnlayer.App;
import lawnlayer.gameObject.staticObj.Grass;
import lawnlayer.utils.gameMap.GameMap;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.direction.MoveDirection;

public class Player extends BaseGameObject {
    public static final Character symbol = 'G';

    public Player(App app) {
        super(app, "Player");
        checkCollision = true;
    }

    static final Integer REFRESH_FRAME = 5;
    static final Integer DEAD_FRAME = 1;
    static final Integer MIN_SIZE_FOR_START_MARK_RED = 6;

    ArrayList<Integer> redFlags = new ArrayList<>();
    Integer frameCount = 0;
    MoveDirection direction = MoveDirection.none;
    MoveDirection lastDirection = MoveDirection.up;
    Boolean isDead = false;

    Boolean isInSafeZone() {
        App lawnlayer = app;
        boolean inSafeZone = false;
        for (BaseGameObject object : lawnlayer.objects) {
            if (object.className == "Wall" || object.className == "Grass") {
                ArrayList<Coordinate> coordinatesCommon = new ArrayList(coordinates);
                coordinatesCommon.retainAll(object.coordinates);

                if (!coordinatesCommon.isEmpty() && coordinates.size() == 1) {
                    inSafeZone = true;
                    break;
                }
            }
        }

        return inSafeZone;
    }

    private void markDied() {
        isDead = true;
        lastDirection = MoveDirection.none;
        direction = MoveDirection.none;
        redFlags = new ArrayList<>();
    }

    private void markRelive() {
        isDead = false;
        coordinates = new ArrayList();
        initCoordinate();
        (app).lives -= 1;
    }

    /// FRAME COUNTING
    private void onKeyboardUpdate() {
        if (app.keyPressed) {
            if (isInSafeZone()) {
                if (app.keyCode == KeyEvent.VK_UP) {
                    direction = MoveDirection.up;
                } else if (app.keyCode == KeyEvent.VK_DOWN) {
                    direction = MoveDirection.down;
                } else if (app.keyCode == KeyEvent.VK_LEFT) {
                    direction = MoveDirection.left;
                } else if (app.keyCode == KeyEvent.VK_RIGHT) {
                    direction = MoveDirection.right;
                } else {
                    direction = MoveDirection.none;
                }
                return;
            }
            if (app.keyCode == KeyEvent.VK_UP && lastDirection != MoveDirection.down) {
                direction = MoveDirection.up;
            } else if (app.keyCode == KeyEvent.VK_DOWN && lastDirection != MoveDirection.up) {
                direction = MoveDirection.down;
            } else if (app.keyCode == KeyEvent.VK_LEFT && lastDirection != MoveDirection.right) {
                direction = MoveDirection.left;
            } else if (app.keyCode == KeyEvent.VK_RIGHT && lastDirection != MoveDirection.left) {
                direction = MoveDirection.right;
            } else {
                return;
            }
        } else {
            if (direction != MoveDirection.none) lastDirection = direction;
            if (isInSafeZone()) direction = MoveDirection.none;
        }
    }

    private void onMovingUpdate() {
        boolean canRefresh = frameCount % REFRESH_FRAME == REFRESH_FRAME - 1;
        if (direction != MoveDirection.none && canRefresh) {
            Coordinate last = coordinates.get(coordinates.size() - 1);
            Coordinate newDirection = last.move(direction);

            if (newDirection.isOutOfBounds()) {
                direction = MoveDirection.none;
                return;
            }

            if (coordinates.contains(newDirection)) {
                markDied();
                return;
            }

            if (isInSafeZone()) {
                coordinates = new ArrayList<>();
            }

            coordinates.add(newDirection);
        }
    }

    private void onRedFlagUpdate() {
        if (direction != MoveDirection.none) {
            redFlags = new ArrayList<>();
        }

        boolean canUpdate = frameCount % 3 == 2 && coordinates.size() > MIN_SIZE_FOR_START_MARK_RED;
        if (canUpdate) {
            if (direction == MoveDirection.none) {
                if (redFlags.size() >= coordinates.size() - 1) {
                    markDied();
                } else {
                    redFlags.add(redFlags.size());
                }
            }
        }
    }

    /// ON DEAD
    private void onDeadUpdate() {
        if (redFlags.size() == coordinates.size()) {
            markRelive();
            return;
        }
        boolean canRefresh = frameCount % DEAD_FRAME == DEAD_FRAME - 1;
        if (canRefresh) {
            redFlags.add(redFlags.size());
        }
    }

    /// FRAME COUNTING
    private void onFrameUpdate() {
        boolean canRefresh = frameCount % GameUtils.FPS == GameUtils.FPS - 1;
        if (canRefresh) {
            frameCount = 0;
        } else {
            frameCount += 1;
        }
    }


    @Override
    protected void initCoordinate() {
        coordinates.add(new Coordinate(0, 0));
    }

    @Override
    protected void drawCoordinates() {
        for (int i = 0; i < coordinates.size(); i += 1) {
            Coordinate coordinate = coordinates.get(i);
            Coordinate transformedCoor = GameUtils.transformCoor(coordinate);
            if (i == coordinates.size() - 1) {
                app.image(App.ball, transformedCoor.x, transformedCoor.y, 20, 20);
            } else {
                if (redFlags.contains(i)) {
                    app.fill(127, 0, 0);
                } else {
                    app.fill(0, 222, 0);
                }
                app.rect(transformedCoor.x + 2, transformedCoor.y + 2, 16, 16);
            }
        }
    }

    @Override
    public void draw() {
        if (isDead.equals(false)) {
            onKeyboardUpdate();
            onMovingUpdate();
            onRedFlagUpdate();
        } else {
            onDeadUpdate();
        }
        super.draw();
        onFrameUpdate();
    }

    @Override
    protected void onCollision(BaseGameObject object, ArrayList<Coordinate> points) {
        super.onCollision(object, points);
        if (isDead) return;
        if (object.className.equals("AntEnemy")) {
            markDied();
        }
        if (object.className.equals("Grass") || object.className.equals("Wall")) {
            if (isInSafeZone()) return;
            if (coordinates.size() < 2) return;
            GameMap cloneMap = (app).masterMap.clone();

            Coordinate before = coordinates.get(coordinates.size() - 2);
            Coordinate after = coordinates.get(coordinates.size() - 1);

            GameMap transformMap = cloneMap.clone();
            transformMap.relativeFloodFill(before);

            GameMap newMap = transformMap;

            newMap.transform(Player.symbol, Grass.symbol);
            coordinates = new ArrayList();
            coordinates.add(after);
            ArrayList<BaseGameObject> queueObjects = newMap.parse(app);
            queueObjects.add(this);
            (app).queueObjects = queueObjects;
        }

        if (object.className.equals("WormEnemy") || object.className.equals("BeetleEnemy")) {
            markDied();
        }
    }
}
