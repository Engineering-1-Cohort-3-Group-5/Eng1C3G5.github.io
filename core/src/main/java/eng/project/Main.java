package eng.project;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import eng.project.game.GameScreen;
import eng.project.ui.DefeatScreen;
import eng.project.ui.MainMenuScreen;
import eng.project.ui.VictoryScreen;

/** Application entry point shared by all platforms and owner of screen navigation. */
public class Main extends com.badlogic.gdx.Game {
    private AssetManager assets;

    @Override
    public void create() {
        assets = new AssetManager();

        // Load textures shared by game screens.
        assets.load("entity/player.png", Texture.class);
        assets.load("entity/goose.png", Texture.class);
        assets.load("entity/food.png", Texture.class);
        assets.load("entity/student.png", Texture.class);
        assets.load("entity/keycard.png", Texture.class);
        assets.load("entity/fan.png", Texture.class);
        assets.load("entity/whistle.png", Texture.class);

        assets.load("world/map.png", Texture.class);
        assets.load("world/bushes.png", Texture.class);
        assets.load("world/buildings.png", Texture.class);
        assets.load("world/border.png", Texture.class);
        assets.load("world/bus.png", Texture.class);

        assets.load("mushroom.png", Texture.class);
        assets.load("wind.png", Texture.class);
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
    public void showVictoryScreen() {
        changeScreen(new VictoryScreen(this));
    }
    public void showDefeatScreen() {
        changeScreen(new DefeatScreen(this));
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
