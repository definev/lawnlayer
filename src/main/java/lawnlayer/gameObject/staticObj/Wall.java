package lawnlayer.gameObject.staticObj;

import lawnlayer.App;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.Coordinate;

public class Wall extends BaseGameObject {
    public static final Character symbol = 'X';

    public Wall(App app) {
        super(app, "Wall");
    }

    @Override
    protected void drawCoordinates() {
        for (Coordinate coordinate : coordinatedinates) {
            var transformedCoor = GameUtils.transformCoor(coordinate);
            app.image(App.concrete, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
