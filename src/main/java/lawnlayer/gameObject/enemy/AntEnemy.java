package lawnlayer.gameObject.enemy;

import lawnlayer.App;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.direction.MoveDirection;

public class AntEnemy extends BaseGameObject {

    public AntEnemy(App app) {
        super(app, "AntEnemy");
    }

    public static final Character symbol = 'a';

    public MoveDirection direction = MoveDirection.right;

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
        boolean canRefresh = frameCount % GameUtils.FPS == GameUtils.FPS - 1;
        if (canRefresh) {
            frameCount = 0;
        } else {
            frameCount += 1;
        }
    }

    @Override
    protected void initCoordinate() {
        coordinatedinates.add(new Coordinate(31, 31));
    }

    @Override
    protected void drawCoordinates() {
        for (Coordinate coordinate : coordinatedinates) {
            Coordinate transformedCoor = GameUtils.transformCoor(coordinate);
            app.fill(32, 102, 21);
            app.rect(transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }

    @Override
    public void draw() {
        super.draw();
        onAntMove();
        onFrameUpdate();

    }

    private void onAntMove() {
        Coordinate coordinate = coordinatedinates.get(0);
        if (coordinate.x == 0 && coordinate.y == GameUtils.MAP_HEIGHT - 1) {
            direction = MoveDirection.right;
        }
        if (coordinate.x == GameUtils.MAP_WIDTH - 1 && coordinate.y == 0) {
            direction = MoveDirection.left;
        }
        if (coordinate.x == 0 && coordinate.y == 0) {
            direction = MoveDirection.down;
        }
        if (coordinate.x == GameUtils.MAP_WIDTH - 1 && coordinate.y == GameUtils.MAP_HEIGHT - 1) {
            direction = MoveDirection.up;
        }

        if (canUpdate()) {
            coordinatedinates.remove(0);
            coordinatedinates.add(coordinate.move(direction));
        }
    }
}
