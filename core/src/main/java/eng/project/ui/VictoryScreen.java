package eng.project.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import eng.project.Main;

public class VictoryScreen extends ScreenAdapter {
    private final Main application;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;

    private Rectangle exitButton;

    public VictoryScreen(Main application) {
        this.application = application;
        shape = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        exitButton = new Rectangle(centerX - 100, centerY - 80, 200, 60);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Draw buttons
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(Color.GREEN);

        shape.setColor(Color.RED);
        shape.rect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);

        shape.end();

        // Draw button text
        GlyphLayout layout = new GlyphLayout();
        batch.begin();

        String title = "You escaped!";
        layout.setText(font, title);

        font.getData().setScale(3f);

        font.draw(batch, title,
            (Gdx.graphics.getWidth() - layout.width * 3f) / 2f,
            exitButton.y + exitButton.height + 120);

        font.getData().setScale(1f);

        layout.setText(font, "EXIT");
        font.draw(batch, "EXIT",
            exitButton.x + (exitButton.width - layout.width) / 2,
            exitButton.y + (exitButton.height + layout.height) / 2);

        batch.end();

        // Handle clicks
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (exitButton.contains(x, y)) {
                application.showMainMenu();
            }
        }
    }

    @Override
    public void dispose() {
        shape.dispose();
        font.dispose();
        batch.dispose();
    }
}
