package euclidstand;

import com.jme.scene.Spatial;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.KeyNodeRotateLeftAction;
import com.jme.input.action.KeyNodeRotateRightAction;

import euclidstand.action.AimUpAction;
import euclidstand.action.AimDownAction;

/**
 * Handles player input keys
 */
public final class PlayerInputHandler extends InputHandler {

	private PlayerInputHandler() {
	}

	/**
	 * Factory method to create a handler
	 * @param base
	 * @param barrel
	 * @return an instance of PlayerInputHandler
	 */
	public static PlayerInputHandler getHandler(Spatial base, Spatial barrel) {
		PlayerInputHandler handler = new PlayerInputHandler();
		handler.setKeyBindings();
		handler.setActions(base, barrel);
		return handler;
	}

	/**
	 * Whether the shoot command is being held
	 * @return true if held, false otherwise
	 */
	public boolean isShoot() {
		return KeyBindingManager.getKeyBindingManager().isValidCommand("shoot", true);
	}

	private void setKeyBindings() {
		KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();
		keyboard.set("aimUp", KeyInput.KEY_W);
		keyboard.set("aimDown", KeyInput.KEY_S);
		keyboard.set("aimLeft", KeyInput.KEY_A);
		keyboard.set("aimRight", KeyInput.KEY_D);
		keyboard.set("shoot", KeyInput.KEY_SPACE);
	}

	private void setActions(Spatial base, Spatial barrel) {
		addAction(new AimUpAction(barrel, 1f, -0.33009472f), "aimUp", true);
		addAction(new AimDownAction(barrel, 1f, 0.1352511f), "aimDown", true);
		addAction(new KeyNodeRotateLeftAction(base, 1f), "aimLeft", true);
		addAction(new KeyNodeRotateRightAction(base, 1f), "aimRight", true);
	}
}
