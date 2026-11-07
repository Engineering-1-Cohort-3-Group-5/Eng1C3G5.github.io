package eng.project.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import eng.project.Main;
import eng.project.collisions.CollisionHandler;
import eng.project.world.Keycard;
import eng.project.entity.Player;
import eng.project.entity.food.FoodManager;
import eng.project.entity.geese.GeeseManager;
import eng.project.inventory.Inventory;
import eng.project.ui.Gui;
import eng.project.world.Map;

/**
 * The playable game screen. It owns the world systems and keeps their update order
 * in one place: movement and AI, collision resolution, then camera tracking.
 */
public class GameScreen extends ScreenAdapter {
    private final Main application;
    private final CameraController camera = new CameraController();
    private final SpriteBatch batch = new SpriteBatch();
    private final Player player;
    private final Keycard keycard;
    private final Map map;
    private final GeeseManager geeseManager;
    private final FoodManager foodManager;
    private final Gui gui = new Gui();
    private final Inventory inventory = new Inventory();
    private final CollisionHandler collisionHandler = new CollisionHandler();
    private GameState state = GameState.Play;

    /** Builds world systems from textures already loaded by {@link eng.project.Main}. */
    public GameScreen(Main application, AssetManager assets) {
        this.application = application;

        geeseManager = new GeeseManager(new TextureRegion(assets.get("entity/goose.png", Texture.class)));
        map = new Map(assets, collisionHandler);
        keycard = new Keycard(new TextureRegion(assets.get("entity/keycard.png", Texture.class)), collisionHandler);
        player = new Player(new TextureRegion(assets.get("entity/player.png", Texture.class)), collisionHandler);
        foodManager = new FoodManager(new TextureRegion(assets.get("entity/food.png", Texture.class)));
    }

    /** Updates and draws this screen once per frame. */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.5f, 0.3f, 1f);
        if (handleInput()) {
            return;
        }
        update(delta);
        renderWorld();
    }

    /** Handles screen-level controls and returns true when it changes screens. */
    private boolean handleInput() {
        if (state == GameState.Pause && Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            application.showMainMenu();
            return true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (state == GameState.Pause) {
                resumeGame();
            } else {
                pauseGame();
            }
        }

        return false;
    }

    /** Advances the simulation only while the game is being played. */
    private void update(float delta) {
        switch (state) {
            case Play -> {
                foodManager.update(delta, player.getX(), player.getY());
                geeseManager.update(delta, player.getX(), player.getY(), foodManager, collisionHandler);
                player.update(delta);
                keycard.update();
                map.update(application);
                collisionHandler.resolveCollisions();
                camera.update(player.getX(), player.getY());
            }
            case Pause -> {
                // Pause-menu input will be handled here when it is implemented.
            }
        }
    }

    /** Draws the world back-to-front using the current camera. */
    private void renderWorld() {
        batch.setProjectionMatrix(camera.getMatrix());
        batch.begin();

        // A paused game still shows the current world behind its future pause UI.
        map.render_back(batch);
        keycard.render(batch);
        foodManager.render(batch);
        geeseManager.render(batch);
        player.render(batch);
        map.render_front(batch);
        gui.render(batch);

        batch.end();
    }

    /** Stops simulation updates while keeping the world visible. */
    public void pauseGame() {
        state = GameState.Pause;
    }

    /** Resumes simulation updates after {@link #pauseGame()}. */
    public void resumeGame() {
        state = GameState.Play;
    }

    /** Returns whether the game simulation is currently paused. */
    public boolean isPaused() {
        return state == GameState.Pause;
    }

    @Override
    public void resize(int width, int height) {
        camera.resize(width, height);
    }

    /** Releases rendering resources owned by this screen. */
    @Override
    public void dispose() {
        // Textures are disposed by AssetManager in Main.
        batch.dispose();
    }
}
