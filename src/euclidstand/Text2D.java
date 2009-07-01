package euclidstand;

import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.renderer.Renderer;
import com.jme.renderer.ColorRGBA;

/**
 * Text class encapsulating details for a line of text on the screen
 */
public class Text2D {

	private final Text textSpatial;
	private String text = null;

	/**
	 * Constructor for Text2D
	 * @param textSpatial text model
	 * @param text to display
	 */
	private Text2D(Text textSpatial, String text) {
		this.textSpatial = textSpatial;
		this.text = text;
	}

	/**
	 * Factory method for Text2D
	 * @param name of the text instance
	 * @param text contents of the text instance
	 * @return Text2D instance
	 */
	public static Text2D getText2D(String name, String text) {
		Text textSpatial = Text.createDefaultTextLabel(name, text);
		return new Text2D(textSpatial, text);
	}

	/**
	 * Position text
	 * @param pixels from left edge of screen
	 */
	public void left(int pixels) {
		textSpatial.getLocalTranslation().x = pixels;
	}

	/**
	 * Position text
	 * @param renderer to get screen width from 
	 * @param pixels from right edge of screen
	 */
	public void right(Renderer renderer, int pixels) {
		textSpatial.getLocalTranslation().x = renderer.getWidth() - textSpatial.getWidth() - pixels;
	}

	/**
	 * Position text
	 * @param pixels from bottom edge of screen
	 */
	public void bottom(int pixels) {
		textSpatial.getLocalTranslation().y = pixels;
	}

	/**
	 * Position text
	 * @param renderer to get screen height from 
	 * @param pixels from top edge of screen
	 */
	public void top(Renderer renderer, int pixels) {
		textSpatial.getLocalTranslation().y = renderer.getHeight() - textSpatial.getHeight() - pixels;
	}

	/**
	 * Sets text colour
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @param a alpha
	 */
	public void setColour(float r, float g, float b, float a) {
		textSpatial.setTextColor(new ColorRGBA(r, g, b, a));
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
		if (text == null) {
			text = "";
		}
		this.text = text;
		textSpatial.print(text);
	}

	/**
	 * @return text being displayed
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return text model
	 */
	public Spatial getSpatial() {
		return textSpatial;
	}
}
