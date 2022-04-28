package lawnlayer.gameObject;

import lawnlayer.App;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.Coordinate;

public class Grass extends BaseGameObject {
    public static final Character symbol = 'g';

    public Grass(App app) {
        super(app, "Grass");
    }

    @Override
    protected void drawCoordinates() {
        for (Coordinate coor : coordinates) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.grass, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
