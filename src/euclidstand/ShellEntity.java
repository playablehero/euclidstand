package euclidstand;

import com.jme.bounding.BoundingSphere;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMETerrain;

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
	private ShellEntity(JMESpatial self, float velocity, float verticalVelocity) {
		super(self);
		setSpeed(velocity);
		this.verticalVelocity = verticalVelocity;
	}

	/**
	 * Updates shell movement
	 * @param interpolation time variable
	 */
	@Override
	public void update(float interpolation) {
		moveForward(interpolation);
		verticalVelocity += Constants.VERTICAL_INCREMENT * interpolation;
		getSelf().addTranslation(0, -verticalVelocity, 0);
	}

	/**
	 * Checks whether shell has impacted terrain
	 */
	@Override
	public void updateTerrain(JMETerrain terrain) {
		JMESpatial self = getSelf();
		float height = terrain.getHeightAboveTerrain(self);
		float spatialY = ((BoundingSphere) self.getWorldBound()).getRadius() +
				self.getY() - height;
		if (spatialY < Constants.SHELL_COLLISION_THRESHOLD) {
			setRemove(true);
			setChanged();
			notifyObservers();
		}
	}

	public static class Factory {
		public ShellEntity make(JMESpatial self, float angle, float velocity) {
			float verticalVelocity = (float) Math.sin(angle) * velocity * Constants.VERTICAL_SCALE;
			return new ShellEntity(self, velocity, verticalVelocity);
		}
	}
}
