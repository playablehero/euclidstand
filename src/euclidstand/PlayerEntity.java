package euclidstand;

import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;

/**
 * Game object encapsulating player logic
 */
public final class PlayerEntity extends Entity {
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

	private int score = 0;
	private float angle = 0;
	private float velocity = 0;
	private float facing = 0;
	private State state = null; // for cannon state
	private Spatial barrel = null;

	/**
	 * Constructor for Player
	 * @param self Model representing the player
	 * @param barrel Model representing the cannon barrel
	 */
	public PlayerEntity(Spatial self, Spatial barrel) {
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
	public void updateTerrain(TerrainBlock terrain) {
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
			velocity += 5;
		}
		if (!charging && state == State.CHARGING)
			setState(State.FIRING);
	}

	/**
	 * @return the model for the barrel
	 */
	public Spatial getBarrel() {
		return barrel;
	}

	/**
	 * @return the firing angle of the player
	 */
	public float getFiringAngle() {
		float[] angles = barrel.getLocalRotation().toAngles(null);
		return angles[0];
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
		float[] angles = getSelf().getLocalRotation().toAngles(null);
		return angles[1];
	}
}
