package eng.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import eng.project.game.GameController;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private GameController gameController;
    private AssetManager assets;

    @Override
    public void create() {
        // Runs at the start of the game, used to initialise everything.
        assets = new AssetManager();

        // Load textures from the assets folder
        assets.load("player.png", Texture.class);
        assets.load("map.png", Texture.class);
        assets.load("goose.png", Texture.class);
        assets.load("food.png", Texture.class);
        assets.finishLoading();

        gameController = new GameController(assets);
    }

    @Override
    public void render() {
        // Runs every frame, used to update and then render everything
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        gameController.update();
        gameController.render();
    }

    @Override
    public void dispose() {
        // All libgdx graphics resources must be disposed to avoid memory leaks
        gameController.dispose();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
        gameController.getCamera().resize(width,height);
    }
}
