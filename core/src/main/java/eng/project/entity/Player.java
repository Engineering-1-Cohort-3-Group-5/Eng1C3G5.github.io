package eng.project.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.Circle;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;

/** Player-controlled circular entity. Input is sampled in {@link #update(float)}. */
public class Player {
    private final TextureRegion playerTexture;
    private ColliderBody body;
    private float speed = 3f;
    private float size = 1f;

    /** Creates the player at the world origin and registers its collider. */
    public Player(TextureRegion texture, CollisionHandler collisionHandler){
        body = new ColliderBody(0,0,false,new Circle(0.6f));
        collisionHandler.add(body);
        playerTexture = texture;
    }

    /** Reads WASD input and moves at a consistent speed in every direction. */
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
            body.push((dx / length) * speed * delta,(dy / length) * speed * delta);
        }
    }
    public void render(SpriteBatch batch){
        // Centred around x and y
        batch.draw(playerTexture,body.getX()-size/2f, body.getY()-size/2f, size, size);
    }
    public float getX() {
        return body.getX();
    }
    public float getY() {
        return body.getY();
    }
}
