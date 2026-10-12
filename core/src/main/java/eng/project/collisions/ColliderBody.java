package eng.project.collisions;

public class Collider {
    private ColliderShape shape;
    private boolean isTrigger;

    public ColliderShape getShape(){
        return shape;
    }

    public boolean isTrigger(){
        return isTrigger;
    }
}
