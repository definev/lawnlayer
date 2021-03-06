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
                ArrayList<Coordinate> collisionPoints = collideAt(object);
                if (!collisionPoints.isEmpty()) {
                    onCollision(object, collisionPoints);
                }
            }
        }
    }

    public void draw() {
        if (!app.isRunningTest) drawCoordinates();
        onCollisionCheck();
    }

    public void addCoordinate(Coordinate coordinate) {
        coordinates.add(coordinate);
    }

    public ArrayList<Coordinate> coordinates = new ArrayList();
    public String className;
    protected Boolean checkCollision = false;

    public ArrayList<Coordinate> collideAt(BaseGameObject object) {
        ArrayList<Coordinate> points = new ArrayList<Coordinate>();
        for (Coordinate coordinate : object.coordinates) {
            if (coordinates.contains(coordinate)) {
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
