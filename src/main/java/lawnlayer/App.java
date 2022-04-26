package lawnlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import lawnlayer.gameObject.Beetle;
import lawnlayer.gameObject.Worm;
import lawnlayer.gameObject.GameObject;
import lawnlayer.gameObject.Player;
import lawnlayer.utils.EnemyConfig;
import lawnlayer.utils.EnemyConfig;
import lawnlayer.utils.GameMap;
import lawnlayer.utils.GameUtils;
import lawnlayer.gameObject.TopBar;
import lawnlayer.utils.LevelConfig;
import processing.core.PApplet;
import processing.core.PImage;

public class App extends PApplet {
    public App() {
        this.configPath = "config.json";
        this.topBar = new TopBar(this);
    }

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITE_SIZE = 20;

    public static final int FPS = 60;

    public String configPath;

    public static PImage ball;
    public static PImage grass;
    public static PImage concrete;
    public static PImage worm;
    public static PImage beetle;

    public Integer lives = 3;
    public ArrayList<LevelConfig> levelConfigs = new ArrayList<>();
    public LevelConfig currentLevel = null;

    public TopBar topBar;
    public ArrayList<GameObject> objects = new ArrayList();

    public GameMap masterMap = new GameMap();

    public ArrayList<GameObject> queueObjects = null;

    public void settings() {
        size(WIDTH, HEIGHT);
        try {
            objects = GameUtils.load(this, this.getClass().getResource("level1.txt").getPath());
            objects.add(new Player(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            levelConfigs = LevelConfig.getConfigs(configPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        frameRate(FPS);

        ball = loadImage(this.getClass().getResource("ball.png").getPath());
        grass = loadImage(this.getClass().getResource("grass.png").getPath());
        concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        worm = loadImage(this.getClass().getResource("worm.png").getPath());
        beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
        background(95, 60, 33);
        topBar.setUp();
        for (GameObject object : objects) {
            object.setUp();
        }
    }

    public void draw() {
        if (currentLevel == null) {
            currentLevel = levelConfigs.get(levelConfigs.size() - 1);
            for (EnemyConfig enemyConfig : currentLevel.enemies) {
                if (enemyConfig.type == 0) {
                    objects.add(new Worm(this));
                }
                if (enemyConfig.type == 1) {
                    objects.add(new Beetle(this));
                }
            }
        }
        if (queueObjects != null) {
            objects = queueObjects;
            queueObjects = null;
        }
        clear();
        background(95, 60, 33);
        masterMap.clear();
        for (GameObject object : objects) {
            masterMap.fill(object);
        }

        for (GameObject object : objects) {
            object.draw();
        }

        masterMap.clear();
        for (GameObject object : objects) {
            masterMap.fill(object);
        }

        topBar.draw();
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
