package euclidstand;

import com.jme.scene.Spatial;

/**
 * Game object encapsulating baddie logic
 */
public class EnemyEntity extends Entity {

	private Entity target = null;
	private int damage = 0;

	/**
	 * Constructor for Enemy
	 * @param self Model representing the bad guy
	 * @param target Entity that the bad guy should be trying to hit
	 */
	public EnemyEntity(Spatial self, Entity target) {
		super(self);
		setTarget(target);
		setSpeed(15);
		setDamage(1);
	}

	/**
	 * Updates logic for the bad guy.
	 * This implementation makes the bad guy go forward until it hits the target.
	 */
	@Override
	public void update(float interpolation) {
		moveForward(interpolation);
		if (!getTarget().isRemove() && hasCollision(getTarget())) {
			setRemove(true);
			getTarget().hit(getDamage());
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * @param target that bad guy is trying to hit
	 */
	public void setTarget(Entity target) {
		this.target = target;
	}

	/**
	 * @return target that bad guy is trying to hit
	 */
	public Entity getTarget() {
		return target;
	}

	/**
	 * @param damage that the bad guy will do
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return damage that the bad guy will do
	 */
	public int getDamage() {
		return damage;
	}
}
