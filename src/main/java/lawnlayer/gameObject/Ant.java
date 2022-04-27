package lawnlayer.gameObject;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.MoveDirection;
import processing.core.PApplet;

public class Ant extends GameObject {

    public Ant(PApplet app) {
        super(app, "Ant");
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
        if (speed + slowdown == 0) return false;
        return frameCount % (App.FPS - (speed + slowdown)) == 0;
    }

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
        coors.add(new Coordinate(31, 31));
    }

    @Override
    protected void drawCoors() {
        for (Coordinate coor : coors) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.color(12, 213, 221, 1);
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
        var coor = coors.get(0);
        if (coor.x == 0 && coor.y == GameUtils.MAP_HEIGHT - 1) {
            direction = MoveDirection.right;
        }
        if (coor.x == GameUtils.MAP_WIDTH - 1 && coor.y == 0) {
            direction = MoveDirection.left;
        }
        if (coor.x == 0 && coor.y == 0) {
            direction = MoveDirection.down;
        }
        if (coor.x == GameUtils.MAP_WIDTH - 1 && coor.y == GameUtils.MAP_HEIGHT - 1) {
            direction = MoveDirection.up;
        }

        if (canUpdate()) {
            coors.remove(0);
            coors.add(coor.move(direction));
        }
    }
}
