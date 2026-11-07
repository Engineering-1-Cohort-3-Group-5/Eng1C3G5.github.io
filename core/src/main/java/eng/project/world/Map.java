package eng.project.world;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.Box;
import eng.project.collisions.CollisionHandler;

import java.util.ArrayList;

/** Renders the map background and declares the current static world features. */
public class Map {
    private final Texture mapTexture;
    private final Texture borderTexture;
    private static final float width = 120f;
    private static final float height = 100f;
    private final ArrayList<Feature> mapFeatures = new ArrayList<>();
    private final Bus bus;
    /** Builds the background and registers the hard-coded building colliders. */
    public Map(AssetManager assets, CollisionHandler collisionHandler){
        this.mapTexture = assets.get("world/map.png",Texture.class);
        this.borderTexture = assets.get("world/border.png",Texture.class);

        bus = new Bus(-22,-36,assets.get("world/bus.png",Texture.class),collisionHandler);

        // Generate map structures here

        // Buildings
        Texture buildingsTexture = assets.get("world/buildings.png",Texture.class);

        TextureRegion[] buildingTypes = {
            new TextureRegion(buildingsTexture, 0, 0, 144, 106),
            new TextureRegion(buildingsTexture, 155, 0, 61, 106),
            new TextureRegion(buildingsTexture, 228, 17, 100, 77),
            new TextureRegion(buildingsTexture, 76, 113, 116, 59),
            new TextureRegion(buildingsTexture, 203, 113, 144, 129),
            new TextureRegion(buildingsTexture, 1, 117, 69, 77)
        };

        Box[] buildingColliders = {
            new Box(11f, 5f), new Box(2.5f, 4.3f),
            new Box(9f, 7f), new Box(5.5f, 2.8f),
            new Box(11f, 10f), new Box(3f, 3.4f)
        };

        float[][] buildingPositions = {
            {5, 10},{0, 10},{-15,-22},{-37,-5},{30,-15},
            {35,10},
        };

        for (int i = 0; i < buildingPositions.length; i++) {
            int buildingType = i % buildingTypes.length;
            float[] position = buildingPositions[i];
            mapFeatures.add(new Feature(
                position[0], position[1], 1f, buildingColliders[buildingType],
                buildingTypes[buildingType], collisionHandler
            ));
        }

        // Bushes
        Texture bushesTexture = assets.get("world/bushes.png",Texture.class);

        TextureRegion[] bushTypes = {
            new TextureRegion(bushesTexture,0,0,118,118),
            new TextureRegion(bushesTexture,118,18,100,100),
            new TextureRegion(bushesTexture,225,0,118,118)
        };

        float[][] bushPositions = {
            {4f, 32f}, {17f, 40f}, {31f, 37f}, {47f, 34f},
            {-52f, 12f}, {-52f, -8f}, {-42f, -18f}, {-35f, -32f},
            {-16f, -40f}, {-10f, -34f}, {4f, -41f}, {19f, -37f},
            {28f, -28f}, {40f, -20f}, {56f, -8f}, {40f, 12f},
            {-55f, 43f}, {-43f, 44f}, {-31f, 45f}, {-18f, 44f},
            {-3f, 45f}, {12f, 44f}, {27f, 44f}, {43f, 43f},
        };

        for (int i = 0; i < bushPositions.length; i++) {
            float[] position = bushPositions[i];
            mapFeatures.add(new Feature(
                position[0], position[1], 3f, new Box(1f, 1f),
                bushTypes[i % bushTypes.length], collisionHandler
            ));
        }
    }

    public void update(){
        bus.update();
    }

    public void render_back(SpriteBatch batch){
        batch.draw(mapTexture,-width/2f, -height/2f, width, height);
    }

    public void render_front(SpriteBatch batch){
        bus.render(batch);
        for (Feature feature : mapFeatures){
            feature.render(batch);
        }
        float borderScale = 1.15f;
        batch.draw(borderTexture,-(width* borderScale)/2f, -(height* borderScale)/2f, width* borderScale, height* borderScale);
    }

    public static float getWidth(){
        return width;
    }

    public static float getHeight(){
        return height;
    }
}
