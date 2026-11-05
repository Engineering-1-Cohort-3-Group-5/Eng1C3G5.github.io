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

public class Student {
    private final TextureRegion studentTexture;
    private float rotation,targetX,targetY,reTargetTimer;
    private ColliderBody body;
    private final float speed, size,sightDistance2;
    private StudentState state = StudentState.Spawning;

    /** Creates a goose with randomized visual size, movement speed, and sight range. */
    public Student(float spawnX, float spawnY, TextureRegion texture, CollisionHandler collisionHandler){
        // Add some random variation to their stats
        speed = 0.5f;
        size = 1f;
        sightDistance2 = MathUtils.random(36f, 81f); // Squared distance the goose can see, a minor optimisation to avoid multiplications

        studentTexture = texture;
        body = new ColliderBody(this,spawnX,spawnY,true, new Circle(size/2f));
        collisionHandler.add(body);
    }
    /** Periodically selects the highest-priority visible target. */
    public void target(float playerX, float playerY, float delta, FoodManager foodManager){
        if (reTargetTimer > 0){
            reTargetTimer -= delta; // Tick the timer down to 0

            if (state == StudentState.Leaving){ // Update moving target pos
                targetY = playerY;
                targetX = playerX;
            }
        }
        else{
            reTargetTimer = 0.5f; // How often the goose checks for a new target

            FoodTarget closestFood = foodManager.findClosestFood(body.getX(), body.getY()); // Get nearest food

            if (closestFood != null && closestFood.distance2() <= sightDistance2){ // If food exists and is within sight
                Food food = closestFood.food();
                // Set target as food
                state = StudentState.Leaving;
                targetX = food.getX();
                targetY = food.getY();
                return;
            }
            else{
                float dx = playerX - body.getX();
                float dy = playerY - body.getY();
                if (dx*dx + dy*dy <= sightDistance2){ // If player within sight
                    // Set target as player
                    state = StudentState.Leaving;
                    targetY = playerY;
                    targetX = playerX;
                    return;
                }
            }
            // Idle if not after player or food
            state = StudentState.Spawning;
        }
    }

    /** Moves towards the current target without overshooting it. */
    public void move(float delta){
        if (state != StudentState.Spawning) {
            // Get dist
            float dx = targetX - body.getX();
            float dy = targetY - body.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // Return if already touching
            if (distance < 0.0001f) return;

            // Move toward target without overshooting
            float step = Math.min(speed * delta, distance);
            body.push(dx / distance * step,dy / distance * step);

            // Calculate angle in degrees (plus offset to line up sprite)
            rotation = -(float) Math.atan2(dx, dy) * MathUtils.radiansToDegrees - 90;
        }
    }

    public void render(SpriteBatch batch){
        batch.draw(
            studentTexture,
            body.getX() - size / 2f, body.getY() - size / 2f,
            size / 2f, size / 2f,
            size, size,
            1f, 1f,
            rotation
        );
    }

    public float getRadius(){
        return size/2f;
    }
}

