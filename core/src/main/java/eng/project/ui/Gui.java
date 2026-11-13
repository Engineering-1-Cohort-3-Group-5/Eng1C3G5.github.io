package eng.project.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eng.project.game.CameraController;
import eng.project.game.GameScreen;
import eng.project.game.GameState;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import eng.project.inventory.Inventory;
import eng.project.inventory.ItemStack;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import eng.project.entity.Player;
import com.badlogic.gdx.Gdx;

/** Entry point for screen-space UI; add HUD elements here as the game expands. */
public class Gui {
    private final Texture buttonTexture = new Texture("pause.png");
    private final SpriteBatch buttonBatch = new SpriteBatch();
    //public CameraController camera = new CameraController();
    private static final float slotSize = 64f;
    private static final float slotGap = 8f;
    private static final float border = 3f;
    private static final float bottomMargin = 20f;
    private static final float iconPadding = 8f;
    // Status bar layout in pixels
    private static final float barHeight = 18f;
    private static final float barGap = 6f;
    private static final float counterMargin = 20f;
    private static final float counterTop = 120f;
    private static final float pauseButtonSize = 50f;
    private static final float pauseButtonMargin = 25f;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final ScreenViewport viewport = new ScreenViewport(camera);

    private final Texture pixel; // 1x1 white texture tinted to draw rectangles
    private final BitmapFont font = new BitmapFont(); // built-in font feature
    private final GlyphLayout layout = new GlyphLayout(); // measures text so it cant be centered correctly
    // Item name popup
    private static final float nameShowTime = 1.5f;
    private int lastSelectedIndex = 0;
    private float nameTimer = 0f;
    // Health bar damage effects
    private static final float flashTime = 0.25f;
    private static final float trailSpeed = 40f;
    private float lastHealth = -1f;
    private float trailHealth = -1f;
    private float flashTimer = 0f;

    public Gui(){
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();
    }
    public void render(SpriteBatch batch, Inventory inventory, Player player, Pause pauseRunner, Play playRunner){
        float delta = Gdx.graphics.getDeltaTime();
        updateEffects(delta, inventory, player);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderStatusBars(batch, player);
        renderHotbar(batch, inventory);
        renderItemName(batch, inventory);
        renderItemCounter(batch, inventory);
        // Draw ui elements here
        //buttonBatch.setProjectionMatrix(camera.getMatrix());
        renderPauseButton(batch, pauseRunner, playRunner);
        batch.end();
    }
    private final Stage stage = new Stage();

    /** Updates the timers for the item name popup, damage and health trails */
    private void updateEffects(float delta, Inventory inventory, Player player) {
        if (inventory.getSelectedIndex() != lastSelectedIndex) {
            lastSelectedIndex = inventory.getSelectedIndex();
            nameTimer = nameShowTime;
        }
        if (nameTimer > 0f) {
            nameTimer -= delta;
        }

        float health = player.getHealth();
        if (lastHealth < 0f) {
            lastHealth = health;
            trailHealth = health;
        }

        if (health < lastHealth) {
            flashTimer = flashTime;
        }
        lastHealth = health;
        if (flashTimer > 0f) {
            flashTimer -= delta;
        }

        if (trailHealth > health) {
            trailHealth -= trailSpeed * delta;
            if ( trailHealth < health) {
                trailHealth = health;
            }
        } else {
            trailHealth = health;
        }

    }

    public void renderPauseButton(SpriteBatch batch, Pause pauseRunner, Play playRunner){
        float x = viewport.getWorldWidth() - pauseButtonSize - pauseButtonMargin;
        float y = viewport.getWorldHeight() - pauseButtonSize - pauseButtonMargin;
        batch.draw(buttonTexture, x - border, y - border, pauseButtonSize + 2 * border, pauseButtonSize + 2 * border);
        pauseButton(pauseRunner, playRunner);
    }
    /** Health and stamina bars stacked just above hotbar */
    private void renderStatusBars(SpriteBatch batch, Player player) {
        float width = Inventory.size * slotSize + (Inventory.size - 1) * slotGap;
        float x = (viewport.getWorldWidth() - width) / 2f;

        float staminaY = bottomMargin + slotSize + border + barGap + border;
        float healthY = staminaY + barHeight + barGap;

        float healthFraction = player.getHealth() / player.getMaxStamina();
        drawBar(batch, x, healthY, width, player.getHealth(), trailHealth, player.getMaxHealth(), getHealthColour(healthFraction));
        Color staminaColour = Color.SKY;
        if (player.isExhausted()) {
            staminaColour = Color.GRAY;
        }
        drawBar(batch, x, staminaY, width, player.getStamina(), player.getStamina(), player.getMaxStamina(), staminaColour);
    }

