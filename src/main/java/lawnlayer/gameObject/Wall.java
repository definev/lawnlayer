package lawnlayer.gameObject;

import lawnlayer.App;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.Coordinate;
import processing.core.PApplet;

public class Wall extends GameObject {
    public static final Character symbol = 'X';

    public Wall(PApplet app) {
        super(app, "Wall");
    }

    @Override
    protected void drawCoors() {
        for (Coordinate coor : coors) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.concrete, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
