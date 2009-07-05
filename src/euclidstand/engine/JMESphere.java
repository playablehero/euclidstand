package euclidstand.engine;

import com.jme.intersection.CollisionResults;
import com.jme.scene.shape.Sphere;

/**
 *
 * @author jmtan
 */
public class JMESphere extends Sphere {
    public JMESphere(String name, JMESpatial spatial, int zSamples, int radialSamples, float radius) {
        super(name, spatial.getWorldTranslation(), zSamples, radialSamples, radius);
	}

	public void calculateCollisions(JMENode scene, CollisionResults results) {
		calculateCollisions(scene.getNode(), results);
	}
}
