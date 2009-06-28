package euclidstand;

import java.util.logging.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;

/**
 * Defines a cannon shell
 */
public class Shell extends Entity {
	private static final Logger logger = Logger.getLogger(Shell.class.getName());

	public Shell(Spatial self, float angle, float velocity, float facing) {
		super(self);
		setSpeed(30);
	}

	/**
	 * Updates shell movement
	 */
	@Override
	public void update(float interpolation) {
		Spatial self = getSelf();
		self.getLocalTranslation().addLocal(self.getLocalRotation().
				getRotationColumn(2).mult(interpolation * getSpeed()));
	}

	/**
	 * Checks whether shell has impacted terrain
	 */
	@Override
	public void updateTerrain(TerrainBlock terrain) {
		/*
		float spatialY = terrain.getHeightFromWorld(self.getLocalTranslation()) +
			((BoundingBox)self.getWorldBound()).yExtent;
		if (spatialY < 0) {
			setRemove(true);
			setChanged();
			notifyObservers();
		}*/
	}
}
