package euclidstand;

import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionData;
import java.util.List;

/**
 * Processes the results of a collision
 */
public class ShellCollision extends BoundingCollisionResults {

	private final List<Entity> entities;

	/**
	 * Construct a ShellCollision list which processes collision results
	 * @param entities list of all entities in the system
	 */
	public ShellCollision(List<Entity> entities) {
		this.entities = entities;
	}

	@Override
	public void processCollisions() {
		int damage = 1;
		for (int i = 0; i < getNumber(); i++) {
			CollisionData collisionData = getCollisionData(i);
			Entity entity = findMatchingEntity(collisionData.getTargetMesh().getName());
			if (entity != null) {
				entity.hit(damage);
			}
		}
	}

	private Entity findMatchingEntity(String name) {
		// assume only one entity will have a matching name
		for (Entity e : entities) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public static class Factory {
		public ShellCollision make(List<Entity> entities) {
			return new ShellCollision(entities);
		}
	}
}
