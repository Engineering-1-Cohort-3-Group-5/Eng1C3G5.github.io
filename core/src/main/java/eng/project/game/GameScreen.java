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
import eng.project.entity.Bus;
import eng.project.entity.Keycard;
import eng.project.entity.Player;
import eng.project.entity.food.FoodManager;
import eng.project.entity.geese.GeeseManager;
import eng.project.entity.students.StudentManager;
import eng.project.entity.mushrooms.MushroomManager;
import eng.project.inventory.Inventory;
import eng.project.inventory.Fan;
import eng.project.inventory.Whistle;
import eng.project.ui.Gui;
import eng.project.world.Map;
import com.badlogic.gdx.InputAdapter;
import eng.project.ui.TimerGui;
import eng.project.ui.Pause;
import eng.project.ui.Play;
import eng.project.inventory.Seeds;

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
    private final StudentManager studentManager;
    private final FoodManager foodManager;
    private final Seeds seeds;
    private final Fan fan;
    private final Whistle whistle;
    private final MushroomManager mushroomManager;
    private final Gui gui;
    private final TimerGui timerGui = new TimerGui();
    private final Inventory inventory = new Inventory();
    private final CollisionHandler collisionHandler = new CollisionHandler();
    public static GameState state = GameState.Play;
    private Pause pauser = new Pause();
    private Play playRunnable = new Play();
    private float timeRemaining = 300f;
    private final Bus bus;

    /** Builds world systems from textures already loaded by {@link eng.project.Main}. */
    public GameScreen(Main application, AssetManager assets) {
        this.application = application;
        // TextureRegion is used so that sprite atlases can later be used.
        gui = new Gui(pauser, playRunnable);

        geeseManager = new GeeseManager(new TextureRegion(assets.get("entity/goose.png", Texture.class)));
        map = new Map(assets, collisionHandler);
        bus = new Bus(-22,-36,assets.get("world/bus.png",Texture.class),collisionHandler);

        keycard = new Keycard(new TextureRegion(assets.get("entity/keycard.png", Texture.class)), collisionHandler);
        player = new Player(new TextureRegion(assets.get("entity/player.png", Texture.class)), collisionHandler);
        foodManager = new FoodManager(new TextureRegion(assets.get("entity/food.png", Texture.class)));

        mushroomManager = new MushroomManager(
            new TextureRegion(assets.get("mushroom.png", Texture.class)),
            map
        );

        seeds = new Seeds(foodManager, player, "Seeds", new TextureRegion(assets.get("entity/food.png", Texture.class)));
        fan = new Fan(collisionHandler, player, camera, "Fan", new TextureRegion(assets.get("entity/fan.png", Texture.class
        )), new TextureRegion(assets.get("wind.png", Texture.class)));
        whistle = new Whistle(player, geeseManager, "Whistle", new TextureRegion(assets.get("entity/whistle.png", Texture.class)));
        for (int i = 0; i < 10; i++) {
            inventory.add(seeds);
        }
        inventory.add(fan);
        inventory.add(whistle);

        studentManager =
            new StudentManager(new TextureRegion(assets.get(
                "entity/student" +
                    ".png", Texture.class)), collisionHandler);

        state = GameState.Play;
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                inventory.scroll((int) Math.signum(amountY));
                return true;
            }
        });
    }

    /** Updates and draws this screen once per frame. */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.5f, 0.3f, 1f);
        if (handleInput()) {
            return;
        }
        if (update(delta)) {
            return;
        }
        renderWorld();
    }

    /** Handles screen-level controls and returns true when it changes screens. */
    private boolean handleInput() {
        if (state == GameState.Pause && Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            application.showMainMenu();
            return true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (GameScreen.state == GameState.Pause){
                playRunnable.run();
            }
            else{
                pauser.run();
            }
        }

        return false;
    }

    /** Advances the simulation only while the game is being played. */
    private boolean update(float delta) {
        switch (state) {
            case Play -> {
                handleInventoryInput();
                // Count down while the game is running.
                timeRemaining = Math.max(0f, timeRemaining - delta);

                // Game over
                if (timeRemaining <= 0 || player.isDead()){
                    application.showDefeatScreen();
                    return true;
                }

                //Starts student hidden event when three minutes
                // remain.
                if (timeRemaining <= 180f) {
                    studentManager.generateStudents(delta);
                }
                foodManager.update(delta);
                geeseManager.update(delta, player.getX(), player.getY(), foodManager, collisionHandler);

                player.update(delta);
                keycard.update(inventory);
                if (bus.update(application)){
                    return true;
                }

                fan.update(delta);
                studentManager.update(delta);

                collisionHandler.resolveCollisions();
                mushroomManager.update(delta, player);
                camera.update(
                    player.getX(),
                    player.getY(),
                    mushroomManager.isDizzy()
                );
            }
            case Pause -> {

            }
        }
        return false;
    }

    public void handleInventoryInput() {
        for (int i = 0; i < Inventory.size; i++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i)) {
                inventory.select(i);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            inventory.useSelected();
        }
    }

    /** Draws the world back-to-front using the current camera. */
    private void renderWorld() {
        batch.setProjectionMatrix(camera.getMatrix());
        batch.begin();

        // A paused game still shows the current world behind its future pause UI.
        map.render_back(batch);
        bus.render(batch);
        keycard.render(batch);
        foodManager.render(batch);
        mushroomManager.render(batch);
        geeseManager.render(batch);
        studentManager.render(batch);
        player.render(batch);
        fan.render(batch);
        map.render_front(batch);

        batch.end();

        timerGui.render(batch, timeRemaining);
        gui.render(batch, inventory, player, pauser, playRunnable);
        gui.tutorialScreen();
    }


    /** Stops simulation updates while keeping the world visible. */
    public static void pauseGame() {
        state = GameState.Pause;
    }

    /** Resumes simulation updates after {link #pauseGame()}. */
    public static void resumeGame() {
        state = GameState.Play;
    }

    @Override
    public void resize(int width, int height) {
        camera.resize(width, height);
        gui.resize(width, height);
    }

    /** Releases rendering resources owned by this screen. */
    @Override
    public void dispose() {
        // Textures are disposed by AssetManager in Main.
        batch.dispose();
        gui.dispose();
        timerGui.dispose();
    }
}
