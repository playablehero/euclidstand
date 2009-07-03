package euclidstand;

import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;

/**
 * Defines a cannon shell
 */
public class Shell extends Entity {

	private float angle = 0;
	private float velocity = 0;
	private float facing = 0;

	/**
	 * Constructor for shell
	 * @param self model representing the shell
	 * @param angle of initial position
	 * @param velocity of initial position
	 * @param facing of initial position
	 */
	public Shell(Spatial self, float angle, float velocity, float facing) {
		super(self);
		setSpeed(30);
		this.angle = angle;
		this.velocity = velocity;
		this.facing = facing;
	}

	/**
	 * Updates shell movement
	 * @param interpolation time variable
	 */
	@Override
	public void update(float interpolation) {
		// TODO: Replace with proper trajectory
		moveForward(interpolation);
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
