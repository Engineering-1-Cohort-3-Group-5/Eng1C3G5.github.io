package eng.project.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.*;

/** A rendered static world feature with a collider registered in the shared handler. */
public class Feature {
    private final TextureRegion texture;
    private final ColliderBody body;
    /** Creates a feature at its collider centre. */
    public Feature(float x, float y, Shape colliderShape, TextureRegion texture, CollisionHandler collisionHandler){
        body = new ColliderBody(this,x,y,false,colliderShape);
        collisionHandler.add(body);
        this.texture = texture;
    }
    /** Draws the texture to match its collider bounds. */
    public void render(SpriteBatch batch){
        if(body.getShape() instanceof Circle circle){
            batch.draw(texture,body.getX()-circle.radius(),body.getY()-circle.radius(),circle.radius()*2,circle.radius()*2);
        }
        else if (body.getShape() instanceof Box box) {
            batch.draw(texture,body.getX()-box.halfWidth(),body.getY()-box.halfHeight(),box.halfWidth() * 2,box.halfHeight()*2);
        }
    }

    public float getX(){
        return body.getX();
    }

    public float getY(){
        return body.getY();
    }

    public float getHalfWidth(){
        Shape shape = body.getShape();

        if (shape instanceof Box box) {
            return box.halfWidth();
        }
        else if (shape instanceof Circle circle) {
            return circle.radius();
        }
        return 0f;
    }

    public float getHalfHeight(){
        Shape shape = body.getShape();

        if (shape instanceof Box box) {
            return box.halfHeight();
        }
        else if (shape instanceof Circle circle) {
            return circle.radius();
        }
        return 0f;
    }
}
