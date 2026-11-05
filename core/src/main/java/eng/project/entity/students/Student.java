package eng.project.entity.students;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import eng.project.collisions.Circle;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.food.Food;
import eng.project.entity.food.FoodManager;
import eng.project.entity.food.FoodTarget;
import eng.project.entity.geese.GooseState;
import eng.project.world.Map;

public class Student {

    private static final float mapWidth = Map.getWidth();
    private static final float mapHeight = Map.getHeight();
    private static final float targetX =
        MathUtils.random(((-mapWidth) / 2f), (mapWidth / 2f));
    private static final float targetY =
        MathUtils.random(((-mapHeight) / 2f), (mapHeight / 2f));

    private final TextureRegion studentTexture;
    private final float speed, size, sightDistance2;
    private float rotation;
    private ColliderBody body;
    private StudentState state = StudentState.Spawning;

    /**
     * Creates a goose with randomized visual size, movement speed,
     * and sight range.
     */
    public Student(float spawnX, float spawnY,
                   TextureRegion texture,
                   CollisionHandler collisionHandler) {
        // Add some random variation to their stats
        speed = 0.5f;
        size = 1f;
        sightDistance2 = MathUtils.random(36f, 81f); // Squared
        // distance the goose can see, a minor optimisation to
        // avoid multiplications

        studentTexture = texture;
        body = new ColliderBody(this, spawnX, spawnY, true,
            new Circle(size / 2f));
        collisionHandler.add(body);
    }

    /**
     * Moves towards the map location without overshooting it.
     */
    public void move(float delta) {
        if (state != StudentState.Spawning) {
            // Get dist
            float dx = targetX - body.getX();
            float dy = targetY - body.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // Return if already touching
            if (distance < 0.0001f) return;

            // Move toward target without overshooting
            float step = Math.min(speed * delta, distance);
            body.push(dx / distance * step, dy / distance * step);

            // Calculate angle in degrees (plus offset to line up
            // sprite)
            rotation =
                -(float) Math.atan2(dx, dy) * MathUtils.radiansToDegrees - 90;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(
            studentTexture,
            body.getX() - size / 2f, body.getY() - size / 2f,
            size / 2f, size / 2f,
            size, size,
            1f, 1f,
            rotation
        );
    }

    public float getRadius() {
        return size / 2f;
    }
}

