package euclidstand;

import euclidstand.engine.JMENode;

/**
 *
 * @author jmtan
 */
public class GameSceneUI {

	private final Text2D angleText;
	private final Text2D velocityText;
	private final Text2D facingText;

	public GameSceneUI(Text2D angleText, Text2D velocityText, Text2D facingText) {
		this.angleText = angleText;
		this.velocityText = velocityText;
		this.facingText = facingText;
	}

	public void update(PlayerEntity player) {
		angleText.setText("Angle: " + player.getFiringAngle());
		velocityText.setText("Velocity: " + player.getVelocity());
		facingText.setText("Facing: " + player.getFacing());
	}

	public static class Factory {

		private final Text2D.Factory textFactory;

		public Factory(Text2D.Factory textFactory) {
			this.textFactory = textFactory;
		}

		public GameSceneUI make(JMENode uiNode, int width, int height) {
			Text2D angleText = textFactory.make("Angle", "", width, height);
			angleText.top(10);
			uiNode.attachChild(angleText.getSpatial());
			Text2D velocityText = textFactory.make("Velocity", "", width, height);
			velocityText.top(30);
			uiNode.attachChild(velocityText.getSpatial());
			Text2D facingText = textFactory.make("Facing", "", width, height);
			facingText.top(50);
			uiNode.attachChild(facingText.getSpatial());
			return new GameSceneUI(angleText, velocityText, facingText);
		}
	}
}
