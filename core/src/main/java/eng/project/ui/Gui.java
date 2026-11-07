package eng.project.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private static final float slotSize = 64f;
    private static final float slotGap = 8f;
    private static final float border = 3f;
    private static final float bottomMargin = 20f;
    private static final float iconPadding = 8f;
    private static final float barHeight = 18f;
    private static final float barGap = 6f;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final ScreenViewport viewport = new ScreenViewport(camera);
    private final Texture pixel;
    private final BitmapFont font = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();
    private static final float nameShowTime = 1.5f;
    private int lastSelectedIndex = 0;
    private float nameTimer = 0f;

    public Gui() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();
    }

    public void render(SpriteBatch batch, Inventory inventory, Player player){
        float delta = Gdx.graphics.getDeltaTime();
        updateEffects(delta, inventory, player);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderStatusBars(batch, player);
        renderHotbar(batch, inventory);
        renderItemName(batch, inventory);
        batch.end();
    }

    private void updateEffects(float delta, Inventory inventory, Player player) {
        if (inventory.getSelectedIndex() != lastSelectedIndex) {
            lastSelectedIndex = inventory.getSelectedIndex();
            nameTimer = nameShowTime;
        }
        if (nameTimer > 0f) {
            nameTimer -= delta;
        }
    }

    private void renderStatusBars(SpriteBatch batch, Player player) {
        float width = Inventory.size * slotSize + (Inventory.size - 1) * slotGap;
        float x = (viewport.getWorldWidth() - width) / 2f;

        float staminaY = bottomMargin + slotSize + border + barGap + border;
        float healthY = staminaY + barHeight + barGap;

        drawBar(batch, x, healthY, width, player.getHealth(), player.getMaxHealth(), Color.RED);
        drawBar(batch, x, staminaY, width, player.getStamina(), player.getMaxStamina(), Color.GOLD);
    }

    private void drawBar(SpriteBatch batch, float x, float y, float width, float value, float max, Color fillColour) {
        float fraction = Math.max(0f, Math.min(1f, value / max));

        batch.setColor(0.1f, 0.1f, 0.1f, 1f);
        batch.draw(pixel, x - border, y - border, width + 2 * border, barHeight + 2 * border);
        batch.setColor(fillColour);
        batch.draw(pixel, x, y, width * fraction, barHeight);
        batch.setColor(Color.WHITE);

        String text = Math.round(value) + " / " + Math.round(max);
        layout.setText(font, text);
        float textX = x + (width - layout.width) / 2f;
        float textY = y + (barHeight + layout.height) / 2f;
        font.draw(batch, text, textX, textY);
    }

    public void renderHotbar(SpriteBatch batch, Inventory inventory) {
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

    public void renderItemName(SpriteBatch batch, Inventory inventory) {
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

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        pixel.dispose();
        font.dispose();
    }
}
