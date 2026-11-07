package eng.project.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.Circle;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.geese.Goose;
import eng.project.world.Map;

/** Player-controlled circular entity. Input is sampled in {@link #update(float)}. */
public class Player {
    private final TextureRegion playerTexture;
    private ColliderBody body;
    private float speed = 3;
    private float size = 1f;
    private boolean hasKeycard = false;

    /** Creates the player at the world origin and registers its collider. */
    public Player(TextureRegion texture, CollisionHandler collisionHandler){
        body = new ColliderBody(this,-30,-30,false,new Circle(0.4f));
        collisionHandler.add(body);
        playerTexture = texture;
    }

    /** Reads WASD input and moves at a consistent speed in every direction. */
    public void update(float delta) {
        // Get input direction
        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.A)&&getX() > (-Map.getWidth()/2f)) dx--;
        if (Gdx.input.isKeyPressed(Input.Keys.D)&&getX() < (Map.getWidth()/2f)) dx++;
        if (Gdx.input.isKeyPressed(Input.Keys.S)&&getY() > (-Map.getHeight()/2f)) dy--;
        if (Gdx.input.isKeyPressed(Input.Keys.W)&&getY() < Map.getHeight()/2f) dy++;

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
    public void pickupKeycard(){
        hasKeycard = true;
    }
    public boolean hasKeycard(){
        return hasKeycard;
    }
}
