package eng.project.entity.students;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.food.FoodManager;

import java.util.ArrayList;

public class StudentManager {

    private ArrayList<Student> students = new ArrayList<Student>();

    private final TextureRegion studentTexture;

    /** Creates the initial flock and registers every goose collider. */
    public StudentManager(TextureRegion texture, CollisionHandler collisionHandler){
        studentTexture = texture;

        for (int i = 0; i < 30; i++){
            Student student = new Student(10-i,10-i,studentTexture,collisionHandler);
            students.add(student);
        }
    }
    public void update(float delta, float playerX, float playerY, FoodManager foodManager){
        for (Student student : students){
            student.target(playerX, playerY,delta,foodManager);
            student.move(delta);
        }
    }

    public void render(SpriteBatch batch){
        for (Student student : students){
            student.render(batch);
        }
    }

    public ArrayList<Student> getGeese(){
        return students;
    }

}

//spawn every 3 seconds for a period of 30 seconds
