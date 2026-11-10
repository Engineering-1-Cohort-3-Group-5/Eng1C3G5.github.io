package eng.project.collisions;

import java.util.ArrayList;

/**
 * Position and collision shape for a world object. Positions represent shape centres.
 * Movement and collision resolution both use {@link #push(float, float)}.
 */
public class ColliderBody {
    private Shape shape;
    private boolean isTrigger;
    private ArrayList<Object> others = new ArrayList<>();
    private Object owner;
    private float x,y;
    private boolean pushable;

    /** Creates a body at the supplied world position. Trigger behaviour is reserved for future event handling. */
    public ColliderBody(Object owner, float x, float y,
                        boolean isTrigger, Shape shape,
                        boolean pushable){
        this.owner = owner;
        this.isTrigger = isTrigger;
        this.x = x;
        this.y = y;
        this.shape = shape;
        this.pushable = pushable;
    }
    /** Returns the shape used by {@link CollisionHandler}. */
    public Shape getShape(){
        return shape;
    }
    /** Returns whether this body is intended to be a non-solid trigger. */
    public boolean isTrigger(){
        return isTrigger;
    }

    /** Adds a collider to this colliders list of colliding colliders*/
    public void touching(ColliderBody other){
        others.add(other.owner);
    }
    /** Sets the colliders hit marker to false*/
    public void resetTouching(){
        others.clear();
    }

    public ArrayList<Object> getTouching(){
        return this.others;
    }

    public float getX() {
        return x;
    }
    public float getY(){
        return y;
    }
    /** Applies a displacement in world units. */
    public void push(float dx,float dy){
        if (pushable == true) {
            x+=dx;
            y+=dy;
        }
    }
}
