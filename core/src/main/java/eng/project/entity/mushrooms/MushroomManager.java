
package eng.project.entity.mushrooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import eng.project.world.Map;

import java.util.ArrayList;

public class MushroomManager {

    private final ArrayList<Mushroom> mushrooms = new ArrayList<>();
    private float dizzinessTimer = 0f;

    // makes 10 mushrooms at random positions
    public MushroomManager(TextureRegion texture, Map map) {

        for (int i = 0; i < 30; i++) {

            float x = MathUtils.random(-19f, 19f);
            float y = MathUtils.random(-19f, 19f);

            // retry if the mushroom is inside a building
            while (!map.canSpawnMushroom(x, y)) {
                x = MathUtils.random(-19f, 19f);
                y = MathUtils.random(-19f, 19f);
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

    public void update(float delta, float playerX, float playerY) {

        // count down the dizziness timer
        if (dizzinessTimer > 0) {
            dizzinessTimer -= delta;
        }

        // checks if the player collects a mushroom
        for (int i = mushrooms.size() - 1; i >= 0; i--) {

            Mushroom mushroom = mushrooms.get(i);

            float distanceX = Math.abs(playerX - mushroom.getX());
            float distanceY = Math.abs(playerY - mushroom.getY());

            if (distanceX < 0.8f && distanceY < 0.8f) {

                mushrooms.remove(i);

                // chance of mushroom being bad/good is chosen
                int chance = MathUtils.random(1, 100);

                if (chance <= 75) {
                    System.out.println("Good mushroom collected: more HP!");
                } else {
                    dizzinessTimer = 10f;
                    System.out.println("Bad mushroom collected: nausea for 10 seconds!");
                }
            }
        }
    }

    // checks whether the player is dizzy
    public boolean isDizzy() {
        return dizzinessTimer > 0;
    }
}
