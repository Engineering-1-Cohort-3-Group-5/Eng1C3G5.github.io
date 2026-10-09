package eng.project.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private final TextureRegion playerTexture;
    private float x = 2f;
    private float y = 2f;
    private float speed = 2f;
    private float size = 1f;

    public Player(TextureRegion texture){
        playerTexture = texture;
    }

    public void update(float delta) {
        // Get input direction

        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx--;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx++;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy--;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy++;

        // Move in normalised direction

        float length = (float) Math.sqrt(dx * dx + dy * dy);

        if (length > 0f) {
            x += (dx / length) * speed * delta;
            y += (dy / length) * speed * delta;
        }
    }
    public void render(SpriteBatch batch){
        // Centred around x and y
        batch.draw(playerTexture,x-size/2f, y-size/2f, size, size);
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
    public void push(float dx, float dy){
        x += dx;
        y += dy;
    }
}
