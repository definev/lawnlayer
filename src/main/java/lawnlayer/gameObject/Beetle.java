package lawnlayer.gameObject;

import java.util.ArrayList;
import java.util.Random;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.EnemyMoveDirection;
import lawnlayer.utils.GameUtils;
import processing.core.PApplet;

public class Beetle extends Worm {
    public Beetle(PApplet app) {
        super(app);
        debugName = "Beetle";
    }

    public static Character symbol = 'b';

    @Override
    protected void drawCoors() {
        for (Coordinate coor : coors) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.beetle, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }

    @Override
    void onCollision(GameObject object, ArrayList<Coordinate> points) {
        super.onCollision(object, points);

        if (object.debugName == "Grass") {
            object.coors.removeAll(points);
        }
    }
}
