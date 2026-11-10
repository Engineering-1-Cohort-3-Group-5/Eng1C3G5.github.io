package eng.project.entity.students;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.ColliderBody;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.geese.Goose;
import eng.project.world.Feature;
import eng.project.world.Map;

import java.util.ArrayList;

public class StudentManager {

    private final TextureRegion studentTexture;

    private final Feature building = Map.getOneMapFeature();
    private final float spawnX =
        building.getX() + building.getHalfWidth();
    private final float spawnY =
        building.getY() + building.getHalfHeight();
    private final CollisionHandler collisionHandler;
    private float spawnTimer = 2f;
    private int studentCount = 10;
    private ArrayList<ColliderBody> colliders;

    private float delta = Gdx.graphics.getDeltaTime();



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

    public void generateStudents(){
        spawnTimer -= delta;

        if (spawnTimer <= 0f && studentCount > 0) {
            Student student = new Student(spawnX,spawnY,studentTexture,
                collisionHandler);
            students.add(student);
            spawnTimer = 2f;
            studentCount -= 1;
        }
    }

    public void update(float delta) {

        generateStudents();

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

    public ArrayList<Student> getStudents() {
        return students;
    }

}
