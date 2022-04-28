package lawnlayer.gameObject;

import java.util.ArrayList;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;

public abstract class BaseGameObject {
    public BaseGameObject(App app, String className) {
        this.app = app;
        this.className = className;
        initCoordinate();
    }

    protected App app;

    protected void initCoordinate() {
    }

    protected abstract void drawCoordinates();

    public void setUp() {
    }

    protected void onCollisionCheck() {
        if (checkCollision) {
            for (BaseGameObject object : app.objects) {
                var collisionPoints = collideAt(object);
                if (!collisionPoints.isEmpty()) {
                    onCollision(object, collisionPoints);
                }
            }
        }
    }

    public void draw() {
        drawCoordinates();
        onCollisionCheck();
    }

    public void addCoordinate(Coordinate coordinate) {
        coordinatedinates.add(coordinate);
    }

    public ArrayList<Coordinate> coordinatedinates = new ArrayList();
    public String className;
    protected Boolean checkCollision = false;

    public ArrayList<Coordinate> collideAt(BaseGameObject object) {
        var points = new ArrayList<Coordinate>();
        for (Coordinate coordinate : object.coordinatedinates) {
            if (coordinatedinates.contains(coordinate)) {
                points.add(coordinate);
            }
        }
        return points;
    }

    protected void onCollision(BaseGameObject object, ArrayList<Coordinate> points) {
    }

    protected void selfDestroy() {
        app.deleteQueue.add(this);
    }
}
