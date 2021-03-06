package euclidstand;

import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMETerrain;

/**
 * Game object encapsulating player logic
 */
public class PlayerEntity extends Entity {
	/**
	 * Current state of the player
	 */
	public enum State {
		/**
		 * Player is not doing anything
		 */
		REST,
		/**
		 * Player is charging the cannon
		 */
		CHARGING,
		/**
		 * Player is firing the cannon
		 */
		FIRING,
		/**
		 * Player's dead.
		 */
		DEAD
	};

	private float velocity = 0;
	private State state = null; // for cannon state
	private JMESpatial barrel = null;

	/**
	 * Constructor for Player
	 * @param self Model representing the player
	 * @param barrel Model representing the cannon barrel
	 */
	public PlayerEntity(JMESpatial self, JMESpatial barrel) {
		super(self);
		setHealth(50);
		setState(State.REST);
		this.barrel = barrel;
	}

	/**
	 * Update logic for the player.
	 * Removes player when health reaches 0.
	 */
	@Override
	public void update(float interpolation) {
		if (getHealth() <= 0) {
			setRemove(true);
			setState(State.DEAD);
		} else {
			if (state == State.FIRING) {
				setState(State.REST);
				velocity = 0;
			}
		}
	}

	/**
	 * Locks the model bounds after checking once.
	 * This is done to avoid the model floating due to changing bounds
	 * @param terrain to use for checking
	 */
	@Override
	public void updateTerrain(JMETerrain terrain) {
		super.updateTerrain(terrain);
		getSelf().lockBounds();
	}

	private void setState(State state) {
		this.state = state;
		setChanged();
		notifyObservers(state);
	}

	/**
	 * Charges the player's weapon
	 * @param charging set to true when charging, set to false again to fire
	 */
	public void charge(boolean charging) {
		if (charging) {
			if (state == State.REST)
				setState(State.CHARGING);
			velocity += Constants.VELOCITY_INCREMENT;
		}
		if (!charging && state == State.CHARGING)
			setState(State.FIRING);
	}

	/**
	 * @return the model for the barrel
	 */
	public JMESpatial getBarrel() {
		return barrel;
	}

	/**
	 * @return the firing angle of the player
	 */
	public float getFiringAngle() {
		// invert the angle
		float firingAngle = -barrel.getXRotation();
		float angle_range = Constants.UP_ANGLE_MAXIMUM - Constants.DOWN_ANGLE_MAXIMUM;
		// assume that UP_INPUT is negative, and DOWN_INPUT is positive
		float input_range = Math.abs(Constants.UP_INPUT_MAXIMUM) + Constants.DOWN_INPUT_MAXIMUM;
		float scale = angle_range / input_range;
		return firingAngle * scale + Constants.DOWN_ANGLE_MAXIMUM;
	}

	/**
	 * @return the initial firing velocity of the player
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * @return the current facing of the player
	 */
	public float getFacing() {
		return getSelf().getYRotation();
	}

	public static class Factory {
		public PlayerEntity make(JMESpatial self, JMESpatial barrel) {
			return new PlayerEntity(self, barrel);
		}
	}
}
