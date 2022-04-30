package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import lawnlayer.gameObject.Player;
import lawnlayer.gameObject.enemy.WormEnemy;
import lawnlayer.utils.Coordinate;
import lawnlayer.utils.direction.EnemyMoveDirection;

public class PlayerTest {
    @Test
    public void enemyHitTest() {
        App app = new App();
        app.testSettings();
        Player player = new Player(app);
        app.objects.add(player);
        player.coordinates = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            player.addCoordinate(new Coordinate(10, i));
        }
        WormEnemy worm = new WormEnemy(app);
        worm.moveDirection = EnemyMoveDirection.topLeft;
        worm.coordinates = new ArrayList<>();
        worm.addCoordinate(new Coordinate(11, 10));

        app.objects.add(worm);

        for (int i = 0; i < 60; i++) {
            player.draw();
            worm.draw();
        }

        assertEquals(app.lives, 2);
    }
}
