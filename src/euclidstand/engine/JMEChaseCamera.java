package euclidstand.engine;

import com.jme.input.ChaseCamera;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 *
 * @author jmtan
 */
public class JMEChaseCamera extends ChaseCamera {
    public JMEChaseCamera(Camera cam, JMESpatial target) {
        super(cam, target.getSpatial());
    }

	public void setMouseXMultiplier(float multiplier) {
		getMouseLook().setMouseXMultiplier(multiplier);
	}

	public void setMouseYMultiplier(float multiplier) {
		getMouseLook().setMouseYMultiplier(multiplier);
	}

	public void setRotateTarget(boolean rotate) {
		getMouseLook().setRotateTarget(rotate);
	}

	public Vector3f getLocation() {
		return getCamera().getLocation();
	}

	public void updateTerrain(JMETerrain terrain) {
		Vector3f camLoc = cam.getLocation();
		float minCamHeight = terrain.getHeightFromWorld(camLoc) + 2;
		if (camLoc.y < minCamHeight) {
			cam.getLocation().y = minCamHeight;
			cam.update();
		}
	}
}
