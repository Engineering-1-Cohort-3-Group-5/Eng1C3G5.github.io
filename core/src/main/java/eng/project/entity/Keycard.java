package eng.project.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.Circle;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.Player;


public class Keycard {
    private final ColliderBody pickupTrigger;
    private final TextureRegion texture;
    private final float width = 0.5f;
    private final float height = 0.4f;
    private boolean collected = false; // Puts keycard object to sleep once collected rather than despawning

    public Keycard(TextureRegion texture, CollisionHandler collisionHandler){
        this.texture = texture;
        pickupTrigger = new ColliderBody(this,30,25,true,new Circle(0.5f),false);
        collisionHandler.add(pickupTrigger);
    }

    public void update(){
        if (!collected) {
            for (Object other : pickupTrigger.getTouching()) {
                if (other instanceof Player) {
                    ((Player) other).pickupKeycard();
                    collected = true;
                }
            }
        }
    }

    public void render(SpriteBatch batch){
        if (!collected){
            batch.draw(texture,pickupTrigger.getX()-width/2,pickupTrigger.getY()-height/2,width,height);
        }
    }
}
