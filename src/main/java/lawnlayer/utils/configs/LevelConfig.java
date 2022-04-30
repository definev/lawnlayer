package lawnlayer.utils.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class LevelConfig {
    public LevelConfig(String outlay, ArrayList<EnemyConfig> enemies, Integer lives) {
        this.outlay = outlay;
        this.enemies = enemies;
        this.lives = lives;
    }

    public static ArrayList<LevelConfig> getConfigs(String path) throws FileNotFoundException {
        ArrayList<LevelConfig> levelConfigs = new ArrayList<>();
        JsonElement json = JsonParser.parseReader(new FileReader(path));

        JsonObject obj = json.getAsJsonObject();
        JsonArray levels = (JsonArray) obj.get("levels");
        for (JsonElement level : levels) {
            JsonObject jsn = level.getAsJsonObject();
            ArrayList<EnemyConfig> enemies = new ArrayList<>();
            JsonArray rawEnemies = jsn.get("enemies").getAsJsonArray();
            for (JsonElement rawWorm : rawEnemies) {
                JsonObject jsn1 = rawWorm.getAsJsonObject();
                enemies.add(new EnemyConfig(jsn1.get("type").getAsInt(), jsn1.get("spawn").getAsString()));
            }

            levelConfigs.add(new LevelConfig(jsn.get("outlay").getAsString(), enemies, obj.get("lives").getAsInt()));
        }

        return levelConfigs;
    }

    public String outlay;
    public ArrayList<EnemyConfig> enemies;
    public Integer lives;
}

