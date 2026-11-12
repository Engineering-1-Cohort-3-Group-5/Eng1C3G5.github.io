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

    // Sprinting
    private final float sprintMultiplier = 1.8f;
    private final float staminaDrainPerSecond = 30f;
    private final float staminaRegenPerSecond = 15f;

    // Health
    private final float maxHealth = 100f;
    private float health = maxHealth;
    private final float maxStamina = 100f;
    private float stamina = maxStamina;
    private final float sprintRecoverThreshold = 25f;
    private boolean exhausted = false;

    /** Creates the player at the world origin and registers its collider. */
    public Player(TextureRegion texture, CollisionHandler collisionHandler){
        body = new ColliderBody(this,-30,-30,false,new Circle(0.4f),
            true);
        collisionHandler.add(body);
        playerTexture = texture;
    }

    /** Reads WASD input and moves at a consistent speed in every direction. Left Shift to sprint if not exhausted */
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

        boolean moving = length > 0f;
        boolean sprinting = moving && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !exhausted;
        updateStamina(delta, sprinting);
        // Move in normalised direction and move faster when sprinting
        if (moving) {
            float currentSpeed = sprinting ? speed * sprintMultiplier : speed;
            body.push((dx / length) * currentSpeed * delta, (dy / length) * currentSpeed * delta);
        }
    }

    /** Drains the stamina whilst sprinting and regenerates it otherwise. Sprinting until 0 stamina locks out sprinting until recovered past threshold */
    public void updateStamina(float delta, boolean sprinting) {
        if (sprinting) {
            stamina -= staminaDrainPerSecond * delta;
        } else {
            stamina += staminaRegenPerSecond * delta;
        }
        stamina = Math.max(0f, Math.min(maxStamina, stamina));

        if (stamina <= 0f) {
            exhausted = true;
        }
        if (exhausted && stamina >= sprintRecoverThreshold) {
            exhausted = false;
        }
    }

    /** Reduces health but never below zero */
    public void damage(float amount) {
        health = Math.max(0f, health - amount);
    }

    /** Restores health but never above max (100) */
    public void heal(float amount) {
        health = Math.min(maxHealth, health + amount);
    }

    /** Returns true once health has reached zero */
    public boolean isDead() {
        return health <= 0f;
    }

    public void render(SpriteBatch batch){
        // Centred around x and y
        batch.draw(playerTexture,body.getX()-size/2f, body.getY()-size/2f, size, size);
    }

    // Getters for position, health and stamina values
    public float getX() {
        return body.getX();
    }

    public float getY() {
        return body.getY();
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getStamina() {
        return stamina;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    /** Returns true while sprinting is locked after running out of stamina */
    public boolean isExhausted() {
        return exhausted;
    }
}
