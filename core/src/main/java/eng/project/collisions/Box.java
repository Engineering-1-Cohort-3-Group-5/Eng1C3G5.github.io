package eng.project.collisions;

/** An axis-aligned rectangular collider centred on its owning {@link ColliderBody}. */
public record Box(float halfWidth, float halfHeight) implements Shape {
}
