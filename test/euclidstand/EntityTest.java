package euclidstand;

import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMETerrain;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author jmtan
 */
public class EntityTest {

	/**
	 * Test of update method, of class Entity.
	 */
	@Test
	public void testUpdate() {
		float interpolation = 0.0F;
		JMESpatial spatial = mock(JMESpatial.class);
		Entity instance = new Entity(spatial);
		instance.update(interpolation);
		verifyNoMoreInteractions(spatial);
	}

	/**
	 * Test of updateTerrain method, of class Entity.
	 */
	@Test
	public void testUpdateTerrain() {
		JMESpatial spatial = mock(JMESpatial.class);
		Entity instance = new Entity(spatial);
		JMETerrain terrain = mock(JMETerrain.class);
		float height = 20f;
		when(terrain.getHeightAboveTerrain(spatial)).thenReturn(height);
		instance.updateTerrain(terrain);
		verify(terrain).getHeightAboveTerrain(spatial);
		verify(spatial).setY(height);
		verifyNoMoreInteractions(terrain, spatial);
	}

	/**
	 * Test of moveForward method, of class Entity.
	 */
	@Test
	public void testMoveForward() {
		float interpolation = 10f;
		float speed = 20f;
		JMESpatial spatial = mock(JMESpatial.class);
		Entity instance = new Entity(spatial);
		instance.setSpeed(speed);
		instance.moveForward(interpolation);
		verify(spatial).moveZAxis(interpolation * speed);
		verifyNoMoreInteractions(spatial);
	}

	/**
	 * Test of getName method, of class Entity.
	 */
	@Test
	public void testGetName() {
		String expResult = "testname";
		JMESpatial spatial = mock(JMESpatial.class);
		Entity instance = new Entity(spatial);
		when(spatial.getName()).thenReturn(expResult);
		String result = instance.getName();
		assertEquals(expResult, result);
		verify(spatial).getName();
		verifyNoMoreInteractions(spatial);
	}

	/**
	 * Test of remove method, of class Entity.
	 */
	@Test
	public void testRemove() {
		JMESpatial spatial = mock(JMESpatial.class);
		Entity instance = new Entity(spatial);
		instance.remove();
		verify(spatial).removeFromParent();
		verifyNoMoreInteractions(spatial);
	}

	/**
	 * Test of hasCollision method, of class Entity.
	 */
	@Test
	public void testHasCollision() {
		Entity other = mock(Entity.class);
		JMESpatial spatial1 = mock(JMESpatial.class);
		Entity instance = new Entity(spatial1);
		JMESpatial spatial2 = mock(JMESpatial.class);
		boolean expResult = true;
		when(spatial1.hasCollision(spatial2, false)).thenReturn(expResult);
		when(other.getSelf()).thenReturn(spatial2);
		boolean result = instance.hasCollision(other);
		assertEquals(expResult, result);
		verify(spatial1).hasCollision(spatial2, false);
		verify(other).getSelf();
		verifyNoMoreInteractions(spatial1, spatial2, other);
	}

	/**
	 * Test of hit method, of class Entity.
	 */
	@Test
	public void testHit() {
		int damage = 10;
		int health = 20;
		JMESpatial spatial = mock(JMESpatial.class);
		Entity instance = new Entity(spatial);
		instance.setHealth(health);
		instance.hit(damage);
		int result = instance.getHealth();
		assertEquals(health - damage, result);
		verifyZeroInteractions(spatial);
	}
}
