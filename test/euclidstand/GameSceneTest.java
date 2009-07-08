package euclidstand;

import com.jme.math.Vector3f;
import euclidstand.engine.JMEChaseCamera;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMETerrain;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 * @author jmtan
 */
public class GameSceneTest {

	/**
	 * Test of update method, of class GameScene.
	 */
	@Test
	public void testUpdate() {
		float interpolation = 1.0F;
		List<Entity> entities = new LinkedList<Entity>();
		List<Entity> entitiesToAdd = new LinkedList<Entity>();
		List<EntityObserver> observers = new LinkedList<EntityObserver>();
		List<Entity> entitiesToRemove = new LinkedList<Entity>();
		Entity entity = mock(Entity.class);
		Entity entity1 = mock(Entity.class);
		entitiesToAdd.add(entity);
		entitiesToAdd.add(entity1);
		JMESpatial sky = mock(JMESpatial.class);
		JMENode sceneNode = mock(JMENode.class);
		JMETerrain terrain = mock(JMETerrain.class);
		JMEChaseCamera chasecam = mock(JMEChaseCamera.class);
		Vector3f camLocation = mock(Vector3f.class);
		GameSceneUI ui = mock(GameSceneUI.class);
		PlayerObserver playerObserver = mock(PlayerObserver.class);
		PlayerEntity playerEntity = mock(PlayerEntity.class);
		observers.add(playerObserver);
		when(entity.isRemove()).thenReturn(true);
		when(entity1.isRemove()).thenReturn(false);
		when(chasecam.getLocation()).thenReturn(camLocation);
		when(playerObserver.getPlayer()).thenReturn(playerEntity);

		GameScene instance = new GameScene(entities, entitiesToAdd, observers, entitiesToRemove, sceneNode, terrain, sky, chasecam, ui);
		instance.update(interpolation);

		int expectedEntities = 1;
		assertEquals("Number of entities should be " + expectedEntities, expectedEntities, entities.size());
		assertEquals("Entities does not match expected", entity1, entities.get(0));
		verify(chasecam).update(interpolation);
		verify(entity).isRemove();
		verify(entity).updateTerrain(terrain);
		verify(entity).update(interpolation);
		verify(entity).remove();
		verify(entity1).isRemove();
		verify(entity1).updateTerrain(terrain);
		verify(entity1).update(interpolation);
		verify(chasecam).updateTerrain(terrain);
		verify(chasecam).getLocation();
		verify(sky).setLocalTranslation(camLocation);
		verify(playerObserver).getPlayer();
		verify(ui).update(playerEntity);
		verify(playerObserver).updateInput(interpolation);
		verifyNoMoreInteractions(chasecam, entity, entity1, chasecam, sky, playerObserver, ui);
		verifyZeroInteractions(camLocation, playerEntity);
	}
}
