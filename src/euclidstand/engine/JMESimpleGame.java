package euclidstand.engine;

import com.jme.app.SimpleGame;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jmtan
 */
public class JMESimpleGame extends SimpleGame {

	protected final List<JMEGameListener> listeners;

	public JMESimpleGame() {
		listeners = new LinkedList<JMEGameListener>();
	}

	public void addListener(JMEGameListener listener) {
		listeners.add(listener);
	}

	public void removeListener(JMEGameListener listener) {
		listeners.remove(listener);
	}

	public void setTitle(String title) {
		display.setTitle(title);
	}

	public Renderer getRenderer() {
		return display.getRenderer();
	}

	public Camera getCamera() {
		return cam;
	}

	public int getWidth() {
		return display.getRenderer().getWidth();
	}

	public int getHeight() {
		return display.getRenderer().getHeight();
	}

	public void attachScene(JMENode sceneNode) {
		sceneNode.attachToParent(rootNode);
	}

	/**
	 * Renames BaseGame's start method as it is final
	 */
	public void run() {
		super.start();
	}

	@Override
	protected void simpleInitGame() {
		for (JMEGameListener listener : listeners) {
			listener.initGame();
		}
	}

	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();
		for (JMEGameListener listener : listeners) {
			listener.update(tpf);
		}
	}

	/**
	 * Replaces the default key handler defined in BaseSimpleGame
	 */
	@Override
	protected void updateInput() {
	}
}
