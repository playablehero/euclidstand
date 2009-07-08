package euclidstand.engine;

import com.jme.app.SimplePassGame;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.Pass;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jmtan
 */
public class JMESimpleGame extends SimplePassGame {

	protected final List<JMEGameListener> listeners;

	public JMESimpleGame() {
		listeners = new LinkedList<JMEGameListener>();
		pManager = new BasicPassManager();
		stencilBits = 4;
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

	public void addRenderPass(Pass pass) {
		pManager.add(pass);
	}

	/**
	 * Renames BaseGame's start method as it is final
	 */
	public void run() {
		super.start();
	}

	@Override
	protected void simpleInitGame() {
		PointLight pl = new PointLight();
		pl.setEnabled(true);
		pl.setDiffuse(new ColorRGBA(.7f, .7f, .7f, 1.0f));
		pl.setAmbient(new ColorRGBA(.25f, .25f, .25f, .25f));
		pl.setLocation(new Vector3f(0, 500, 0));
		pl.setShadowCaster(true);

		lightState.detachAll();
		lightState.attach(pl);
		lightState.setGlobalAmbient(new ColorRGBA(1f, 1f, 1f, 1.0f));

		rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
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
