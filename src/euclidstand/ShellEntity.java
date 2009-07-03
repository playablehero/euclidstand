package euclidstand;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;

/**
 * Defines a cannon shell
 */
public class ShellEntity extends Entity {

	private float verticalVelocity;

	/**
	 * Constructor for shell
	 * @param self model representing the shell
	 * @param angle of initial position
	 * @param velocity of initial position
	 * @param facing of initial position
	 */
	private ShellEntity(Spatial self, float velocity, float verticalVelocity) {
		super(self);
		setSpeed(velocity);
		this.verticalVelocity = verticalVelocity;
	}

	public static ShellEntity getShell(Spatial self, float angle, float velocity) {
		float verticalVelocity = (float) Math.sin(angle) * velocity * Constants.VERTICAL_SCALE;
		return new ShellEntity(self, velocity, verticalVelocity);
	}

	/**
	 * Attaches an explosion to the location of this shell
	 * @param explosion model
	 */
	public void setExplosion(Spatial explosion) {
		getSelf().getParent().attachChild(explosion);
		getSelf().getParent().updateRenderState();
	}

	/**
	 * Updates shell movement
	 * @param interpolation time variable
	 */
	@Override
	public void update(float interpolation) {
		moveForward(interpolation);
		verticalVelocity += Constants.VERTICAL_INCREMENT * interpolation;
		Vector3f v = new Vector3f(0, -verticalVelocity, 0);
		getSelf().getLocalTranslation().addLocal(v);
	}

	/**
	 * Checks whether shell has impacted terrain
	 */
	@Override
	public void updateTerrain(TerrainBlock terrain) {
		Spatial self = getSelf();
		float height = terrain.getHeightFromWorld(self.getLocalTranslation());
		float spatialY = ((BoundingSphere)self.getWorldBound()).getRadius() +
				self.getLocalTranslation().getY() - height;
		if (spatialY < Constants.SHELL_COLLISION_THRESHOLD) {
			setRemove(true);
			setChanged();
			notifyObservers();
		}
	}
}