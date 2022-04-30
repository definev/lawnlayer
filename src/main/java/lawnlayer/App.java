package lawnlayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import lawnlayer.gameObject.enemy.AntEnemy;
import lawnlayer.gameObject.enemy.BeetleEnermy;
import lawnlayer.gameObject.staticObj.Grass;
import lawnlayer.gameObject.enemy.WormEnemy;
import lawnlayer.gameObject.BaseGameObject;
import lawnlayer.gameObject.Player;
import lawnlayer.utils.GameState;
import lawnlayer.utils.configs.EnemyConfig;
import lawnlayer.utils.gameMap.GameMap;
import lawnlayer.utils.GameUtils;
import lawnlayer.gameObject.staticObj.TopBar;
import lawnlayer.utils.configs.LevelConfig;
import lawnlayer.utils.PowerUpTimerTask;
import processing.core.PApplet;
import processing.core.PImage;

public class App extends PApplet {
    public App() {
        this.configPath = "config.json";
        this.topBar = new TopBar(this);
    }

    public String configPath;

    public static PImage ball;
    public static PImage grass;
    public static PImage concrete;
    public static PImage worm;
    public static PImage beetle;
    public static PImage ant;
    public static PImage slowPowerUp;

    public GameState state = GameState.playing;
    public Integer targetPercent = 80;

    public Integer lives = 3;
    public ArrayList<LevelConfig> levelConfigs = new ArrayList<>();
    public Integer currentLevel = null;
    public ArrayList<BaseGameObject> deleteQueue = new ArrayList<>();
    public ArrayList<BaseGameObject> insertQueue = new ArrayList<>();

    public boolean isRunningTest = false;

    Timer timer = new Timer();

    public void nextLevel() {
        state = GameState.playing;
        currentLevel = (currentLevel + 1) % levelConfigs.size();
        targetPercent = 80;
        configGameMap(levelConfigs.get(currentLevel));
    }

    public void retryLevel() {
        state = GameState.playing;
        targetPercent = 80;
        configGameMap(levelConfigs.get(currentLevel));
    }

    public TopBar topBar;
    public ArrayList<BaseGameObject> objects = new ArrayList();

    public GameMap masterMap = new GameMap();

    public ArrayList<BaseGameObject> queueObjects = null;

    private void configGameMap(LevelConfig config) {
        try {
            lives = config.lives;
            masterMap.clear();
            objects = GameUtils.load(this, this.getClass().getResource(config.outlay).getPath());
            for (EnemyConfig enemyConfig : config.enemies) {
                if (enemyConfig.type == 0) {
                    objects.add(new WormEnemy(this));
                }
                if (enemyConfig.type == 1) {
                    objects.add(new BeetleEnermy(this));
                }
            }
            objects.add(new Player(this));
            objects.add(new AntEnemy(this));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settings() {
        size(GameUtils.WIDTH, GameUtils.HEIGHT);
        try {
            levelConfigs = LevelConfig.getConfigs(configPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void testSettings() {
        isRunningTest = true;
        try {
            levelConfigs = LevelConfig.getConfigs(configPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void setup() {
        frameRate(GameUtils.FPS);

        ball = loadImage(this.getClass().getResource("ball.png").getPath());
        grass = loadImage(this.getClass().getResource("grass.png").getPath());
        concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        worm = loadImage(this.getClass().getResource("worm.png").getPath());
        beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
        ant = loadImage(this.getClass().getResource("ant.png").getPath());
        slowPowerUp = loadImage(this.getClass().getResource("slow_power_up.png").getPath());

        background(95, 60, 33);
        topBar.setUp();
        for (BaseGameObject object : objects) {
            object.setUp();
        }

        timer.schedule(new PowerUpTimerTask(this), 1000, 10000);
    }

    public void draw() {
        if (state == GameState.win) {
            clear();
            background(95, 60, 33);
            textSize(32);
            text(String.format("Level %d complete!!!", currentLevel), 1080 / 2, 720 / 2);
            textSize(32);
            text("Press any key to play next level", 1080 / 2, 720 / 2+ 80);
            if (keyPressed) {
                nextLevel();
            }
            return;
        }
        if (state == GameState.lose) {
            clear();
            background(95, 60, 33);
            textSize(64);
            textAlign(CENTER);
            text(String.format("Level %d: You lose!!!", currentLevel), 1080 / 2, 720 / 2);
            textSize(32);
            text("Press any key to play again", 1080 / 2, 720 / 2+ 80);
            if (keyPressed) {
                retryLevel();
            }
            return;
        }

        if (!deleteQueue.isEmpty()) {
            objects.removeAll(deleteQueue);
            deleteQueue = new ArrayList<>();
        }
        if (!insertQueue.isEmpty()) {
            objects.addAll(insertQueue);
            insertQueue = new ArrayList<>();
        }

        if (currentLevel == null) {
            currentLevel = 0;
            LevelConfig level = levelConfigs.get(currentLevel);
            configGameMap(level);
        }
        if (queueObjects != null) {
            objects = queueObjects;
            queueObjects = null;
        }

        if (lives <= 0) {
            state = GameState.lose;
            return;
        }

        if (masterMap.evaluatePercent(Grass.symbol) >= targetPercent) {
            state = GameState.win;
            return;
        }

        clear();
        background(95, 60, 33);
        masterMap.clear();
        for (BaseGameObject object : objects) {
            masterMap.fill(object);
        }

        for (BaseGameObject object : objects) {
            object.draw();
        }

        masterMap.clear();
        for (BaseGameObject object : objects) {
            masterMap.fill(object);
        }

        topBar.draw();
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
