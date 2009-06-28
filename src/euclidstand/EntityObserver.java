package euclidstand;

import java.util.LinkedList;

/**
 * Observers track the state of entities, and take action when the state changes.
 */
public abstract class EntityObserver {
	protected LinkedList<Entity> entities = null;

	public EntityObserver(LinkedList<Entity> entities) {
		this.entities = entities;
	}
}
