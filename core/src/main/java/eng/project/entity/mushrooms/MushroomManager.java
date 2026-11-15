
package eng.project.entity.mushrooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import eng.project.world.Map;
import eng.project.entity.Player;

import java.util.ArrayList;

public class MushroomManager {

    private final ArrayList<Mushroom> mushrooms = new ArrayList<>();
    private float dizzinessTimer = 0f;

    // makes 10 mushrooms at random positions
    public MushroomManager(TextureRegion texture, Map map) {

        for (int i = 0; i < 30; i++) {

            float x = MathUtils.random(-map.getWidth() / 2 + 1, map.getWidth() / 2 - 1);
            float y = MathUtils.random(-map.getHeight() / 2 + 1, map.getHeight() / 2 - 1);

            // retry if the mushroom is inside a building
            while (!map.canSpawnMushroom(x, y)) {
                x = MathUtils.random(-map.getWidth() / 2 + 1, map.getWidth() / 2 - 1);
                y = MathUtils.random(-map.getHeight() / 2 + 1, map.getHeight() / 2 - 1);
            }

            mushrooms.add(new Mushroom(x, y, texture));
        }
    }

    // draw all mushrooms
    public void render(SpriteBatch batch) {
        for (Mushroom mushroom : mushrooms) {
            mushroom.render(batch);
        }
    }

    // checks if the player has picked up a mushroom

    public boolean update(float delta, Player player) {
        // count down the dizziness timer
        if (dizzinessTimer > 0) {
            dizzinessTimer -= delta;
        }

        // checks if the player collects a mushroom
        for (int i = mushrooms.size() - 1; i >= 0; i--) {

            Mushroom mushroom = mushrooms.get(i);

            float distanceX = Math.abs(player.getX() - mushroom.getX());
            float distanceY = Math.abs(player.getY() - mushroom.getY());

            if (distanceX < 0.8f && distanceY < 0.8f) {

                mushrooms.remove(i);

                // chance of mushroom being bad/good is chosen
                int chance = MathUtils.random(1, 100);


                if (chance <= 75) {

                    // only heal if the player isn't at full health
                    if (player.getHealth() < player.getMaxHealth()) {
                        player.heal(20f); // heal 20hp
                        return false;
                    }

                } else {
                    dizzinessTimer = 10f; // dizzy for 10s
                    return true;
                }

            }
        }
        return false;
    }

    // checks whether the player is dizzy
    public boolean isDizzy() {
        return dizzinessTimer > 0;
    }
}
