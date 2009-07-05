package euclidstand.engine;

import com.jme.input.ChaseCamera;
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
}
