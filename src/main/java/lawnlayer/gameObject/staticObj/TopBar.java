package lawnlayer.gameObject;

import lawnlayer.App;
import processing.core.PApplet;
import processing.core.PFont;

public class TopBar {
    public TopBar(App app) {
        this.app = app;
    }

    private final App app;
    private PFont f;

    public void setUp() {
        this.f = app.createFont("Times-Roman",16,false);
    }

    public void draw() {
        String livesText = String.format("Live: %d", app.lives);
        String percentText = String.format("%d%s / %s", app.masterMap.evaluatePercent(Grass.symbol), "%", "100%");
        String levelText = String.format("Level: %d", app.currentLevel + 1);

        app.textFont(f);
        app.fill(255);
        app.textSize(32);
        app.text(livesText, 40, 30);
        app.textSize(32);
        app.text(percentText, 200, 30);
        app.textSize(20);
        app.text(levelText, 900, 50);
    }
}
