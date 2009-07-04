package euclidstand;

import com.jme.scene.Spatial;
import euclidstand.engine.JMEText;

/**
 * Text class encapsulating details for a line of text on the screen
 */
public class Text2D {

	private final JMEText textSpatial;
	private final int screenWidth;
	private final int screenHeight;

	/**
	 * Constructor for Text2D
	 * @param textSpatial text model
	 * @param text to display
	 */
	public Text2D(JMEText textSpatial, int screenWidth, int screenHeight) {
		this.textSpatial = textSpatial;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	/**
	 * Factory method for Text2D
	 * @param name of the text instance
	 * @param text contents of the text instance
	 * @return Text2D instance
	 */
	public static Text2D getText2D(String name, String text, int screenWidth, int screenHeight) {
		JMEText textSpatial = new JMEText();
		textSpatial.setName(name);
		textSpatial.print(text);
		textSpatial.setDefaultCulling();
		textSpatial.setDefaultTexture();
		textSpatial.setDefaultFontBlend();
		return new Text2D(textSpatial, screenWidth, screenHeight);
	}

	/**
	 * Position text
	 * @param pixels from left edge of screen
	 */
	public void left(int pixels) {
		textSpatial.setX(pixels);
	}

	/**
	 * Position text
	 * @param renderer to get screen width from 
	 * @param pixels from right edge of screen
	 */
	public void right(int pixels) {
		textSpatial.setX(screenWidth - textSpatial.getWidth() - pixels);
	}

	/**
	 * Position text
	 * @param pixels from bottom edge of screen
	 */
	public void bottom(int pixels) {
		textSpatial.setY(pixels);
	}

	/**
	 * Position text
	 * @param renderer to get screen height from 
	 * @param pixels from top edge of screen
	 */
	public void top(int pixels) {
		textSpatial.setY(screenHeight - textSpatial.getHeight() - pixels);
	}

	/**
	 * @param scale of the text
	 */
	public void setScale(float scale) {
		textSpatial.setLocalScale(scale);
	}

	/**
	 * @param text to display
	 */
	public void setText(String text) {
		textSpatial.print(text);
	}

	/**
	 * @return text being displayed
	 */
	public String getText() {
		return textSpatial.getText().toString();
	}

	/**
	 * @return text model
	 */
	public Spatial getSpatial() {
		return textSpatial;
	}
}
