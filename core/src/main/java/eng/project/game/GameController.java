package eng.project.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.GeeseManager;
import eng.project.entity.Player;
import eng.project.world.Map;

import static com.badlogic.gdx.Gdx.graphics;

public class GameController {
    private final CameraController camera;
    private final SpriteBatch batch;
    private final Player player;
    private final Map map;
    private final GeeseManager geeseManager;

    public GameController(AssetManager assets){
        // Init objects here

        // TextureRegion is used so that sprite atlases can later be used
        player = new Player(new TextureRegion(assets.get("player.png", Texture.class)));
        map = new Map(new TextureRegion(assets.get("map.png", Texture.class)));
        geeseManager = new GeeseManager(new TextureRegion(assets.get("goose.png", Texture.class)));

        camera = new CameraController();
        batch = new SpriteBatch();
    }

    public void update(){
        // Use delta for all movements or animations to synchronise across frame rates
        float delta = graphics.getDeltaTime();

        // Update objects here
        player.update(delta);
        geeseManager.update(delta);
        CollisionHandler.resolvePlayerGeeseCollisions(geeseManager.getGeese(),player);
        camera.update(player.getX(), player.getY());
    }

    public void render(){
        batch.setProjectionMatrix(camera.getMatrix());
        batch.begin();

        // Draw objects here, objects drawn last will be in the foreground
        map.render(batch);
        geeseManager.render(batch);
        player.render(batch);

        batch.end();
    }

    public void dispose(){
        // Dispose any libgdx render resources here. textures are already disposed by asset manager
        batch.dispose();
    }
}
