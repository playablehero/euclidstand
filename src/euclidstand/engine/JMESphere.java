package euclidstand.engine;

import com.jme.bounding.BoundingSphere;
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

    public JMESphere(String name, int zSamples, int radialSamples, float radius) {
        super(name, zSamples, radialSamples, radius);
	}

	public void calculateCollisions(JMENode scene, CollisionResults results) {
		calculateCollisions(scene.getNode(), results);
	}

	public void setBoundsToSphere() {
		setModelBound(new BoundingSphere());
		updateModelBound();
	}

	public void setSphereToLocation(JMESpatial spatial) {
		updateGeometry(spatial.getWorldTranslation(), getZSamples(), getRadialSamples(), getRadius());
		updateModelBound();
	}

	public static class Factory {
		public JMESphere make(String name, int zSamples, int radialSamples, float radius) {
			return new JMESphere(name, zSamples, radialSamples, radius);
		}
		public JMESphere make(String name, JMESpatial spatial, int zSamples, int radialSamples, float radius) {
			return new JMESphere(name, spatial, zSamples, radialSamples, radius);
		}
	}
}
