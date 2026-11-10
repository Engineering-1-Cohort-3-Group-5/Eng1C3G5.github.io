package eng.project.entity.geese;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.food.FoodManager;

import java.util.ArrayList;

/** Owns the game's goose collection and updates/renders every goose. */
public class GeeseManager {
    private ArrayList<Goose> geese = new ArrayList<>();
    private final TextureRegion gooseTexture;
    private float spawnTimer = 1;
    private final float spawnTime = 1f;

    /** Creates the initial flock and registers every goose collider. */
    public GeeseManager(TextureRegion texture){
        gooseTexture = texture;
    }
    public void update(float delta, float playerX, float playerY, FoodManager foodManager, CollisionHandler collisionHandler){
        if (spawnTimer <= 0){
            spawnTimer = spawnTime;

            int offsetX;
            int offsetY;
            if (MathUtils.random(0,1)==0){
                offsetX = MathUtils.random(-15,15);
                if (MathUtils.random(0,1)==0){
                    offsetY =  MathUtils.random(14,16);
                }
                else{
                    offsetY = -MathUtils.random(14,16);
                }
            }
            else{
                offsetY = MathUtils.random(-15,15);
                if (MathUtils.random(0,1)==0){
                    offsetX = MathUtils.random(14,16);
                }
                else{
                    offsetX = -MathUtils.random(14,16);
                }
            }

            Goose newGoose = new Goose(playerX + offsetX,playerY + offsetY,gooseTexture,collisionHandler);
            geese.add(newGoose);
        }
        else{
            spawnTimer -= delta;
        }

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
}
