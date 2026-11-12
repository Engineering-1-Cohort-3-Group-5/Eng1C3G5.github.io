package eng.project.entity.students;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import eng.project.collisions.Circle;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.world.Map;

public class Student {

    private static final float mapWidth = Map.getWidth();
    private static final float mapHeight = Map.getHeight();
    private static final float targetX =
        MathUtils.random((-mapWidth) / 2f, mapWidth / 2f);
    private static final float targetY = mapHeight / 2f;



    private final TextureRegion studentTexture;
    private final float speed, size;
    private float rotation;
    private ColliderBody body;
    private StudentState state = StudentState.Travelling;

    /**
     * Creates a student with set visual size, and movement speed.
     */
    public Student(float spawnX, float spawnY,
                   TextureRegion texture,
                   CollisionHandler collisionHandler) {
        // Add some random variation to their stats
        speed = 2f;
        size = 2f;

        studentTexture = texture;
        body = new ColliderBody(this, spawnX, spawnY, false,
            new Circle(size / 2f), true);
        collisionHandler.add(body);
    }

    /**
     * Moves towards the map location, aiming to reach within 1f of
     * it.
     */
    public void move(float delta) {
        if (state != StudentState.TargetFound) {
            float dx = targetX - body.getX();
            float dy = targetY - body.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // Move toward target without overshooting
            float step = Math.min(speed * delta, distance);
            body.push(dx / distance * step, dy / distance * step);

            // Calculate angle in degrees (plus offset to line up
            // sprite)
            rotation =
                -(float) Math.atan2(dx, dy) * MathUtils.radiansToDegrees - 90;

            if (distance < 1f) {
                state = StudentState.TargetFound;
            }
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

    public StudentState getState() {
        return state;
    }

    public ColliderBody getBody() {
        return body;
    }
}

