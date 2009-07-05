package euclidstand;

import com.jme.bounding.BoundingSphere;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMETerrain;
import java.util.Observer;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author jmtan
 */
public class ShellEntityTest {

	/**
	 * Test of getShell method, of class ShellEntity.
	 */
	@Test
	public void testGetShell() {
		float velocity = 100f;
		JMESpatial self = mock(JMESpatial.class);
		ShellEntity result = ShellEntity.getShell(self, 0.0f, velocity);
		assertEquals("Velocity does not match expected", velocity, result.getSpeed(), 0.0f);
		assertEquals(self, result.getSelf());
		verifyZeroInteractions(self);
	}

	/**
	 * Test of update method, of class ShellEntity.
	 */
	@Test
	public void testUpdate() {
		float interpolation = 10f;
		JMESpatial self = mock(JMESpatial.class);
		// sets verticalVelocity to 0
		ShellEntity instance = ShellEntity.getShell(self, 0.0f, 0.0f);
		instance.update(interpolation);
		verify(self).moveZAxis(anyFloat());
		float exp = -interpolation * Constants.VERTICAL_INCREMENT;
		verify(self).addTranslation(0, exp, 0);
		verifyNoMoreInteractions(self);
	}

	/**
	 * Test of updateTerrain method, of class ShellEntity.
	 */
	@Test
	public void testUpdateTerrainCollision() {
		float height = 7f;
		float radius = 2f;
		float y = 5f;
		JMETerrain terrain = mock(JMETerrain.class);
		JMESpatial self = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		BoundingSphere sphere = mock(BoundingSphere.class);
		ShellEntity instance = ShellEntity.getShell(self, 0.0f, 0.0f);
		instance.addObserver(observer);
		when(terrain.getHeightAboveTerrain(self)).thenReturn(height);
		when(self.getWorldBound()).thenReturn(sphere);
		when(self.getY()).thenReturn(y);
		when(sphere.getRadius()).thenReturn(radius);
		instance.updateTerrain(terrain);
		assertTrue("Instance should be removed", instance.isRemove());
		verify(terrain).getHeightAboveTerrain(self);
		verify(self).getWorldBound();
		verify(self).getY();
		verify(sphere).getRadius();
		verify(observer).update(eq(instance), isNull());
		verifyNoMoreInteractions(terrain, self, sphere, observer);
	}

	/**
	 * Test of updateTerrain method, of class ShellEntity.
	 */
	@Test
	public void testUpdateTerrainNoCollision() {
		float height = 2f;
		float radius = 2f;
		float y = 5f;
		JMETerrain terrain = mock(JMETerrain.class);
		JMESpatial self = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		BoundingSphere sphere = mock(BoundingSphere.class);
		ShellEntity instance = ShellEntity.getShell(self, 0.0f, 0.0f);
		instance.addObserver(observer);
		when(terrain.getHeightAboveTerrain(self)).thenReturn(height);
		when(self.getWorldBound()).thenReturn(sphere);
		when(self.getY()).thenReturn(y);
		when(sphere.getRadius()).thenReturn(radius);
		instance.updateTerrain(terrain);
		assertFalse("Instance should not be removed", instance.isRemove());
		verify(terrain).getHeightAboveTerrain(self);
		verify(self).getWorldBound();
		verify(self).getY();
		verify(sphere).getRadius();
		verifyNoMoreInteractions(terrain, self, sphere);
		verifyZeroInteractions(observer);
	}
}
