package euclidstand;

import java.util.List;

/**
 * Observers track the state of entities, and take action when the state changes.
 */
public abstract class EntityObserver {

	/**
	 * List of entities to add next frame
	 */
	protected List<Entity> entitiesToAdd = null;

	/**
	 * @param entitiesToAdd list of entities which will be added next frame
	 */
	public EntityObserver(List<Entity> entitiesToAdd) {
		this.entitiesToAdd = entitiesToAdd;
	}
}
