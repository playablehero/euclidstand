package euclidstand;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import euclidstand.engine.JMEGameListener;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMEShadowedRenderPass;
import euclidstand.engine.JMESimpleGame;
import java.util.LinkedList;
import java.util.List;

/**
 * Game class handling setup and game logic
 */
public class EuclidStand implements JMEGameListener {

	private final Builder builder;
	private final JMENode.Factory nodeFactory;
	private final GameScene.Factory sceneFactory;
	private final JMESimpleGame game;
	private final JMEShadowedRenderPass renderPass;
	protected GameScene scene;

	/**
	 * Game constructor
	 */
	public EuclidStand(Builder builder,
			JMENode.Factory nodeFactory,
			GameScene.Factory sceneFactory,
			JMEShadowedRenderPass renderPass,
			JMESimpleGame game) {
		this.builder = builder;
		this.nodeFactory = nodeFactory;
		this.sceneFactory = sceneFactory;
		this.renderPass = renderPass;
		this.game = game;
	}

	public void initGame() {
		game.setTitle("Euclid's Last Stand");
		builder.initialise(game.getRenderer());
		JMENode sceneNode = nodeFactory.make("Game Scene");
		game.attachScene(sceneNode);
		renderPass.addSpatialToRender(sceneNode);
		game.addRenderPass(renderPass);
		scene = sceneFactory.make(builder, sceneNode, renderPass, game.getCamera(), game.getWidth(), game.getHeight());
	}

	public void update(float interpolation) {
		scene.update(interpolation);
	}

	public void run() {
		game.run();
	}

	/**
	 * Java main for launching the game
	 * @param args unused
	 */
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new EuclidModule());
		JMESimpleGame game = injector.getInstance(JMESimpleGame.class);
		EuclidStand.Factory factory = injector.getInstance(EuclidStand.Factory.class);
		factory.make(game).run();
	}

	public static class Factory {

		private final Builder builder;
		private final JMENode.Factory nodeFactory;
		private final GameScene.Factory sceneFactory;
		private final JMEShadowedRenderPass renderPass;

		@Inject
		public Factory(Builder builder, JMENode.Factory nodeFactory, GameScene.Factory sceneFactory, JMEShadowedRenderPass renderPass) {
			this.builder = builder;
			this.nodeFactory = nodeFactory;
			this.sceneFactory = sceneFactory;
			this.renderPass = renderPass;
		}

		public EuclidStand make(JMESimpleGame game) {
			EuclidStand euclidStand = new EuclidStand(builder, nodeFactory, sceneFactory, renderPass, game);
			game.addListener(euclidStand);
			return euclidStand;
		}
	}

	public static class EuclidModule extends AbstractModule {

		@Override
		protected void configure() {
			TypeLiteral<List<Entity>> eList = new TypeLiteral<List<Entity>>() {};
			TypeLiteral<LinkedList<Entity>> eLinkedList = new TypeLiteral<LinkedList<Entity>>() {};
			TypeLiteral<List<EntityObserver>> oList = new TypeLiteral<List<EntityObserver>>() {};
			TypeLiteral<LinkedList<EntityObserver>> oLinkedList = new TypeLiteral<LinkedList<EntityObserver>>() {};
			bind(eList).to(eLinkedList);
			bind(oList).to(oLinkedList);
		}
	}
}
