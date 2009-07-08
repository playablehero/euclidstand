package euclidstand;

import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author jmtan
 */
public class GameSceneUITest {

	/**
	 * Test of update method, of class GameSceneUI.
	 */
	@Test
	public void testUpdate() {
		float angle = 20f;
		float velocity = 30f;
		float facing = 50f;
		Text2D angleText = mock(Text2D.class);
		Text2D velocityText = mock(Text2D.class);
		Text2D facingText = mock(Text2D.class);
		PlayerEntity player = mock(PlayerEntity.class);
		when(player.getFacing()).thenReturn(facing);
		when(player.getFiringAngle()).thenReturn(angle);
		when(player.getVelocity()).thenReturn(velocity);

		GameSceneUI instance = new GameSceneUI(angleText, velocityText, facingText);
		instance.update(player);

		verify(angleText).setText("Angle: " + angle);
		verify(facingText).setText("Facing: " + facing);
		verify(velocityText).setText("Velocity: " + velocity);
		verify(player).getFacing();
		verify(player).getFiringAngle();
		verify(player).getVelocity();
		verifyNoMoreInteractions(angleText, velocityText, facingText, player);
	}
}
