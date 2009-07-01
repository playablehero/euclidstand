package euclidstand;

import java.util.Observable;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;

/**
 * Generic game object.
 * Entities are responsible for notifying their observers of changes in state,
 * and the positioning and movement of their spatials.
 */
public class Entity extends Observable {

	/**
	 * Model representing this entity
	 */
	protected final Spatial self;
	/**
	 * Movement speed per frame
	 */
	protected float speed = 0;
	/**
	 * Health of this entity
	 */
	protected int health = 0;
	/**
	 * Indicates whether this entity will be removed next frame
	 */
	protected boolean remove = false;

	/**
	 * Constructor for Entity
	 * @param self Model representing the entity
	 */
	public Entity(Spatial self) {
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
	public void updateTerrain(TerrainBlock terrain) {
		float spatialY = terrain.getHeightFromWorld(self.getLocalTranslation()) +
				((BoundingBox) self.getWorldBound()).yExtent;
		self.getLocalTranslation().y = spatialY;
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
	public Spatial getSelf() {
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