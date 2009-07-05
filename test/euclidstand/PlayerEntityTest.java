package euclidstand;

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
public class PlayerEntityTest {

	/**
	 * Test of update method, of class PlayerEntity.
	 */
	@Test
	public void testUpdate() {
		JMESpatial self = mock(JMESpatial.class);
		JMESpatial barrel = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		PlayerEntity instance = new PlayerEntity(self, barrel);
		instance.addObserver(observer);
		instance.update(0);
		verifyZeroInteractions(self, barrel, observer);
	}

	@Test
	public void testUpdateDie() {
		int health = 0;
		JMESpatial self = mock(JMESpatial.class);
		JMESpatial barrel = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		PlayerEntity instance = new PlayerEntity(self, barrel);
		instance.addObserver(observer);
		instance.setHealth(health);
		instance.update(0);
		assertTrue("Instance should be removed", instance.isRemove());
		PlayerEntity.State expected = PlayerEntity.State.DEAD;
		verify(observer).update(isA(PlayerEntity.class), eq(expected));
		verifyNoMoreInteractions(observer);
		verifyZeroInteractions(self, barrel);
	}

	/**
	 * Test of updateTerrain method, of class PlayerEntity.
	 */
	@Test
	public void testUpdateTerrain() {
		JMESpatial self = mock(JMESpatial.class);
		JMESpatial barrel = mock(JMESpatial.class);
		JMETerrain terrain = mock(JMETerrain.class);
		PlayerEntity instance = new PlayerEntity(self, barrel);
		instance.updateTerrain(terrain);
		verify(self).lockBounds();
		verify(self).setY(anyFloat());
		verify(terrain).getHeightAboveTerrain(any(JMESpatial.class));
		verifyNoMoreInteractions(self, terrain);
		verifyZeroInteractions(barrel);
	}

	/**
	 * Test of charge method, of class PlayerEntity.
	 */
	@Test
	public void testChargeIsCharging() {
		JMESpatial self = mock(JMESpatial.class);
		JMESpatial barrel = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		PlayerEntity instance = new PlayerEntity(self, barrel);
		instance.addObserver(observer);
		instance.charge(true);
		assertEquals("Velocity does not match expected after charging",
				Constants.VELOCITY_INCREMENT, instance.getVelocity(), 0.0);
		PlayerEntity.State expected = PlayerEntity.State.CHARGING;
		verify(observer).update(isA(PlayerEntity.class), eq(expected));
		verifyNoMoreInteractions(observer);
		verifyZeroInteractions(self, barrel);
	}

	/**
	 * Test of charge method, of class PlayerEntity.
	 */
	@Test
	public void testChargeAndFire() {
		JMESpatial self = mock(JMESpatial.class);
		JMESpatial barrel = mock(JMESpatial.class);
		Observer observer = mock(Observer.class);
		PlayerEntity instance = new PlayerEntity(self, barrel);
		instance.addObserver(observer);

		instance.charge(true);
		PlayerEntity.State expected = PlayerEntity.State.CHARGING;
		verify(observer).update(isA(PlayerEntity.class), eq(expected));

		instance.charge(false);
		expected = PlayerEntity.State.FIRING;
		verify(observer).update(isA(PlayerEntity.class), eq(expected));

		verifyNoMoreInteractions(observer);
		verifyZeroInteractions(self, barrel);
	}

	/**
	 * Test of getFiringAngle method, of class PlayerEntity.
	 */
	@Test
	public void testGetFiringAngle() {
		float rotation = 20f;
		JMESpatial self = mock(JMESpatial.class);
		JMESpatial barrel = mock(JMESpatial.class);
		PlayerEntity instance = new PlayerEntity(self, barrel);
		when(barrel.getXRotation()).thenReturn(rotation);
		float result = instance.getFiringAngle();

		// invert the angle
		float firingAngle = -rotation;
		float angle_range = Constants.UP_ANGLE_MAXIMUM - Constants.DOWN_ANGLE_MAXIMUM;
		// assume that UP_INPUT is negative, and DOWN_INPUT is positive
		float input_range = Math.abs(Constants.UP_INPUT_MAXIMUM) + Constants.DOWN_INPUT_MAXIMUM;
		float scale = angle_range / input_range;
		float expected = firingAngle * scale + Constants.DOWN_ANGLE_MAXIMUM;
		assertEquals("Firing angle does not match", expected, result, 0.0f);

		verify(barrel).getXRotation();
		verifyNoMoreInteractions(barrel);
		verifyZeroInteractions(self);
	}

	/**
	 * Test of getFacing method, of class PlayerEntity.
	 */
	@Test
	public void testGetFacing() {
		JMESpatial self = mock(JMESpatial.class);
		JMESpatial barrel = mock(JMESpatial.class);
		PlayerEntity instance = new PlayerEntity(self, barrel);
		instance.getFacing();
		verify(self).getYRotation();
		verifyNoMoreInteractions(self);
		verifyZeroInteractions(barrel);
	}
}
