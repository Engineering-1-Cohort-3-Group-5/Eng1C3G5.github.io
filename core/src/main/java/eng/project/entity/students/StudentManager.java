package eng.project.entity.students;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.world.Feature;
import eng.project.world.Map;

/**
 * Manages a hidden event where students generate by the side
 * of a building and then travel across the map to reach
 * another building, blocking the player's path.
 */
public class StudentManager {

    private final TextureRegion studentTexture;

    private final Feature building = Map.studentStartBuilding();
    private final float spawnX =
        building.getX();
    private final float spawnY =
        building.getY() + building.getHalfHeight();

    private float spawnTimer = 1f;
    private int studentCount = 30;

    private final CollisionHandler collisionHandler;
    private final ArrayList<ColliderBody> colliders;

    private final ArrayList<Student> students = new ArrayList<Student>();

    /**
     *Initialises StudentManager class.
     * @param texture represents the appearance of each student.
     * @param collisionHandler manages all collidable
     *                         objects on the board.
     */
    public StudentManager(TextureRegion texture,
                          CollisionHandler collisionHandler) {

        studentTexture = texture;
        this.collisionHandler = collisionHandler;
        colliders = collisionHandler.getColliders();

    }

    /**
     *Generates a new student every frame until there are 30
     * total.
     */
    public void generateStudents(float delta){
        spawnTimer -= delta;

        if (spawnTimer <= 0f && studentCount >= 0) {
            Student student = new Student(spawnX,spawnY,studentTexture,
                collisionHandler);
            students.add(student);
            spawnTimer = 1f;
            studentCount -= 1;
        }
    }

    public void update(float delta) {
        /**
         *Updates position of student on the map.
         */

        for (Student student : students) {
            student.move(delta);
        }

        remove();
    }
    /**
     * Removes the student from the game once they reach designated
     * coordinates.
     */
    public void remove(){
        int s = students.size() - 1;
        while (s >= 0) {
            Student student = students.get(s);
            if (student.getState() == StudentState.TargetFound) {
                colliders.remove(student.getBody());
                students.remove(student);
            }
            s -= 1;
        }
    }

    /**
     *Creates 2d-graphic of each student.
     */
    public void render(SpriteBatch batch) {
        for (Student student : students) {
            student.render(batch);
        }
    }

}
