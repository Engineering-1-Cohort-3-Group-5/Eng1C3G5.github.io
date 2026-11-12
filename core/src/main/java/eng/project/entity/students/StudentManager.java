package eng.project.entity.students;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.world.Feature;
import eng.project.world.Map;

import java.util.ArrayList;

public class StudentManager {

    private final TextureRegion studentTexture;

    private final Feature buildingDimensions = Map.getB4Dimensions();

    private final float spawnX =
        building.getX() + bu;
    private final float spawnY =
        building.getY() + building.getHalfHeight();

    private final CollisionHandler collisionHandler;
    private float spawnTimer = 1.5f;
    private int studentCount = 5;
    private final ArrayList<ColliderBody> colliders;

    private ArrayList<Student> students = new ArrayList<Student>();

    /**
     * Creates the initial flock and registers every goose collider.
     */
    public StudentManager(TextureRegion texture,
                          CollisionHandler collisionHandler) {

        studentTexture = texture;
        this.collisionHandler = collisionHandler;
        colliders = collisionHandler.getColliders();

    }

    public void generateStudents(float delta){
        spawnTimer -= delta;

        if (spawnTimer <= 0f && studentCount >= 0) {
            Student student = new Student(spawnX,spawnY,studentTexture,
                collisionHandler);
            students.add(student);
            spawnTimer = 1.5f;
            studentCount -= 1;
        }
    }

    public void update(float delta) {

        for (Student student : students) {
            student.move(delta);
        }

        remove();
    }
    /**
     * Removes the student from the game once it reaches designated
     * coordinates.
     *
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

    public void render(SpriteBatch batch) {
        for (Student student : students) {
            student.render(batch);
        }
    }

}
