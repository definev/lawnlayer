package lawnlayer.utils;

import java.util.ArrayList;
import java.util.TimerTask;

import lawnlayer.App;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.gameObject.powerup.SlowEnemyPowerUp;

public class PowerUpTimerTask extends TimerTask {
    public PowerUpTimerTask(App app) {
        this.app = app;
    }

    public App app;

    @Override
    public void run() {
        if (app.state != GameState.playing) return;
        ArrayList<BaseGameObject> deleteQueue = new ArrayList<BaseGameObject>();
        for (BaseGameObject object : app.objects) {
            if (object.className.contains("PowerUp")) {
                deleteQueue.add(object);
            }
        }

        app.deleteQueue.addAll(deleteQueue);
        app.insertQueue.add(new SlowEnemyPowerUp(app));
    }
}
