package eng.project.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraController {
    private final OrthographicCamera camera;
    private final Viewport viewport;

    public CameraController (){
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(20, 15, camera);
    }
    public void update(float followX,float followY){
        //linear interpolation between the camera pos and target pos to smooth its movements
        //t varies how tightly the camera will follow the target, higher t -> stricter following
        final float t = 0.05f;
        camera.position.x = lerp(camera.position.x,followX,t);
        camera.position.y = lerp(camera.position.y,followY,t);
        viewport.apply();
        camera.update();
    }

    public Matrix4 getMatrix(){
        return camera.combined;
    }

    public void resize(int width, int height){
        viewport.update(width, height, false);
    }

    static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }
}
