package lawnlayer.gameObject;

import lawnlayer.App;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.Coordinate;

public class Wall extends BaseGameObject {
    public static final Character symbol = 'X';

    public Wall(App app) {
        super(app, "Wall");
    }

    @Override
    protected void drawCoordinates() {
        for (Coordinate coor : coordinates) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.concrete, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
