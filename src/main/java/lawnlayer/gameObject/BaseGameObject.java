package lawnlayer.gameObject;

import java.util.ArrayList;
import java.util.Timer;

import lawnlayer.App;
import lawnlayer.utils.Coordinate;
import processing.core.PApplet;

public abstract class GameObject {
    GameObject(App app, String debugName) {
        this.app = app;
        this.debugName = debugName;
        initCoor();
    }

    protected App app;

    protected void initCoor() {}

    protected abstract void drawCoors();

    public void setUp() {}

    protected void collisionCheck() {
        if (needToCheckCollision) {
            for (GameObject object : app.objects) {
                var collisionPoints = collideAt(object);
                if (!collisionPoints.isEmpty()) {
                    onCollision(object, collisionPoints);
                }
            }
        }
    }

    public void draw() {
        drawCoors();
        collisionCheck();
    }

    public void addCoor(Coordinate coor) {
        coors.add(coor);
    }

    public ArrayList<Coordinate> coors = new ArrayList();
    public String debugName;
    protected Boolean needToCheckCollision = false;

    public ArrayList<Coordinate> collideAt(GameObject object) {
        var points = new ArrayList<Coordinate>();
        for (Coordinate coor : object.coors) {
            if (coors.contains(coor)) {
                points.add(coor);
            }
        }
        return points;
    }

    void onCollision(GameObject object, ArrayList<Coordinate> points) {}

    void selfDestroy() {
        app.deleteQueue.add(this);
    }
}
