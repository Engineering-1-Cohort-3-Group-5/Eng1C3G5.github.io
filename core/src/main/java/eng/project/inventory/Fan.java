package eng.project.inventory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eng.project.collisions.Box;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.Player;
import eng.project.game.CameraController;


/** An inventory item that sends a blast of wind towards the mouse direction to push geese  */
public class Fan extends Item{
    private CollisionHandler collisionHandler;
    private ColliderBody windBody;
    private Player player;
    private ItemState state = ItemState.Usable;
    private final CameraController camera;
    private final TextureRegion windTexture;
    private float directionX;
    private float directionY;
    private float timeRemaining;
    private float originY;
    private float originX;
    private float pushStep;
    private final float speed = 6.0f; //wind collider speed in units per second
    private final float pushSpeed = 6.0f;
    private float rechargeTime = 10;

    /** Creates a fan with an inventory icon and wind texture. */
    public Fan(CollisionHandler collisionHandler, Player player, CameraController camera, String name, TextureRegion icon, TextureRegion windTexture){
        super(name, icon);
        this.collisionHandler = collisionHandler;
        this.player = player;
        this.camera = camera;
        this.windTexture = windTexture;
    }

    /** replaces any active wind blasts with one aimed from the player toward the mouse direction */
    @Override
    public boolean Use(){
        Vector2 mousePos = camera.getWorldPosition(Gdx.input.getX(), Gdx.input.getY());
        float targetX = mousePos.x;
        float targetY = mousePos.y;
        float playerX = player.getX();
        float playerY = player.getY();
        if(windBody != null){
            collisionHandler.remove(windBody);
            windBody = null;

        }
        float dx = targetX - playerX;
        float dy = targetY - playerY;
        float length = (float) Math.sqrt(dx * dx + dy * dy);


        if(length < 0.0001f){
            return false;
        }

        directionX = dx / length;
        directionY = dy / length;
        originX = playerX;
        originY = playerY;

        float spawnX = playerX + directionX * 0.3f; //distance ahead where blast starts
        float spawnY = playerY + directionY * 0.3f; //both need to be equal

        //new wind colluder so that half width and half height makes a 1x1 box, larger box more geese but bigger jumps
        windBody = new ColliderBody(this, spawnX, spawnY, false,
            new Box(0.5f, 0.5f), true);
        collisionHandler.add(windBody);
        timeRemaining = 0.75f; //blast lasts for 0.75 seconds, can be changed, distance = speed * time
        pushStep = 0f;
        return false;
    }

    /** Updates the wind blast position and removes it after a set time. */
    public void update(float delta){
        if (windBody == null){
            return;
        }
        pushStep = pushSpeed * delta;
        windBody.push(
            directionX * speed * delta,
            directionY * speed * delta);

        timeRemaining -= delta;

        if (timeRemaining <= 0f){
            collisionHandler.remove(windBody);
            windBody = null;
        }
        
    }

    /** Renders the wind blast texture at the wind collider position. */
    public void render(SpriteBatch batch){
        if (windBody == null){
            return;
        }    
        float rotation = (float) Math.toDegrees(Math.atan2(directionY, directionX));
    
        batch.draw(
            windTexture,
            windBody.getX() - 0.5f, windBody.getY() - 0.5f,
            0.5f, 0.5f,
            1f, 1f,
            1f, 1f,
            rotation
        );
    }

    /** Pushes a goose in the direction of the wind blast if it is in front of the blast. */
    public void pushGoose(ColliderBody gooseBody){
        float dx = gooseBody.getX() - originX;
        float dy = gooseBody.getY() - originY;
        float fowardDistance = dx * directionX + dy * directionY;
        if (fowardDistance < 0f){
            return;
        }
        gooseBody.push(
            directionX * pushStep,
            directionY * pushStep);
    }

    @Override
    public void Recharge(){
        while (rechargeTime > 0){
            rechargeTime -= 1;
        }
        rechargeTime = 10;
        state = ItemState.Usable;
    }
}
