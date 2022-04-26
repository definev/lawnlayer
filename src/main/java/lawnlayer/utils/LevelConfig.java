package lawnlayer.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import processing.data.JSONObject;

public class LevelConfig {
    public LevelConfig(String outlay, ArrayList<EnemyConfig> enemies) {
        this.outlay = outlay;
        this.enemies = enemies;
    }

    public static ArrayList<LevelConfig> getConfigs(String path) throws FileNotFoundException {
        ArrayList<LevelConfig> levelConfigs = new ArrayList<>();
        var json = JsonParser.parseReader(new FileReader(path));

        var obj = json.getAsJsonObject();
        var levels = (JsonArray) obj.get("levels");
        for (JsonElement level : levels) {
            var jsn = level.getAsJsonObject();
            ArrayList<EnemyConfig> enemies = new ArrayList<>();
            var rawEnemies = jsn.get("enemies").getAsJsonArray();
            for (JsonElement rawWorm : rawEnemies) {
                var jsn1 = rawWorm.getAsJsonObject();
                enemies.add(new EnemyConfig(jsn1.get("type").getAsInt(), jsn1.get("spawn").getAsString()));
            }

            levelConfigs.add(new LevelConfig(jsn.get("outlay").getAsString(), enemies));
        }

        return levelConfigs;
    }

    public String outlay;
    public ArrayList<EnemyConfig> enemies;
}

