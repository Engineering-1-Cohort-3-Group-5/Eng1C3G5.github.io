package eng.project.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {
    private final TextureRegion mapTexture;
    private final float width = 40f;
    private final float height = 40f;

    public Map(TextureRegion texture){
        mapTexture = texture;
    }

    public void render(SpriteBatch batch){
        // Centred around 0,0
        batch.draw(mapTexture,-width/2f, -height/2f, width, height);
    }
}