    /** Green when healthy, orange when hurt, red when low, and white briefly when damaged */
    private Color getHealthColour(float fraction) {
        if (flashTimer > 0f) {
            return Color.WHITE;
        }
        if (fraction > 0.6f) {
            return Color.GREEN;
        }
        if (fraction > 0.3f) {
            return Color.ORANGE;
        }
        return Color.RED;
    }

    /** Draws a bar filled to value/max, with a trail showing recently lost value and the number in the center */
    private void drawBar(SpriteBatch batch, float x, float y, float width, float value, float trailValue, float max, Color fillColour) {
        float fraction = Math.max(0f, Math.min(1f, value / max));
        float trailFraction = Math.max(0f, Math.min(1f, trailValue / max));

        batch.setColor(0.1f, 0.1f, 0.1f, 1f);
        batch.draw(pixel, x - border, y - border, width + 2 * border, barHeight + 2 * border);

        batch.setColor(Color.LIGHT_GRAY);
        batch.draw(pixel, x, y, width * trailFraction, barHeight);

        batch.setColor(fillColour);
        batch.draw(pixel, x, y, width * fraction, barHeight);
        batch.setColor(Color.WHITE);

        String text = Math.round(value) + " / " + Math.round(max);
        layout.setText(font, text);
        float textX = x + (width - layout.width) / 2f;
        float textY = y + (barHeight + layout.height) / 2f;
        font.draw(batch, text, textX, textY);
    }
    /** Hotbar slots centered at the bottom of the screen with slot numbers, item icons and stack counts*/
    private void renderHotbar(SpriteBatch batch, Inventory inventory) {
        float totalWidth = Inventory.size * slotSize + (Inventory.size - 1) * slotGap;
        float startX = (viewport.getWorldWidth() - totalWidth) / 2f;
        float y = bottomMargin;

        for (int i = 0; i < Inventory.size; i++) {
            float x = startX + i * (slotSize + slotGap);
            boolean selected = i == inventory.getSelectedIndex();

            if (selected) {
                batch.setColor(Color.GOLD);
            } else {
                batch.setColor(Color.DARK_GRAY);
            }
            batch.draw(pixel, x - border, y - border, slotSize + 2 * border, slotSize + 2 * border);

            batch.setColor(0.12f, 0.12f, 0.12f, 1f);
            batch.draw(pixel, x, y, slotSize, slotSize);
            batch.setColor(Color.WHITE);
            font.draw(batch, String.valueOf(i + 1), x + 4f, y + slotSize - 4f);

            ItemStack stack = inventory.get(i);
            if (stack == null) {
                continue;
            }

            if (stack.getItem().getIcon() != null) {
                batch.draw(stack.getItem().getIcon(), x + iconPadding, y + iconPadding, slotSize - 2 * iconPadding, slotSize - 2 * iconPadding);
            }

            if (stack.getCount() > 1) {
                font.draw(batch, String.valueOf(stack.getCount()), x + slotSize - 16f, y + 18f);
            }
        }
        batch.setColor(Color.WHITE);
    }

    /** Shows the selected items name at the bottom of the screen after switching slots and fades out over time */
    private void renderItemName(SpriteBatch batch, Inventory inventory) {
        ItemStack stack = inventory.getSelectedStack();
        if (nameTimer <= 0f || stack == null) {
            return;
        }

        float alpha = Math.min(1f, nameTimer / 0.5f);
        String name = stack.getItem().getName();
        layout.setText(font, name);

        float barsTop = bottomMargin + slotSize + border + 2 * (barGap + border + barHeight);
        float x = (viewport.getWorldWidth() - layout.width) / 2f;
        float y = barsTop + 8f + layout.height;

        font.setColor(1f, 1f, 1f, alpha);
        font.draw(batch, name, x ,y);
        font.setColor(Color.WHITE);
    }

