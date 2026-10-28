package eng.project.entity.geese;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.food.FoodManager;

import java.util.ArrayList;

/** Owns the game's goose collection and updates/renders every goose. */
public class GeeseManager {
    private ArrayList<Goose> geese = new ArrayList<>();

    private final TextureRegion gooseTexture;

    /** Creates the initial flock and registers every goose collider. */
    public GeeseManager(TextureRegion texture, CollisionHandler collisionHandler){
        gooseTexture = texture;

        for (int i = 0; i < 30; i++){
            Goose goose = new Goose(10-i,10-i,gooseTexture,collisionHandler);
            geese.add(goose);
        }
    }
    public void update(float delta, float playerX, float playerY, FoodManager foodManager){
        for (Goose goose : geese){
            goose.target(playerX, playerY,delta,foodManager);
            goose.move(delta);
        }
    }

    public void render(SpriteBatch batch){
        for (Goose goose : geese){
            goose.render(batch);
        }
    }

    public ArrayList<Goose> getGeese(){
        return geese;
    }
}
