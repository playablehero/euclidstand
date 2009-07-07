package euclidstand;

import euclidstand.engine.JMEGameListener;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESimpleGame;

/**
 * Game class handling setup and game logic
 */
public class EuclidStand implements JMEGameListener {

	private final Builder builder;
	private final JMENode.Factory nodeFactory;
	private final GameScene scene;
	private final JMESimpleGame game;

	/**
	 * Game constructor
	 */
	public EuclidStand(Builder builder, JMENode.Factory nodeFactory, GameScene scene, JMESimpleGame game) {
		this.builder = builder;
		this.nodeFactory = nodeFactory;
		this.scene = scene;
		this.game = game;
	}

	@Override
	public void initGame() {
		game.setTitle("Euclid's Last Stand");
		builder.initialise(game.getRenderer());
		JMENode sceneNode = nodeFactory.make("Game Scene");
		game.attachScene(sceneNode);
		scene.create(builder, sceneNode, game.getCamera(), game.getWidth(), game.getHeight());
	}

	@Override
	public void update(float interpolation) {
		scene.update(interpolation);
	}

	public void start() {
		game.start();
	}

	/**
	 * Java main for launching the game
	 * @param args unused
	 */
	public static void main(String[] args) {
		Builder builder = new Builder();
		JMENode.Factory nodeFactory = new JMENode.Factory();
		GameScene scene = new GameScene();
		JMESimpleGame game = new JMESimpleGame();
		EuclidStand euclid = new EuclidStand(builder, nodeFactory, scene, game);
		game.addListener(euclid);
		euclid.start();
	}
}
