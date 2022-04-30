package lawnlayer.gameObject.enemy;

import java.util.ArrayList;

import lawnlayer.App;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.GameUtils;

public class BeetleEnermy extends WormEnemy {
    public BeetleEnermy(App app) {
        super(app);
        className = "BeetleEnemy";
    }

    public static Character symbol = 'b';

    @Override
    protected void drawCoordinates() {
        for (Coordinate coordinate : coordinates) {
            Coordinate transformedCoor = GameUtils.transformCoor(coordinate);
            app.image(App.beetle, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }

    @Override
    protected void onCollision(BaseGameObject object, ArrayList<Coordinate> points) {
        super.onCollision(object, points);

        if (object.className.equals("Grass")) {
            object.coordinates.removeAll(points);
        }
    }
}
