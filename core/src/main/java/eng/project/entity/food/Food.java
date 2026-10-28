package eng.project.entity.food;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** A temporary food item that geese can use as a movement target. */
public class Food {
    private final TextureRegion foodTexture;
    private float x,y;
    private final float size = 1;
    private float timer = 5; // How long food lives

    public Food(TextureRegion texture,float x,float y){
        foodTexture = texture;
        this.x = x;
        this.y = y;
    }

    /** Counts down the item's lifetime. */
    public void update(float delta) {
        timer -= delta;
    }
    public void render(SpriteBatch batch) {
        batch.draw(foodTexture, x - size / 2f, y - size / 2f, size, size);
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getSize() {
        return size;
    }
    public boolean isFinished() {
        return timer <= 0;
    }
}
