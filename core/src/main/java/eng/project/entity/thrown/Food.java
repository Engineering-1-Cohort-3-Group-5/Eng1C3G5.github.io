package eng.project.entity.thrown;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Food implements ThrownItem{
    private final TextureRegion foodTexture;
    private float x,y;
    private final float size = 1;
    private float timer = 5; // how long food lives

    public Food(TextureRegion texture,float x,float y){
        foodTexture = texture;
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(float delta) {
        timer -= delta;
    }
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(foodTexture, x - size / 2f, y - size / 2f, size, size);
    }
    @Override
    public float getX() {
        return x;
    }
    @Override
    public float getY() {
        return y;
    }
    @Override
    public float getSize() {
        return size;
    }
    @Override
    public boolean isFinished() {
        return timer <= 0;
    }
}
