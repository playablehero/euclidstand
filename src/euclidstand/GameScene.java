package euclidstand;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.jme.renderer.Camera;
import euclidstand.engine.JMEChaseCamera;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMEShadowedRenderPass;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMETerrain;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author jmtan
 */
public class GameScene {

	private static final Logger logger =
			Logger.getLogger(GameScene.class.getName());
	private final List<Entity> entities;
	private final List<Entity> entitiesToAdd;
	private final List<EntityObserver> observers;
	private final List<Entity> entitiesToRemove;
	private final JMENode sceneNode;
	private final JMETerrain terrain;
	private final JMESpatial sky;
	private final JMEChaseCamera chasecam;
	private final GameSceneUI ui;

	public GameScene(List<Entity> entities, List<Entity> entitiesToAdd, List<EntityObserver> observers, List<Entity> entitiesToRemove, JMENode sceneNode, JMETerrain terrain, JMESpatial sky, JMEChaseCamera chasecam, GameSceneUI ui) {
		this.entities = entities;
		this.entitiesToAdd = entitiesToAdd;
		this.observers = observers;
		this.entitiesToRemove = entitiesToRemove;
		this.sceneNode = sceneNode;
		this.sky = sky;
		this.terrain = terrain;
		this.chasecam = chasecam;
		this.ui = ui;
	}

	/**
	 * Updates the game logic.
	 * Responsibilities include:
	 * <ol>
	 * <li>Keeping entities level with the terrain</li>
	 * <li>Removing marked entities</li>
	 * <li>Keeping camera above terrain</li>
	 * <li>Keeping sky around player</li>
	 * <li>Update UI</li>
	 * </ol>
	 */
	public void update(float interpolation) {
		logger.fine("Updating camera");
		chasecam.update(interpolation);

		logger.fine("Updating entities");
		for (Entity e : entitiesToAdd) {
			entities.add(e);
		}
		entitiesToAdd.clear();

		for (Entity e : entities) {
			e.updateTerrain(terrain);
			e.update(interpolation);
			if (e.isRemove()) {
				entitiesToRemove.add(e);
			}
		}

		for (Entity e : entitiesToRemove) {
			e.remove();
			entities.remove(e);
		}
		entitiesToRemove.clear();

		logger.fine("Updating locations");
		chasecam.updateTerrain(terrain);

		sky.setLocalTranslation(chasecam.getLocation());

		logger.fine("Updating GUI");
		PlayerEntity player = getPlayerObserver().getPlayer();
		ui.update(player);
		getPlayerObserver().updateInput(interpolation);
	}

	private PlayerObserver getPlayerObserver() {
		for (EntityObserver o : observers) {
			if (o instanceof PlayerObserver) {
				return (PlayerObserver) o;
			}
		}
		return null;
	}

	public static class Factory {

		private final Provider<List<Entity>> entityListFactory;
		private final Provider<List<EntityObserver>> observerListFactory;
		private final JMENode.Factory nodeFactory;
		private final GameSceneUI.Factory uiFactory;
		private final ShellCollision.Factory shellCollisionFactory;
		private final ShellObserver.Factory shellObserverFactory;
		private final PlayerObserver.Factory playerObserverFactory;
		private final EnemyObserver.Factory enemyObserverFactory;
		private final JMEChaseCamera.Factory cameraFactory;

		@Inject
		public Factory(Provider<List<Entity>> entityListFactory, Provider<List<EntityObserver>> observerListFactory, JMENode.Factory nodeFactory, GameSceneUI.Factory uiFactory, ShellCollision.Factory shellCollisionFactory, ShellObserver.Factory shellObserverFactory, PlayerObserver.Factory playerObserverFactory, EnemyObserver.Factory enemyObserverFactory, JMEChaseCamera.Factory cameraFactory) {
			this.entityListFactory = entityListFactory;
			this.observerListFactory = observerListFactory;
			this.nodeFactory = nodeFactory;
			this.uiFactory = uiFactory;
			this.shellCollisionFactory = shellCollisionFactory;
			this.shellObserverFactory = shellObserverFactory;
			this.playerObserverFactory = playerObserverFactory;
			this.enemyObserverFactory = enemyObserverFactory;
			this.cameraFactory = cameraFactory;
		}

		public GameScene make(Builder builder, JMENode sceneNode, JMEShadowedRenderPass sPass, Camera cam, int width, int height) {
			List<Entity> entities = entityListFactory.get();
			List<Entity> entitiesToAdd = entityListFactory.get();
			List<Entity> entitiesToRemove = entityListFactory.get();
			List<EntityObserver> observers = observerListFactory.get();

			logger.info("Building world");
			JMETerrain terrain = builder.buildTerrain("Terrain");
			sceneNode.attachChild(terrain);
			JMESpatial sky = builder.buildSky("Sky");
			sceneNode.attachChild(sky);

			logger.info("Building entities");

			ShellCollision shellCollision = shellCollisionFactory.make(entities);
			ShellObserver shellObserver = shellObserverFactory.make(builder, sceneNode, shellCollision, entitiesToAdd);
			PlayerObserver playerObserver = playerObserverFactory.make(builder, sPass, sceneNode, shellObserver, entitiesToAdd);
			PlayerEntity player = playerObserver.getPlayer();
			EnemyObserver enemyObserver = enemyObserverFactory.make(sPass, entitiesToAdd, player, builder, sceneNode);

			observers.add(shellObserver);
			observers.add(playerObserver);
			observers.add(enemyObserver);

			logger.info("Initialising camera");

			JMEChaseCamera chasecam = cameraFactory.make(cam, player.getSelf());
			chasecam.setEnableSpring(false);
			chasecam.setMouseXMultiplier(0.5f);
			chasecam.setMouseYMultiplier(0.1f);
			chasecam.setRotateTarget(false);

			logger.info("Creating GUI");
			JMENode uiNode = nodeFactory.make("UI");
			sceneNode.attachChild(uiNode);
			GameSceneUI ui = uiFactory.make(uiNode, width, height);
			return new GameScene(entities, entitiesToAdd, observers, entitiesToRemove, sceneNode, terrain, sky, chasecam, ui);
		}
	}
}
