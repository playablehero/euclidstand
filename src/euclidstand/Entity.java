package euclidstand;

import java.util.Observable;

import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMETerrain;

/**
 * Generic game object.
 * Entities are responsible for notifying their observers of changes in state,
 * and the positioning and movement of their spatials.
 */
public class Entity extends Observable {

	/**
	 * Model representing this entity
	 */
	private final JMESpatial self;
	/**
	 * Movement speed per frame
	 */
	private float speed = 0;
	/**
	 * Health of this entity
	 */
	private int health = 0;
	/**
	 * Indicates whether this entity will be removed next frame
	 */
	private boolean remove = false;

	/**
	 * Constructor for Entity
	 * @param self Model representing the entity
	 */
	public Entity(JMESpatial self) {
		this.self = self;
	}

	/**
	 * Updates entity logic every frame.
	 * Default implementation does nothing
	 * @param interpolation current time per frame
	 */
	public void update(float interpolation) {
	}

	/**
	 * Sets the entity's y-value to be level with terrain
	 * @param terrain to use for checking
	 */
	public void updateTerrain(JMETerrain terrain) {
		self.setY(terrain.getHeightAboveTerrain(self));
	}

	/**
	 * Moves the entity forward, defined by current speed
	 * @param interpolation time variable
	 */
	public void moveForward(float interpolation) {
		self.moveZAxis(interpolation * getSpeed());
	}

	/**
	 * @return entity name
	 */
	public String getName() {
		return self.getName();
	}

	/**
	 * Removes this entity from the scene
	 */
	public void remove() {
		self.removeFromParent();
	}

	/**
	 * Tests for collision with another entity
	 * @param other entity to test with
	 * @return true if collided, false otherwise
	 */
	public boolean hasCollision(Entity other) {
		return self.hasCollision(other.getSelf(), false);
	}

	/**
	 * Do a set amount of damage to this entity
	 * @param damage amount of damage
	 */
	public void hit(int damage) {
		health -= damage;
	}

	/**
	 * Get the model representing the entity
	 *
	 * @return instance of Spatial
	 */
	public JMESpatial getSelf() {
		return self;
	}

	/**
	 * @param speed of the entity
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * @return speed of the entity
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param health of the entity
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return health of the entity
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param remove whether this entity should be removed
	 */
	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	/**
	 * @return whether this entity should be removed
	 */
	public boolean isRemove() {
		return remove;
	}
}
