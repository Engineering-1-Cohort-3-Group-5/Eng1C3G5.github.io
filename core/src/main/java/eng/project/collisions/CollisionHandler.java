package eng.project.collisions;

import eng.project.entity.geese.Goose;
import eng.project.entity.Player;

import java.util.ArrayList;

public class CollisionHandler {
    public static void resolvePlayerGeeseCollisions(ArrayList<Goose> geese, Player player){
        for (int i = 0; i < geese.size(); i++) {
            Goose a = geese.get(i);

            // Check player
            // Get distance between pair
            float dx = player.getX() - a.getX();
            float dy = player.getY() - a.getY();
            float minDistance = a.getRadius() + player.getRadius();
            float distanceSquared = dx * dx + dy * dy;

            // If circles overlap
            if (distanceSquared < minDistance * minDistance) {
                float distance = (float) Math.sqrt(distanceSquared); // Only calculate inside if-statement to avoid redundant maths

                // Pick a stable direction if the centers are exactly coincident.
                if (distance < 0.0001f) {
                    dx = 1f;
                    dy = 0f;
                    distance = 1f;
                }

                // Calculate the direction and magnitude of the overlap
                float overlap = minDistance - distance;
                float normalX = dx / distance;
                float normalY = dy / distance;

                // Push apart
                a.push(-normalX * overlap * 0.5f, -normalY * overlap * 0.5f);
                player.push(normalX * overlap * 0.5f, normalY * overlap * 0.5f);
            }

            // Check other geese
            for (int j = i + 1; j < geese.size(); j++) { // Avoid double-checking and checking with self
                Goose b = geese.get(j);

                dx = b.getX() - a.getX();
                dy = b.getY() - a.getY();
                minDistance = a.getRadius() + b.getRadius();
                distanceSquared = dx * dx + dy * dy;
                if (distanceSquared < minDistance * minDistance) {
                    float distance = (float) Math.sqrt(distanceSquared); // Only calculate inside if-statement to avoid redundant maths
                    if (distance < 0.0001f) {
                        dx = 1f;
                        dy = 0f;
                        distance = 1f;
                    }
                    float overlap = minDistance - distance;
                    float normalX = dx / distance;
                    float normalY = dy / distance;

                    // Help them become less rigid
                    final float smooth = 0.3f;

                    a.push(-normalX * overlap * 0.5f * smooth, -normalY * overlap * 0.5f * smooth);
                    b.push(normalX * overlap * 0.5f* smooth, normalY * overlap * 0.5f * smooth);
                }
            }
        }
    }
}
