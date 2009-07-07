package euclidstand;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

import euclidstand.action.AimUpAction;
import euclidstand.action.AimDownAction;
import euclidstand.engine.JMEKeyNodeRotateLeftAction;
import euclidstand.engine.JMEKeyNodeRotateRightAction;
import euclidstand.engine.JMESpatial;

/**
 * Handles player input keys
 */
public class PlayerInputHandler extends InputHandler {

	private PlayerInputHandler() {
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

	private void setActions(JMESpatial base, JMESpatial barrel) {
		addAction(new AimUpAction(barrel, 1f, Constants.UP_INPUT_MAXIMUM), "aimUp", true);
		addAction(new AimDownAction(barrel, 1f, Constants.DOWN_INPUT_MAXIMUM), "aimDown", true);
		addAction(new JMEKeyNodeRotateLeftAction(base, 1f), "aimLeft", true);
		addAction(new JMEKeyNodeRotateRightAction(base, 1f), "aimRight", true);
	}

	public static class Factory {

		/**
		 * Factory method to create a handler
		 * @param base
		 * @param barrel
		 * @return an instance of PlayerInputHandler
		 */
		public PlayerInputHandler make(JMESpatial base, JMESpatial barrel) {
			PlayerInputHandler handler = new PlayerInputHandler();
			handler.setKeyBindings();
			handler.setActions(base, barrel);
			return handler;
		}
	}
}
