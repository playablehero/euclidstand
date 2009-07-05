package euclidstand;

import euclidstand.engine.JMESpatial;
import java.util.Observer;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author jmtan
 */
public class EnemyEntityTest {

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	/**
	 * Test of update method, of class EnemyEntity.
	 */
	@Test
	public void testUpdate() {
		Entity target = mock(Entity.class);
		JMESpatial spatial = mock(JMESpatial.class);
		EnemyEntity instance = new EnemyEntity(spatial, target);
		when(target.isRemove()).thenReturn(true);
		instance.update(0);
		verify(spatial).moveZAxis(anyFloat());
		verifyNoMoreInteractions(spatial);
	}

	@Test
	public void testHitTargetAndDie() {
		int damage = 10;
		Entity target = mock(Entity.class);
		JMESpatial spatial = mock(JMESpatial.class);
		JMESpatial spatial2 = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		EnemyEntity instance = new EnemyEntity(spatial, target);
		instance.setDamage(damage);
		instance.addObserver(observer);
		when(target.isRemove()).thenReturn(false);
		when(target.getSelf()).thenReturn(spatial2);
		when(spatial.hasCollision(spatial2, false)).thenReturn(true);
		instance.update(0);
		assertTrue("Instance should be removed", instance.isRemove());
		verify(spatial).moveZAxis(anyFloat());
		verify(target).isRemove();
		verify(spatial).hasCollision(spatial2, false);
		verify(target).getSelf();
		verify(target).hit(damage);
		verify(observer).update(isA(EnemyEntity.class), isNull());
		verifyNoMoreInteractions(spatial, spatial2, target, observer);
	}

	@Test
	public void testDieWithoutHitting() {
		int health = 0;
		Entity target = mock(Entity.class);
		JMESpatial spatial = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		EnemyEntity instance = new EnemyEntity(spatial, target);
		instance.addObserver(observer);
		instance.setHealth(health);
		when(target.isRemove()).thenReturn(true);
		instance.update(0);
		assertTrue("Instance should be removed", instance.isRemove());
		verify(spatial).moveZAxis(anyFloat());
		verify(target).isRemove();
		verify(observer).update(isA(EnemyEntity.class), isNull());
		verifyNoMoreInteractions(spatial, target, observer);
	}
}
