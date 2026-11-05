package eng.project.entity.students;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import eng.project.collisions.CollisionHandler;
import eng.project.world.Feature;
import eng.project.world.Map;

import java.util.ArrayList;

public class StudentManager {

    private final TextureRegion studentTexture;
    private final Feature building = Map.getOneMapFeature();
    private final float x = building.getX() + building.getHalfWidth();
    private final float y = building.getY() + building.getHalfHeight();


    private final ArrayList<Student> students = new ArrayList<Student>();

    /**
     * Creates the initial flock and registers every goose collider.
     */
    public StudentManager(TextureRegion texture,
                          CollisionHandler collisionHandler) {

        studentTexture = texture;
        int spawnTimer = 30;

        while (spawnTimer > 0) {
            Student student = new Student(x, y, studentTexture,
                collisionHandler);
            students.add(student);
            spawnTimer -= 3;
        }
    }

    public void target(){
        //TODO: generate coordinates on border for students to aim for
        float targetX;
        float targetY;

    }

    public void update(float delta) {
        for (Student student : students) {
            student.move(delta);
        }
    }
    /**
     * Removes the student from the game once it reaches designated
     * coordinates.
     *
     */
    public void remove(){
        //TODO
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
