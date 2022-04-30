package lawnlayer.gameObject;

import lawnlayer.App;
import lawnlayer.utils.GameMap;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.Coordinate;
import processing.core.PApplet;

public class Grass extends GameObject {
    public static final Character symbol = 'g';

    public Grass(PApplet app) {
        super(app, "Grass");
    }

    @Override
    protected void drawCoors() {
        for (Coordinate coor : coors) {
            Coordinate transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.grass, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
