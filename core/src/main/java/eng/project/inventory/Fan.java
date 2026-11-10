package eng.project.inventory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import eng.project.collisions.Box;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.Player;
import eng.project.game.CameraController;


public class Fan extends Item{
    private CollisionHandler collisionHandler;
    private ColliderBody windBody;
    private Player player;
    private ItemState state = ItemState.Usable;
    private final CameraController camera;
    private float directionX;
    private float directionY;
    private float timeRemaining;
    private float originY;
    private float originX;
    private float pushStep;
    private final float speed = 6.0f; //wind collider speed in units per second
    private final float pushSpeed = 6.0f;
    private float rechargeTime = 10;


    public Fan(CollisionHandler collisionHandler, Player player, CameraController camera){
        this.collisionHandler = collisionHandler;
        this.player = player;
        this.camera = camera;
    }

    @Override
    public void Use(){
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
            return;
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
        System.out.println("Fan used wind collider created");
        timeRemaining = 0.75f; //blast lasts for 0.75 seconds, can be changed, distance = speed * time
        pushStep = 0f;
    }

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
