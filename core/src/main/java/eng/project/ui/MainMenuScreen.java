package eng.project.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import eng.project.Main;

/**
 * Placeholder for the game's main menu. Press P to start a game.
 */
public class MainMenuScreen extends ScreenAdapter {
    private final Main application;

    public MainMenuScreen(Main application) {
        this.application = application;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            application.startGame();
        }
    }
}
