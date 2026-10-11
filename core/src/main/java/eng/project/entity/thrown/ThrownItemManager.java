package eng.project.entity.thrown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class ThrownItemManager {
    private final ArrayList<ThrownItem> thrownItems = new ArrayList<>(); // Generic list allows for easy addition of throwable items
    private final TextureRegion foodTexture;
    public ThrownItemManager(TextureRegion foodTexture){
        this.foodTexture = foodTexture;
    }

    public void update(float delta,float playerX, float playerY) {
        if (Gdx.input.justTouched()){
            // Place food on player if mouse clicked
            thrownItems.add(new Food(foodTexture,playerX,playerY));
        }

        for (ThrownItem item : thrownItems) {
            item.update(delta);
        }

        thrownItems.removeIf(ThrownItem::isFinished);
    }
    public FoodTarget findClosestFood(float gooseX, float gooseY) {
        Food closest = null;
        float closestDistance2 = Float.MAX_VALUE;

        for (ThrownItem item : thrownItems) {
            if (item instanceof Food food && !food.isFinished()) {
                float dx = food.getX() - gooseX;
                float dy = food.getY() - gooseY;
                float distance2 = dx*dx + dy*dy;

                if (distance2 < closestDistance2) {
                    closest = food;
                    closestDistance2 = distance2;
                }
            }
        }

        // Return both food itself and distance squared if food exists
        return closest == null ? null : new FoodTarget(closest, closestDistance2);
    }


    public void render(SpriteBatch batch){
        for (ThrownItem item : thrownItems) {
            item.render(batch);
        }
    }
}
