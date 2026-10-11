package eng.project.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.geese.GeeseManager;
import eng.project.entity.Player;
import eng.project.entity.thrown.ThrownItemManager;
import eng.project.inventory.Inventory;
import eng.project.ui.Gui;
import eng.project.world.Map;

import static com.badlogic.gdx.Gdx.graphics;

public class GameController {
    private final CameraController camera = new CameraController();
    private final SpriteBatch batch = new SpriteBatch();
    private final Player player;
    private final Map map;
    private final GeeseManager geeseManager;
    private final ThrownItemManager thrownItemManager;
    private final Gui gui = new Gui();
    private final Inventory inventory = new Inventory();

    public GameController(AssetManager assets){
        // TextureRegion is used so that sprite atlases can later be used
        player = new Player(new TextureRegion(assets.get("player.png", Texture.class)));
        map = new Map(new TextureRegion(assets.get("map.png", Texture.class)));
        geeseManager = new GeeseManager(new TextureRegion(assets.get("goose.png", Texture.class)));
        thrownItemManager = new ThrownItemManager(new TextureRegion(assets.get("food.png", Texture.class)));
    }

    public void update(){
        // Use delta for all movements or animations to synchronise across frame rates
        float delta = graphics.getDeltaTime();

        // Update objects here
        thrownItemManager.update(delta,player.getX(), player.getY());
        player.update(delta);
        geeseManager.update(delta,player.getX(), player.getY(),thrownItemManager);
        CollisionHandler.resolvePlayerGeeseCollisions(geeseManager.getGeese(),player);
        camera.update(player.getX(), player.getY());
    }

    public void render(){
        batch.setProjectionMatrix(camera.getMatrix());
        batch.begin();

        // Draw objects here, objects drawn last will be in the foreground
        map.render(batch);
        thrownItemManager.render(batch);
        geeseManager.render(batch);
        player.render(batch);
        gui.render(batch);

        batch.end();
    }

    public void dispose(){
        // Dispose any libgdx render resources here. textures are already disposed by asset manager
        batch.dispose();
    }

    public CameraController getCamera(){
        return camera;
    }
}
