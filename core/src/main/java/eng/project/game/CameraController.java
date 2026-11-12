package eng.project.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Maintains a smoothly following camera and its viewport configuration. */
public class CameraController {
    private final OrthographicCamera camera;
    private final Viewport viewport;

    public CameraController (){
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(150, 150, camera);
    }
    /** Moves the camera towards the follow target and refreshes its projection matrix. */

    public void update(float followX, float followY, boolean dizzy) {

        //camera movement
        final float t = 0.05f;
        camera.position.x = lerp(camera.position.x, followX, t);
        camera.position.y = lerp(camera.position.y, followY, t);

        // add a wobble effect if the player is dizzy
        if (dizzy) {
            float wobble = (float) Math.sin(
                com.badlogic.gdx.utils.TimeUtils.millis() / 100.0
            ) * 0.15f;

            camera.position.x += wobble;
        }

        viewport.apply();
        camera.update();
    }


    /** Returns the projection matrix used for world rendering. */
    public Matrix4 getMatrix(){
        return camera.combined;
    }

    public Vector2 getWorldPosition( float screenX, float screenY){
        Vector2 position = new Vector2(screenX, screenY);
        viewport.unproject(position);
        return position;
    }


    /** Updates the viewport after a window-size change without re-centering the camera. */
    public void resize(int width, int height){
        viewport.update(width, height, false);
    }

    static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }
}
