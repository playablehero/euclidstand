package euclidstand;

import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;

/**
 * Defines a cannon shell
 */
public class Shell extends Entity {
	public Shell(Spatial self, float angle, float velocity, float facing) {
		super(self);
		setSpeed(30);
	}

	/**
	 * Updates shell movement
	 */
	@Override
	public void update(float interpolation) {
		Spatial localSpatial = getSelf();
		localSpatial.getLocalTranslation().addLocal(localSpatial.getLocalRotation().
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
