package eng.project.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.Main;
import eng.project.collisions.*;

public class Bus {
    private final TextureRegion texture;
    private final ColliderBody body;
    private final float width = 5;
    private final float height = 10;
    public Bus(float x, float y, Texture texture, CollisionHandler collisionHandler){
        body = new ColliderBody(this,x,y,false,new Box(width/2,height/2),false);
        collisionHandler.add(body);
        this.texture = new TextureRegion(texture);
    }
    /** Draws the texture to match its collider bounds. */
    public void render(SpriteBatch batch){
        batch.draw(
            texture,
            body.getX() - width / 2f, body.getY() - height / 2f,
            width, height
        );
    }
    public boolean update(Main application){
        for (Object other: body.getTouching()){
            if (other instanceof Player){
                if(((Player) other).hasKeycard()){
                    application.showVictoryScreen();
                    return true;
                }
                else{
                    // Need keycard
                }
            }
        }
        return false;
    }
}
