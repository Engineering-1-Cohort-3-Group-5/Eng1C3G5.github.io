package eng.project.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public class CameraController {
    private final OrthographicCamera camera;

    public CameraController (){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 20, 12);
    }
    public void update(float followX,float followY){
        //linear interpolation between the camera pos and target pos to smooth its movements
        //t varies how tightly the camera will follow the target, higher t -> stricter following
        final float t = 0.05f;
        camera.position.x = lerp(camera.position.x,followX,t);
        camera.position.y = lerp(camera.position.y,followY,t);
        camera.update();
    }

    public Matrix4 getMatrix(){
        return camera.combined;
    }

    static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }
}
