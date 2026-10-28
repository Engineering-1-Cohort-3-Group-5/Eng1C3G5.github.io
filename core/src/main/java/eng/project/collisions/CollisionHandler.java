package eng.project.collisions;

import java.util.ArrayList;

/**
 * Performs pairwise collision detection and positional resolution for registered bodies.
 * New shape combinations belong in {@link #resolveCollision(ColliderBody, ColliderBody)}.
 */
public class CollisionHandler {
    private final ArrayList<ColliderBody> colliders = new ArrayList<>();

    /** Registers a body to be checked on each call to {@link #resolveCollisions()}. */
    public void add(ColliderBody collider){
        colliders.add(collider);
    }
    /** Stops checking a body. Call this when its owning object is removed. */
    public void remove(ColliderBody collider){
        colliders.remove(collider);
    }
    /** Checks each unordered pair once and separates pairs that overlap. */
    public void resolveCollisions(){
        for (int i = 0; i < colliders.size(); i++){
            ColliderBody a = colliders.get(i);

            for (int j = i + 1; j < colliders.size(); j++){ // Avoid double-checking and checking against self
                ColliderBody b = colliders.get(j);

                resolveCollision(a,b);
            }
        }
    }
    private void resolveCollision(ColliderBody a, ColliderBody b){
        if (a.getShape() instanceof Circle circleA && b.getShape() instanceof Circle circleB) {
            circleCircle(a, circleA, b, circleB);
        }
        else if (a.getShape() instanceof Box boxA && b.getShape() instanceof Box boxB) {
            boxBox(a,boxA,b,boxB);
        }
        else if (a.getShape() instanceof Box box && b.getShape() instanceof Circle circle) {
            boxCircle(b,circle,a,box);

        } else if (a.getShape() instanceof Circle circle && b.getShape() instanceof Box box) {
            boxCircle(a,circle,b,box);
        }

    }
    /** Resolves a circle against a solid, axis-aligned box by moving the circle only. */
    private void boxCircle(ColliderBody circleCollider, Circle circle, ColliderBody boxCollider, Box box){
        // Clamp the centre to find the nearest point when the circle is outside the box.
        float boxLeft = boxCollider.getX() - box.halfWidth();
        float boxRight = boxCollider.getX() + box.halfWidth();
        float closestX = Math.max(boxLeft,Math.min(circleCollider.getX(),boxRight));

        float boxTop = boxCollider.getY() + box.halfHeight();
        float boxBottom = boxCollider.getY() - box.halfHeight();
        float closestY = Math.max(boxBottom,Math.min(circleCollider.getY(),boxTop));

        float dx = circleCollider.getX() - closestX;
        float dy = circleCollider.getY() - closestY;
        float distanceSquared = dx * dx + dy * dy;

        if (distanceSquared > 0f) {
            float distance = (float) Math.sqrt(distanceSquared);
            if (distance < circle.radius()) {
                // The normal points from the closest box point towards the circle centre.
                float penetration = circle.radius() - distance;
                circleCollider.push(dx / distance * penetration, dy / distance * penetration);
            }
            return;
        }

        // A zero distance means the centre is inside (or exactly on) the box. Move it
        // through the nearest face; the radius must clear that face as well.
        float toLeft = circleCollider.getX() - boxLeft;
        float toRight = boxRight - circleCollider.getX();
        float toBottom = circleCollider.getY() - boxBottom;
        float toTop = boxTop - circleCollider.getY();

        float nearestFaceDistance = toLeft;
        float normalX = -1f;
        float normalY = 0f;
        if (toRight < nearestFaceDistance) {
            nearestFaceDistance = toRight;
            normalX = 1f;
            normalY = 0f;
        }
        if (toBottom < nearestFaceDistance) {
            nearestFaceDistance = toBottom;
            normalX = 0f;
            normalY = -1f;
        }
        if (toTop < nearestFaceDistance) {
            nearestFaceDistance = toTop;
            normalX = 0f;
            normalY = 1f;
        }
        circleCollider.push(normalX * (nearestFaceDistance + circle.radius()),
            normalY * (nearestFaceDistance + circle.radius()));
    }
    private void boxBox(ColliderBody colliderA, Box boxA, ColliderBody colliderB, Box boxB){
        float dx = colliderA.getX() - colliderB.getX();
        float dy = colliderA.getY() - colliderB.getY();

        float overlapX = boxA.halfWidth() + boxB.halfWidth() - Math.abs(dx);
        float overlapY = boxA.halfHeight() + boxB.halfHeight() - Math.abs(dy);

        // They are separated on at least one axis.
        if (overlapX <= 0f || overlapY <= 0f) {
            return;
        }

        // Resolve on the axis requiring the least movement.
        if (overlapX < overlapY) {
            float pushX = (dx < 0f ? -overlapX : overlapX) / 2f;

            colliderA.push(pushX, 0f);
            colliderB.push(-pushX, 0f);
        } else {
            float pushY = (dy < 0f ? -overlapY : overlapY) / 2f;

            colliderA.push(0f, pushY);
            colliderB.push(0f, -pushY);
        }
    }
    private void circleCircle(ColliderBody colliderA, Circle circleA, ColliderBody colliderB, Circle circleB) {
        // Get distance between pair
        float dx = colliderB.getX() - colliderA.getX();
        float dy = colliderB.getY() - colliderA.getY();
        float distanceSquared = dx*dx + dy*dy;
        float combinedRadius = circleA.radius() + circleB.radius();

        // If circles overlap
        if (distanceSquared < combinedRadius * combinedRadius){
            float distance = (float) Math.sqrt(distanceSquared); // Only calculate inside if-statement to avoid redundant maths

            // Pick a stable direction if the centers are exactly coincident.
            if (distance < 0.0001f) {
                dx = 1f;
                dy = 0f;
                distance = 1f;
            }

            // Calculate the direction and magnitude of the overlap
            float overlap = combinedRadius - distance;
            float normalX = dx / distance;
            float normalY = dy / distance;

            // Help them become less rigid
            final float smooth = 0.3f;

            // Push apart
            colliderA.push(-normalX * overlap * 0.5f * smooth, -normalY * overlap * 0.5f * smooth);
            colliderB.push(normalX * overlap * 0.5f* smooth, normalY * overlap * 0.5f * smooth);
        }
    }
}