    public void renderItemCounter(SpriteBatch batch, Inventory inventory) {
        String text = "Items used: " + inventory.getItemsUsed();
        layout.setText(font, text);

        float padding = 8f;
        float boxWidth = layout.width + 2 * padding;
        float boxHeight = layout.height + 2 * padding;
        float x = counterMargin;
        float y = viewport.getWorldHeight() - counterTop - boxHeight;

        batch.setColor(Color.DARK_GRAY);
        batch.draw(pixel, x - border, y - border, boxWidth + 2 * border, boxHeight + 2 * border);
        batch.setColor(0.12f, 0.12f, 0.12f, 1f);
        batch.draw(pixel, x, y, boxWidth, boxHeight);
        batch.setColor(Color.WHITE);

        font.draw(batch, text, x + padding, y + padding + layout.height);
    }

    /** Keeps the HUD camera matched to the window size */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /** Releases the textures and font created by the GUI */
    public void dispose() {
        pixel.dispose();
        font.dispose();
    }

    public void TutorialScreen() {
        Gdx.input.setInputProcessor(stage);

        Label labelWASD = new Label("Use WASD to move", new Label.LabelStyle(null, Color.WHITE));
        Label labelScroll = new Label("Use scroll wheel on mouse or keyboard numbers to select an item", new Label.LabelStyle(null, Color.WHITE));
        Label labelLClick = new Label("Left click to use item", new Label.LabelStyle(null, Color.WHITE));
        Label labelPause = new Label("Press the pause button or ESC to pause", new Label.LabelStyle(null, Color.WHITE));
        Label labelPlay = new Label("Press again to play", new Label.LabelStyle(null, Color.WHITE));
        Label labelInstruct = new Label("Find the bus pass and get to the bus before time runs out!", new Label.LabelStyle(null, Color.WHITE));

        Container labelContainer = new Container(labelWASD);
        labelContainer.bottom();
        stage.addActor(labelWASD);

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A) ||
            Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            labelWASD.setVisible(false);
            Container containerScroll = new Container(labelScroll);
            containerScroll.bottom();
            stage.addActor(labelScroll);
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                labelScroll.setVisible(false);
                Container containerLClick = new Container(labelLClick);
                containerLClick.bottom();
                stage.addActor(labelLClick);
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    labelLClick.setVisible(false);
                    Container containerPause = new Container(labelPause);
                    containerPause.bottom();
                    stage.addActor(labelPause);
                    if (Gdx.input.isKeyJustPressed(ESCAPE)) {
                        labelPause.setVisible(false);
                        Container containerPlay = new Container(labelPlay);
                        containerPlay.bottom();
                        stage.addActor(labelPlay);
                        if (Gdx.input.isKeyJustPressed(ESCAPE)) {
                            labelPlay.setVisible(false);
                            Container containerInstruct = new Container(labelInstruct);
                            containerInstruct.bottom();
                            stage.addActor(labelInstruct);

                        }
                    }
                }
            }
        }
    }

    public void pauseButton(Runnable onPause, Runnable onPlay) {
        float x = viewport.getWorldWidth() - pauseButtonSize - pauseButtonMargin;
        float y = viewport.getWorldHeight() - pauseButtonSize - pauseButtonMargin;
        Gdx.input.setInputProcessor(stage);

        Texture buttonTexture = new Texture("pause.png");
        Sprite buttonSprite = new Sprite(buttonTexture);
        ImageButton pauseButton = new ImageButton(new SpriteDrawable(buttonSprite));
        pauseButton.sizeBy(50 + 2 * border);
        pauseButton.setPosition(x - border, y - border);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameScreen.state == GameState.Pause){
                    onPlay.run();
                }
                else{
                    onPause.run();
                }
            }
        });
        stage.addActor(pauseButton);
    }

    public void update(float delta) {
        stage.act(delta);
        stage.draw();
    }

}
