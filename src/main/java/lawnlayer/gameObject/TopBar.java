package lawnlayer.gameObject;

import lawnlayer.App;
import processing.core.PApplet;
import processing.core.PFont;

public class TopBar {
    public TopBar(PApplet app) {
        this.app = app;
    }

    private final PApplet app;
    private PFont f;

    public void setUp() {
        this.f = app.createFont("Times-Roman",16,false);
    }

    public void draw() {
        String livesText = String.format("Live: %d", ((App) app).lives);
        String percentText = String.format("%d%s / %d%%", ((App) app).masterMap.evaluatePercent(Grass.symbol), "%", ((App) app).targetPercent);
        String levelText = String.format("Level: %d", ((App) app).currentLevel + 1);

        app.textFont(f);
        app.fill(255);
        app.text(livesText, 40, 30);
        app.text(percentText, 200, 30);
        app.text(levelText, 900, 50);
    }
}
