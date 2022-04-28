package lawnlayer.gameObject;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lawnlayer.App;
import lawnlayer.gameObject.enemy.AntEnemy;
import lawnlayer.gameObject.enemy.BeetleEnermy;
import lawnlayer.gameObject.enemy.WormEnemy;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.GameUtils;

public class SlowEnemyPowerUp extends BaseGameObject {
    public SlowEnemyPowerUp(App app) {
        super(app, "SlowEnemyPowerUp");
        checkCollision = true;
    }

    public static Character symbol = 's';

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            var game = app;
            for (BaseGameObject baseGameObject : game.objects) {
                if (baseGameObject.className == "WormEnemy") {
                    var worm = (WormEnemy) baseGameObject;
                    worm.setSlowdown(0);
                }
                if (baseGameObject.className == "BeetleEnemy") {
                    var beetle = (BeetleEnermy) baseGameObject;
                    beetle.setSlowdown(0);
                }
                if (baseGameObject.className == "AntEnemy") {
                    var ant = (AntEnemy) baseGameObject;
                    ant.setSlowdown(0);
                }
            }
            executor.shutdown();
        }
    };


    @Override
    protected void initCoordinate() {
        coordinates.add((app).masterMap.randomizeLocation());
    }

    @Override
    void onCollision(BaseGameObject object, ArrayList<Coordinate> points) {
        if (object.className == "Player") {
            var game = app;
            for (BaseGameObject baseGameObject : game.objects) {
                if (baseGameObject.className == "WormEnemy") {
                    var worm = (WormEnemy) baseGameObject;
                    worm.setSlowdown(10);
                }
                if (baseGameObject.className == "BeetleEnemy") {
                    var beetle = (BeetleEnermy) baseGameObject;
                    beetle.setSlowdown(10);
                }
                if (baseGameObject.className == "AntEnemy") {
                    var ant = (AntEnemy) baseGameObject;
                    ant.setSlowdown(10);
                }
            }
            selfDestroy();
            executor.schedule(task, 5, TimeUnit.SECONDS);
        }
    }

    @Override
    protected void drawCoordinates() {
        for (Coordinate coor : coordinates) {
            var transformedCoor = GameUtils.transformCoor(coor);
            app.image(App.slowPowerUp, transformedCoor.x, transformedCoor.y, 20, 20);
        }
    }
}
