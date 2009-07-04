package euclidstand;

import com.jme.scene.Spatial;
import euclidstand.engine.JMEText;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author jmtan
 */
public class Text2DTest {

	/**
	 * Test of left method, of class Text2D.
	 */
	@Test
	public void testLeft() {
		int pixels = 20;
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, 0, 0);
		instance.left(pixels);
		verify(textSpatial).setX(pixels);
		verifyNoMoreInteractions(textSpatial);
	}

	/**
	 * Test of right method, of class Text2D.
	 */
	@Test
	public void testRight() {
		int width = 800;
		int pixels = 20;
		float spatialWidth = 20;
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, width, 0);
		when(textSpatial.getWidth()).thenReturn(spatialWidth);
		instance.right(pixels);
		verify(textSpatial).getWidth();
		verify(textSpatial).setX(width - spatialWidth - pixels);
		verifyNoMoreInteractions(textSpatial);
	}

	/**
	 * Test of bottom method, of class Text2D.
	 */
	@Test
	public void testBottom() {
		int pixels = 10;
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, 0, 0);
		instance.bottom(pixels);
		verify(textSpatial).setY(pixels);
		verifyNoMoreInteractions(textSpatial);
	}

	/**
	 * Test of top method, of class Text2D.
	 */
	@Test
	public void testTop() {
		int height = 600;
		int pixels = 10;
		float spatialHeight = 20;
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, 0, height);
		when(textSpatial.getHeight()).thenReturn(spatialHeight);
		instance.top(pixels);
		verify(textSpatial).getHeight();
		verify(textSpatial).setY(height - spatialHeight - pixels);
		verifyNoMoreInteractions(textSpatial);
	}

	/**
	 * Test of setScale method, of class Text2D.
	 */
	@Test
	public void testSetScale() {
		float scale = 2.0f;
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, 0, 0);
		instance.setScale(scale);
		verify(textSpatial).setLocalScale(scale);
		verifyNoMoreInteractions(textSpatial);
	}

	/**
	 * Test of setText method, of class Text2D.
	 */
	@Test
	public void testSetText() {
		String text = "test text";
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, 0, 0);
		instance.setText(text);
		verify(textSpatial).print(text);
		verifyNoMoreInteractions(textSpatial);
	}

	/**
	 * Test of getText method, of class Text2D.
	 */
	@Test
	public void testGetText() {
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, 0, 0);
		String expResult = "test text";
		when(textSpatial.getText()).thenReturn(new StringBuffer(expResult));
		String result = instance.getText();
		assertEquals(expResult, result);
		verify(textSpatial).getText();
		verifyNoMoreInteractions(textSpatial);
	}

	/**
	 * Test of getSpatial method, of class Text2D.
	 */
	@Test
	public void testGetSpatial() {
		JMEText textSpatial = mock(JMEText.class);
		Text2D instance = new Text2D(textSpatial, 0, 0);
		Spatial result = instance.getSpatial();
		assertEquals(textSpatial, result);
		verifyZeroInteractions(textSpatial);
	}

}