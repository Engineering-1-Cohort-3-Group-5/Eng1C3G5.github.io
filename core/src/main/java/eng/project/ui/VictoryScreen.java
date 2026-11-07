package eng.project.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import eng.project.Main;

public class VictoryScreen extends ScreenAdapter {
    private final Main application;

    public VictoryScreen(Main application) {
        this.application = application;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.5f, 1f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            application.showMainMenu();
        }
    }
}
