package eng.project.entity.geese;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.entity.thrown.ThrownItemManager;

import java.util.ArrayList;

public class GeeseManager {
    private ArrayList<Goose> geese = new ArrayList<>();

    private final TextureRegion gooseTexture;

    public GeeseManager(TextureRegion texture){
        gooseTexture = texture;

        for (int i = 0; i < 10; i++){
            Goose goose = new Goose(5-i,i,gooseTexture);
            geese.add(goose);
        }
    }
    public void update(float delta, float playerX, float playerY, ThrownItemManager thrownItemManager){
        for (Goose goose : geese){
            goose.target(playerX, playerY,delta,thrownItemManager);
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
