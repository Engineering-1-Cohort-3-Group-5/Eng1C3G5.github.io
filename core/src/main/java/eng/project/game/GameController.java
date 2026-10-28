package eng.project.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eng.project.collisions.CollisionHandler;
import eng.project.entity.geese.GeeseManager;
import eng.project.entity.Player;
import eng.project.entity.food.FoodManager;
import eng.project.inventory.Inventory;
import eng.project.ui.Gui;
import eng.project.world.Map;

import static com.badlogic.gdx.Gdx.graphics;

/**
 * Coordinates the game loop. Keep cross-system update order here: input and AI move
 * bodies first, collisions correct positions next, then the camera follows the result.
 */
public class GameController {
    private final CameraController camera = new CameraController();
    private final SpriteBatch batch = new SpriteBatch();
    private final Player player;
    private final Map map;
    private final GeeseManager geeseManager;
    private final FoodManager foodManager;
    private final Gui gui = new Gui();
    private final Inventory inventory = new Inventory();
    private final CollisionHandler collisionHandler = new CollisionHandler();
    private final GameState state = GameState.Play;

    /** Builds world systems from textures already loaded by {@link eng.project.Main}. */
    public GameController(AssetManager assets){
        // TextureRegion is used so that sprite atlases can later be used
        player = new Player(new TextureRegion(assets.get("player.png", Texture.class)),collisionHandler);
        geeseManager = new GeeseManager(new TextureRegion(assets.get("goose.png", Texture.class)),collisionHandler);
        map = new Map(new TextureRegion(assets.get("map.png", Texture.class)),new TextureRegion(assets.get("building.png", Texture.class)),collisionHandler);
        foodManager = new FoodManager(new TextureRegion(assets.get("food.png", Texture.class)));
    }

    /** Advances all simulation systems by one rendered frame. */
    public void update(){
        // Add ui to change game state and adjust the update accordingly
        switch (state){
            case Menu -> {
                // Main menu ui update
            }
            case Play -> {
                // Use delta for all movements or animations to synchronise across frame rates
                float delta = graphics.getDeltaTime();

                // Update objects here
                foodManager.update(delta,player.getX(), player.getY());
                player.update(delta);
                geeseManager.update(delta,player.getX(), player.getY(), foodManager);
                collisionHandler.resolveCollisions();
                camera.update(player.getX(), player.getY());
            }
            case Pause -> {
                // Pause menu ui update
            }
        }
    }

    /** Draws the world back-to-front using the current camera. */
    public void render(){
        batch.setProjectionMatrix(camera.getMatrix());
        batch.begin();

        switch (state){
            case Menu -> {
               // Draw main menu ui
            }
            case Play,Pause -> {
                // Draw objects here, objects drawn last will be in the foreground
                map.render(batch);
                foodManager.render(batch);
                geeseManager.render(batch);
                player.render(batch);
                gui.render(batch); // Add pause ui when paused

                batch.end();
            }
        }
    }

    /** Releases rendering resources owned by this controller. */
    public void dispose(){
        // Dispose any libgdx render resources here. textures are already disposed by asset manager
        batch.dispose();
    }

    public CameraController getCamera(){
        return camera;
    }
}
