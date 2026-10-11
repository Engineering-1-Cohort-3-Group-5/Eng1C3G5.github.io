package eng.project.entity.geese;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Null;
import eng.project.entity.thrown.Food;
import eng.project.entity.thrown.FoodTarget;
import eng.project.entity.thrown.ThrownItemManager;

public class Goose {
    private final TextureRegion gooseTexture;  // Having each goose store its own texture could allow for variations in how geese appear
    private float x,y,rotation,targetX,targetY,reTargetTimer;
    private final float speed, size,sightDistance2;
    private GooseState state = GooseState.Idle;

    public Goose(float spawnX, float spawnY, TextureRegion texture){
        gooseTexture = texture;
        x = spawnX;
        y = spawnY;

        // Add some random variation to their stats
        speed = MathUtils.random(0.5f, 2.5f);
        size = MathUtils.random(0.8f, 1.2f);
        sightDistance2 = MathUtils.random(16f, 49f); // Squared distance the goose can see, a minor optimisation to avoid multiplications
    }
    public void target(float playerX, float playerY, float delta, ThrownItemManager thrownItemManager){
        if (reTargetTimer > 0){
            reTargetTimer -= delta; // Tick the timer down to 0

            if (state == GooseState.ChasingPlayer){ // Update moving target pos
                targetY = playerY;
                targetX = playerX;
            }
        }
        else{
            reTargetTimer = 0.5f; // How often the goose checks for a new target

            FoodTarget closestFood = thrownItemManager.findClosestFood(x,y); // Get nearest food

            if (closestFood != null && closestFood.distance2() <= sightDistance2){ // If food exists and is within sight
                Food food = closestFood.food();
                // Set target as food
                state = GooseState.ChasingFood;
                targetX = food.getX();
                targetY = food.getY();
                return;
            }
            else{
                float dx = playerX - x;
                float dy = playerY - y;
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

    public void move(float delta){
        if (state != GooseState.Idle) {
            // Get dist
            float dx = targetX - x;
            float dy = targetY - y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // Return if already touching
            if (distance < 0.0001f) return;

            // Move toward target without overshooting
            float step = Math.min(speed * delta, distance);
            x += dx / distance * step;
            y += dy / distance * step;

            // Calculate angle in degrees (plus offset to line up sprite)
            rotation = -(float) Math.atan2(dx, dy) * MathUtils.radiansToDegrees - 90;
        }
    }

    public void render(SpriteBatch batch){
        batch.draw(
            gooseTexture,
            x - size / 2f, y - size / 2f,
            size / 2f, size / 2f,
            size, size,
            1f, 1f,
            rotation
        );
    }

    public void push(float dx, float dy){
        x += dx;
        y += dy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius(){
        return size/2f;
    }
}
