
package eng.project.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/** Draws the game's retro-style timer. */
public class Gui {

    private final ShapeRenderer shapes = new ShapeRenderer();
    private final BitmapFont font = new BitmapFont();
    private final Matrix4 uiMatrix = new Matrix4();

    private static final float GAME_TIME = 300f;

    private final Color cream = new Color(1f, 0.98f, 0.94f, 1f);
    private final Color coral = new Color(1f, 0.39f, 0.39f, 1f);
    private final Color black = Color.BLACK;

    /** Draws the timer in the top-left corner. */
    public void render(SpriteBatch batch, float timeRemaining) {

        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();

        uiMatrix.setToOrtho2D(0, 0, width, height);
        shapes.setProjectionMatrix(uiMatrix);

        float x = 15;
        float y = height - 110;

        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Small shadow behind the timer.
        shapes.setColor(black);
        shapes.rect(x + 5, y - 5, 205, 95);

        // Black border and cream background.
        shapes.rect(x, y, 205, 95);
        shapes.setColor(cream);
        shapes.rect(x + 4, y + 4, 197, 87);

        // Red label decoration.
        shapes.setColor(coral);
        shapes.rect(x + 140, y + 70, 50, 13);

        // Timer progress bar.
        float progress = Math.max(0f, timeRemaining / GAME_TIME);

        shapes.setColor(black);
        shapes.rect(x + 15, y + 12, 175, 12);

        shapes.setColor(coral);
        shapes.rect(x + 18, y + 15, 169 * progress, 6);

        // Draw sharp pixel numbers.
        int totalSeconds = (int) Math.ceil(timeRemaining);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        String time = String.format("%02d:%02d", minutes, seconds);

        drawPixelTime(time, x + 49, y + 35);

        shapes.end();

        // Small label using the default font at native size.
        batch.setProjectionMatrix(uiMatrix);
        batch.begin();
        font.setColor(black);
        font.draw(batch, "TIME LEFT", x + 15, y + 82);
        batch.end();
    }

    /** Draws the countdown using square pixels. */
    private void drawPixelTime(String time, float x, float y) {

        float pixelSize = 4f;

        for (int i = 0; i < time.length(); i++) {
            String pattern = getDigitPattern(time.charAt(i));

            for (int j = 0; j < pattern.length(); j++) {
                if (pattern.charAt(j) == '1') {
                    float px = x + i * 22 + (j % 3) * pixelSize;
                    float py = y + (4 - j / 3) * pixelSize;

                    shapes.setColor(black);
                    shapes.rect(px, py, pixelSize, pixelSize);
                }
            }
        }
    }

    /** Returns a small pixel pattern for each number. */
    private String getDigitPattern(char digit) {
        return switch (digit) {
            case '0' -> "111101101101111";
            case '1' -> "010110010010111";
            case '2' -> "111001111100111";
            case '3' -> "111001111001111";
            case '4' -> "101101111001001";
            case '5' -> "111100111001111";
            case '6' -> "111100111101111";
            case '7' -> "111001001001001";
            case '8' -> "111101111101111";
            case '9' -> "111101111001111";
            case ':' -> "000010000010000";
            default -> "000000000000000";
        };
    }

    /** Releases the interface resources. */
    public void dispose() {
        shapes.dispose();
        font.dispose();
    }
}
