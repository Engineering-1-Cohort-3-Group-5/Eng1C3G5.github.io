package eng.project.collisions;

/**
 * Position and collision shape for a world object. Positions represent shape centres.
 * Movement and collision resolution both use {@link #push(float, float)}.
 */
public class ColliderBody {
    private Shape shape;
    private boolean isTrigger;
    private float x,y;
    /** Creates a body at the supplied world position. Trigger behaviour is reserved for future event handling. */
    public ColliderBody(float x, float y,boolean isTrigger,Shape shape){
        this.isTrigger = isTrigger;
        this.x = x;
        this.y = y;
        this.shape = shape;
    }
    /** Returns the shape used by {@link CollisionHandler}. */
    public Shape getShape(){
        return shape;
    }
    /** Returns whether this body is intended to be a non-solid trigger. */
    public boolean isTrigger(){
        return isTrigger;
    }
    public float getX() {
        return x;
    }
    public float getY(){
        return y;
    }
    /** Applies a displacement in world units. */
    public void push(float dx,float dy){
        x+=dx;
        y+=dy;
    }
}
