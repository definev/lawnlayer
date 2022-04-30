package lawnlayer.gameObject.staticObj;

import lawnlayer.App;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.utils.GameUtils;
import lawnlayer.utils.Coordinate;

public class Grass extends BaseGameObject {
    public static final Character symbol = 'g';

    public Grass(App app) {
        super(app, "Grass");
    }

    @Override
    protected void drawCoordinates() {
        for (Coordinate coordinate : coordinatedinates) {
            Coordinate transformedCoor = GameUtils.transformCoor(coordinate);
            app.image(App.grass, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
