package eng.project.entity.thrown;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ThrownItem {
    void update(float delta);
    void render(SpriteBatch batch);

    float getX();
    float getY();
    float getSize();

    boolean isFinished();
}
