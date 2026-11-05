package eng.project;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import eng.project.game.GameScreen;
import eng.project.ui.MainMenuScreen;

/** Application entry point shared by all platforms and owner of screen navigation. */
public class Main extends com.badlogic.gdx.Game {
    private AssetManager assets;

    @Override
    public void create() {
        assets = new AssetManager();

        // Load textures shared by game screens.
        assets.load("player.png", Texture.class);
        assets.load("map.png", Texture.class);
        assets.load("goose.png", Texture.class);
        assets.load("food.png", Texture.class);
        assets.load("building.png", Texture.class);
        assets.load("student.png", Texture.class);
        assets.finishLoading();

        showMainMenu();
    }

    /** Opens a fresh playable game screen. */
    public void startGame() {
        changeScreen(new GameScreen(this, assets));
    }

    /** Returns to the main menu. */
    public void showMainMenu() {
        changeScreen(new MainMenuScreen(this));
    }

    /** Replaces and disposes the current screen, which prevents abandoned game screens leaking resources. */
    private void changeScreen(Screen nextScreen) {
        Screen previousScreen = getScreen();
        setScreen(nextScreen);

        if (previousScreen != null) {
            previousScreen.dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
    }
}
