package euclidstand.engine;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

/**
 *
 * @author jmtan
 */
public class JMESimpleGameDebug extends JMESimpleGame {

	private boolean showDebug = false;

	@Override
	protected void simpleInitGame() {
		super.simpleInitGame();
		SceneMonitor.getMonitor().registerNode(rootNode, "Root Node");
		KeyBindingManager.getKeyBindingManager().set("toggle_debug", KeyInput.KEY_U);
	}

	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();
		SceneMonitor.getMonitor().updateViewer(tpf);
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"toggle_debug", false)) {
			showDebug = !showDebug;
			SceneMonitor.getMonitor().showViewer(showDebug);
		}
	}

	@Override
	protected void simpleRender() {
		super.simpleRender();
		SceneMonitor.getMonitor().renderViewer(display.getRenderer());
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		SceneMonitor.getMonitor().cleanup();
	}

}
