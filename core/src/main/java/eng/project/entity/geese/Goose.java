package eng.project.entity.geese;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import eng.project.collisions.Circle;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.food.Food;
import eng.project.entity.food.FoodTarget;
import eng.project.entity.food.FoodManager;

/**
 * A moving goose whose simple state machine chooses food before chasing a nearby player.
 * Add future behaviours by extending {@link GooseState} and this target-selection method.
 */
public class Goose {
    private final TextureRegion gooseTexture;  // Having each goose store its own texture could allow for variations in how geese appear
    private float rotation,targetX,targetY,reTargetTimer;
    private ColliderBody body;
    private final float speed, size,sightDistance2;
    private GooseState state = GooseState.Idle;

    /** Creates a goose with randomized visual size, movement speed, and sight range. */
    public Goose(float spawnX, float spawnY, TextureRegion texture, CollisionHandler collisionHandler){
        // Add some random variation to their stats
        speed = MathUtils.random(0.5f, 2.5f);
        size = MathUtils.random(0.8f, 1.2f);
        sightDistance2 = MathUtils.random(64, 144); // Squared distance the goose can see, a minor optimisation to avoid multiplications

        gooseTexture = texture;
        body = new ColliderBody(this,spawnX,spawnY,false, new Circle(size/2f));
        collisionHandler.add(body);
    }
    /** Periodically selects the highest-priority visible target. */
    public void target(float playerX, float playerY, float delta, FoodManager foodManager){
        if (reTargetTimer > 0){
            reTargetTimer -= delta; // Tick the timer down to 0

            if (state == GooseState.ChasingPlayer){ // Update moving target pos
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
                state = GooseState.ChasingFood;
                targetX = food.getX();
                targetY = food.getY();
                return;
            }
            else{
                float dx = playerX - body.getX();
                float dy = playerY - body.getY();
                if (dx*dx + dy*dy <= sightDistance2){ // If player within sight
                    // Set target as player
                    state = GooseState.ChasingPlayer;
                    targetY = playerY;
                    targetX = playerX;
                    return;
                }
            }
            // Idle if not after player or food
            state = GooseState.Idle;
        }
    }

    /** Moves towards the current target without overshooting it. */
    public void move(float delta){
        if (state != GooseState.Idle) {
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
            gooseTexture,
            body.getX() - size / 2f, body.getY() - size / 2f,
            size / 2f, size / 2f,
            size, size,
            1f, 1f,
            rotation
        );
    }
}
