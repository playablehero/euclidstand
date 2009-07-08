
package euclidstand;

import com.jmex.effects.particles.ParticleMesh;
import euclidstand.EnemyEntity.Factory;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESpatial;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Eugene
 */
public class EnemyObserverTest {

	/**
	 * Test of createWave method, of class EnemyObserver.
	 */
	@Test
	public void testCreateWave() {
		int number = 10;
		List<Entity> entitiesToAdd = mock(List.class);
		Entity target = mock(Entity.class);
		Builder builder = mock(Builder.class);
		JMENode enemyNode = mock(JMENode.class);
		Factory enemyFactory = mock(Factory.class);
		JMESpatial enemySpatial = mock(JMESpatial.class);
		EnemyEntity enemyEntity = mock(EnemyEntity.class);
		when(builder.buildBaddie(anyString())).thenReturn(enemySpatial);
		when(enemyFactory.make(enemySpatial, target)).thenReturn(enemyEntity);
		when(enemyEntity.getSelf()).thenReturn(enemySpatial);

		EnemyObserver instance = new EnemyObserver(entitiesToAdd, target, enemyNode, builder, enemyFactory);
		instance.createWave(number);

		assertEquals("Parameter entitiesToAdd does not match instance", entitiesToAdd, instance.entitiesToAdd);
		verify(builder, times(number)).buildBaddie(anyString());
		verify(enemyFactory, times(number)).make(enemySpatial, target);
		verify(entitiesToAdd, times(number)).add(enemyEntity);
		verify(enemyEntity, times(number)).addObserver(instance);
		verify(enemyEntity, times(number)).getSelf();
		verify(enemyNode, times(number)).attachChild(enemySpatial);
		verify(enemyNode, times(number)).updateRenderState();
		verifyNoMoreInteractions(builder, enemyFactory, entitiesToAdd, enemyEntity, enemyNode);
		verifyZeroInteractions(target, enemySpatial);
	}

	/**
	 * Test of update method, of class EnemyObserver.
	 */
	@Test
	public void testUpdate() {
		String entityName = "test name";
		List<Entity> entitiesToAdd = mock(List.class);
		Entity target = mock(Entity.class);
		Builder builder = mock(Builder.class);
		JMENode enemyNode = mock(JMENode.class);
		Factory enemyFactory = mock(Factory.class);
		JMESpatial enemySpatial = mock(JMESpatial.class);
		EnemyEntity enemyEntity = mock(EnemyEntity.class);
		ParticleMesh explosion = mock(ParticleMesh.class);
		when(builder.buildBaddie(anyString())).thenReturn(enemySpatial);
		when(enemyFactory.make(enemySpatial, target)).thenReturn(enemyEntity);
		when(enemyEntity.getSelf()).thenReturn(enemySpatial);
		when(enemyEntity.getName()).thenReturn(entityName);
		when(builder.buildSmallExplosion(anyString(), eq(enemySpatial))).thenReturn(explosion);

		EnemyObserver instance = new EnemyObserver(entitiesToAdd, target, enemyNode, builder, enemyFactory);
		instance.update(enemyEntity, null);

		verify(builder, atLeastOnce()).buildBaddie(anyString());
		verify(builder, atLeastOnce()).buildSmallExplosion(eq(entityName + "Death"), eq(enemySpatial));
		verify(enemyFactory, atLeastOnce()).make(enemySpatial, target);
		verify(entitiesToAdd, atLeastOnce()).add(enemyEntity);
		verify(enemyEntity, atLeastOnce()).addObserver(instance);
		verify(enemyEntity, atLeastOnce()).getSelf();
		verify(enemyEntity, atLeastOnce()).getName();
		verify(enemyNode, atLeastOnce()).attachChild(enemySpatial);
		verify(enemyNode, atLeastOnce()).attachChild(explosion);
		verify(explosion, atLeastOnce()).forceRespawn();
		verify(enemyNode, atLeastOnce()).updateRenderState();
		verifyNoMoreInteractions(builder, enemyFactory, entitiesToAdd, enemyEntity, enemyNode, explosion);
		verifyZeroInteractions(target, enemySpatial);
	}

}