package eng.project.entity.students;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import eng.project.collisions.Circle;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.world.Feature;
import eng.project.world.Map;

/**
 * A moving student who exits a pre-selected building and travels
 * across the map to another building at specified time.
 */
public class Student {

    private final Feature building = Map.studentTargetBuilding();
    private final float targetX =
        building.getX();
    private final float targetY =
        building.getY() + building.getHalfHeight();

    private final TextureRegion studentTexture;
    private final float speed, size;
    private final ColliderBody body;
    private float rotation;
    private StudentState state = StudentState.Travelling;

    /**
     *Creates a student with set speed and size.
     * @param spawnX is the x coordinate of where student first
     *               appears on map.
     * @param spawnY is the y coordinate of where student first
     *      *               appears on map.
     * @param texture is the student appearance.
     * @param collisionHandler dictates how student interacts with
     *                         other objects.
     */
    public Student(float spawnX, float spawnY,
                   TextureRegion texture,
                   CollisionHandler collisionHandler) {

        speed = 2f;
        size = 2f;
        studentTexture = texture;
        //
        body = new ColliderBody(this, spawnX, spawnY, false,
            new Circle(size / 2f), false);
        collisionHandler.add(body);
    }

    /**
     *Student moves towards the target.
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

    /**
     * Renders 2d graphic of current student.
     */
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

    /**
     * @return the current state of the student.
     */
    public StudentState getState() {
        return state;
    }

    /**
     * Renders 2d graphic of current student.
     * @return the interactive element of the student.
     */
    public ColliderBody getBody() {
        return body;
    }
}

