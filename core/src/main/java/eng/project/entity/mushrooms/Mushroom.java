package eng.project.entity.mushrooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Mushroom {

    private final float x;
    private final float y;
    private final float size = 1f;

    private final TextureRegion texture;

    // creates a mushroom at a specific position
    public Mushroom(float x, float y, TextureRegion texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    // draws the mushroom onto  the screen
    public void render(SpriteBatch batch) {
        batch.draw(texture, x - size / 2f, y - size / 2f, size, size);
    }

    // returns the mushroom's X position
    public float getX() {
        return x;
    }

    // returns the mushroom's Y position
    public float getY() {
        return y;
    }
}
