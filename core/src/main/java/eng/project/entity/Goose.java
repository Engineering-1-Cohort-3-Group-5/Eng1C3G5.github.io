package eng.project.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Goose {

    // Having each goose store its own texture allows for variations in how geese appear
    private final TextureRegion gooseTexture;
    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private float speed = 1f;

    private float size = 0.8f;

    public Goose(float spawnX, float spawnY, TextureRegion texture){
        gooseTexture = texture;
        x = spawnX;
        y = spawnY;
        targetX = 0;
        targetY = 0;
    }

    public void update(float delta){
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
    }

    public void render(SpriteBatch batch){
        // Centred around x and y
        batch.draw(gooseTexture,x - size/2f,y - size/2f,size,size);
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
