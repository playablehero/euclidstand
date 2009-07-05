package euclidstand.action;

import com.jme.input.action.InputActionEvent;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import euclidstand.engine.JMESpatial;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author jmtan
 */
public class AimDownActionTest {

	/**
	 * Test of performAction method, of class AimDownAction.
	 */
	@Test
	public void testPerformActionWithinConstraint() {
		float constraint = 10f;
		float rotation = 9f;
		InputActionEvent evt = mock(InputActionEvent.class);
		JMESpatial spatial = mock(JMESpatial.class);
		when(spatial.getXRotation()).thenReturn(rotation);
		when(evt.getTime()).thenReturn(0f);
		when(spatial.getRotationMatrix()).thenReturn(new Matrix3f());
		AimDownAction instance = new AimDownAction(spatial, 0f, constraint);
		instance.performAction(evt);
		verify(spatial).getXRotation();
		verify(spatial).getXRotationAsVector(any(Vector3f.class));
		verify(evt).getTime();
		verify(spatial).getRotationMatrix();
		verify(spatial).setRotationMatrix(any(Matrix3f.class));
		verify(spatial).normaliseRotation();
		verifyNoMoreInteractions(spatial, evt);
	}

	@Test
	public void testPerformActionOutsideConstraint() {
		float constraint = 10f;
		float rotation = 11f;
		InputActionEvent evt = mock(InputActionEvent.class);
		JMESpatial spatial = mock(JMESpatial.class);
		when(spatial.getXRotation()).thenReturn(rotation);
		AimDownAction instance = new AimDownAction(spatial, 0f, constraint);
		instance.performAction(evt);
		verify(spatial).getXRotation();
		verifyNoMoreInteractions(spatial);
		verifyZeroInteractions(evt);
	}
}
