package euclidstand.engine;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jmex.terrain.TerrainBlock;

/**
 *
 * @author jmtan
 */
public class JMETerrain extends TerrainBlock {
    public JMETerrain(String name, int size, Vector3f stepScale,
    		float[] heightMap, Vector3f origin) {
		super(name, size, stepScale, heightMap, origin);
	}

	public float getHeightAboveTerrain(JMESpatial spatial) {
		float sizeOffset = 0;
		if (spatial.getWorldBound() instanceof BoundingBox) {
			Vector3f extents = ((BoundingBox)spatial.getWorldBound()).getExtent(null);
			sizeOffset = extents.getY();
		}
		if (spatial.getWorldBound() instanceof BoundingSphere) {
			sizeOffset = ((BoundingSphere)spatial.getWorldBound()).getRadius();
		}
		float height = getHeightFromWorld(spatial.getLocalTranslation());
		return height + sizeOffset;
	}
}
