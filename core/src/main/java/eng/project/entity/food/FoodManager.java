package eng.project.entity.food;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

/** Creates, expires, renders, and locates food items in the world. */
public class FoodManager {
    private final ArrayList<Food> food = new ArrayList<>(); // Generic list allows for easy addition of throwable items
    private final TextureRegion foodTexture;
    public FoodManager(TextureRegion foodTexture){
        this.foodTexture = foodTexture;
    }

    public void spawnFood(float x, float y) {
        food.add(new Food(foodTexture, x, y));
    }

    /** Updates food lifetimes and removes finished items  */
    public void update(float delta) {
        for (Food item : food) {
            item.update(delta);
        }

        food.removeIf(Food::isFinished);
    }

    /** Returns the nearest active food and its squared distance, or {@code null} if none exist. */
    public FoodTarget findClosestFood(float gooseX, float gooseY) {
        Food closest = null;
        float closestDistance2 = Float.MAX_VALUE;

        for (Food item : food) {
            if (!item.isFinished()) {
                float dx = item.getX() - gooseX;
                float dy = item.getY() - gooseY;
                float distance2 = dx*dx + dy*dy;

                if (distance2 < closestDistance2) {
                    closest = item;
                    closestDistance2 = distance2;
                }
            }
        }

        // Return both food itself and distance squared if food exists
        return closest == null ? null : new FoodTarget(closest, closestDistance2);
    }

    public void render(SpriteBatch batch){
        for (Food item : food) {
            item.render(batch);
        }
    }
}
