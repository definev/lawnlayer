package lawnlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import lawnlayer.gameObject.Ant;
import lawnlayer.gameObject.Beetle;
import lawnlayer.gameObject.Grass;
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

enum GameState {
    playing,
    lose,
    win,
}

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
    public static PImage ant;

    public GameState state = GameState.playing;
    public Integer targetPercent = 80;

    public Integer lives = 3;
    public ArrayList<LevelConfig> levelConfigs = new ArrayList<>();
    public Integer currentLevel = null;

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
    public ArrayList<GameObject> objects = new ArrayList();

    public GameMap masterMap = new GameMap();

    public ArrayList<GameObject> queueObjects = null;

    private void configGameMap(LevelConfig config) {
        try {
            lives = config.lives;
            masterMap.clear();
            objects = GameUtils.load(this, this.getClass().getResource(config.outlay).getPath());
            for (EnemyConfig enemyConfig : config.enemies) {
                if (enemyConfig.type == 0) {
                    objects.add(new Worm(this));
                }
                if (enemyConfig.type == 1) {
                    objects.add(new Beetle(this));
                }
            }
            objects.add(new Player(this));
            objects.add(new Ant(this));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settings() {
        size(WIDTH, HEIGHT);
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
        ant = loadImage(this.getClass().getResource("ant.png").getPath());

        background(95, 60, 33);
        topBar.setUp();
        for (GameObject object : objects) {
            object.setUp();
        }
    }

    public void draw() {
        if (state == GameState.win) {
            clear();
            background(95, 60, 33);
            textSize(64);
            text("You win!!!", 600, 720);
            textSize(32);
            text("Press Space to play next level!", 1080 / 2, 720 / 2+ 80);
            if (keyPressed) {
                if (key == ' ') {
                    nextLevel();
                }
            }
            return;
        }
        if (state == GameState.lose) {
            clear();
            background(95, 60, 33);
            textSize(64);
            textAlign(CENTER);
            text("You lose!!!", 1080 / 2, 720 / 2);
            textSize(32);
            text("Press Space to play again!", 1080 / 2, 720 / 2+ 80);
            if (keyPressed) {
                if (key == ' ') {
                    retryLevel();
                }
            }
            return;
        }

        if (currentLevel == null) {
            currentLevel = 0;
            var level = levelConfigs.get(currentLevel);
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
