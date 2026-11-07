package eng.project.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.*;

/** A rendered static world feature with a collider registered in the shared handler. */
public class Feature {
    private final TextureRegion texture;
    private final ColliderBody body;
    private final float spriteScale; // determines the size of the sprite relative to the collider
    /** Creates a feature at its collider centre. */
    public Feature(float x, float y,float spriteScale, Shape colliderShape, TextureRegion texture, CollisionHandler collisionHandler){
        this.spriteScale = spriteScale;
        body = new ColliderBody(this,x,y,false,colliderShape);
        collisionHandler.add(body);
        this.texture = texture;
    }
    /** Draws the texture to match its collider bounds. */
    public void render(SpriteBatch batch){
        if(body.getShape() instanceof Circle circle){
            batch.draw(texture,body.getX()-circle.radius()* spriteScale,body.getY()-circle.radius()* spriteScale,circle.radius()*2* spriteScale,circle.radius()* spriteScale);
        }
        else if (body.getShape() instanceof Box box) {
            batch.draw(texture,body.getX()-box.halfWidth()* spriteScale,body.getY()-box.halfHeight()* spriteScale,box.halfWidth() * 2* spriteScale,box.halfHeight()*2* spriteScale);
        }
    }
}
