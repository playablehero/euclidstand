package euclidstand;

import com.jme.scene.Spatial;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyNodeLookUpAction;
import com.jme.input.action.KeyNodeLookDownAction;
import com.jme.input.action.KeyNodeRotateLeftAction;
import com.jme.input.action.KeyNodeRotateRightAction;

import euclidstand.action.AimUpAction;
import euclidstand.action.AimDownAction;

public class PlayerInputHandler extends InputHandler {
	public PlayerInputHandler(Spatial base, Spatial barrel) {
		setKeyBindings();
		setActions(base, barrel);
	}

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
