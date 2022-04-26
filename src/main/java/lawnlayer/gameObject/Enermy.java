package lawnlayer.gameObject;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.GameUtils;
import processing.core.PApplet;

public class Enermy extends GameObject {
    Enermy(PApplet app) {
        super(app, "Enermy");
    }

    @Override
    protected void drawCoors() {
        for (Coordinate coor : coors) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.worm, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
