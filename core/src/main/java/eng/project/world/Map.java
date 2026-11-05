package eng.project.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.Box;
import eng.project.collisions.CollisionHandler;

import java.util.ArrayList;

/** Renders the map background and declares the current static world features. */
public class Map {
    private final TextureRegion mapTexture;
    private static final float width = 40f;
    private static final float height = 40f;
    private final ArrayList<Feature> mapFeatures = new ArrayList<>();
    /** Builds the background and registers the hard-coded building colliders. */
    public Map(TextureRegion mapTexture,TextureRegion buildingTexture, CollisionHandler collisionHandler){
        this.mapTexture = mapTexture;

        // Generate map structures here
        mapFeatures.add(new Feature(-10,0,new Box(2f,3f),buildingTexture,collisionHandler));
        mapFeatures.add(new Feature(-2,4,new Box(4f,3f),buildingTexture,collisionHandler));
        mapFeatures.add(new Feature(-15,15,new Box(4f,4f),buildingTexture,collisionHandler));
        mapFeatures.add(new Feature(14,-15,new Box(4f,1.5f),buildingTexture,collisionHandler));
        mapFeatures.add(new Feature(11,11,new Box(4f,1.5f),buildingTexture,collisionHandler));
        mapFeatures.add(new Feature(16,1,new Box(3f,3f),buildingTexture,collisionHandler));
        mapFeatures.add(new Feature(-15,-12,new Box(3f,5f),buildingTexture,collisionHandler));
    }

    public void render(SpriteBatch batch){
        // Centred around 0,0
        batch.draw(mapTexture,-width/2f, -height/2f, width, height);

        for (Feature feature : mapFeatures){
            feature.render(batch);
        }
    }

    public static float getWidth(){
        return width;
    }

    public static float getHeight(){
        return height;
    }
}
