package euclidstand;

import com.jme.renderer.Camera;
import euclidstand.engine.JMEChaseCamera;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMEShadowedRenderPass;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMESphere;
import euclidstand.engine.JMETerrain;
import java.util.LinkedList;
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
	private final JMENode.Factory nodeFactory;
	private final PlayerEntity.Factory playerEntityFactory;
	private final PlayerInputHandler.Factory inputHandlerFactory;
	private final ShellEntity.Factory shellEntityFactory;
	private final EnemyEntity.Factory enemyFactory;
	private final GameSceneUI.Factory uiFactory;
	private final ShellCollision.Factory shellCollisionFactory;
	private JMENode sceneNode;
	private JMETerrain terrain;
	private JMEChaseCamera chasecam;
	private GameSceneUI ui;

	/**
	 * Game constructor
	 */
	public GameScene() {
		entities = new LinkedList<Entity>();
		entitiesToAdd = new LinkedList<Entity>();
		observers = new LinkedList<EntityObserver>();
		entitiesToRemove = new LinkedList<Entity>();
		nodeFactory = new JMENode.Factory();
		playerEntityFactory = new PlayerEntity.Factory();
		inputHandlerFactory = new PlayerInputHandler.Factory();
		shellEntityFactory = new ShellEntity.Factory();
		enemyFactory = new EnemyEntity.Factory();
		uiFactory = new GameSceneUI.Factory(new Text2D.Factory());
		shellCollisionFactory = new ShellCollision.Factory();
	}

	public void create(Builder builder, JMENode sceneNode, JMEShadowedRenderPass sPass, Camera cam, int width, int height) {
		this.sceneNode = sceneNode;

		logger.info("Building world");
		terrain = builder.buildTerrain("Terrain");
		sceneNode.attachChild(terrain);
		sceneNode.attachChild(builder.buildSky("Sky"));

		logger.info("Building entities");

		ShellCollision shellCollision = shellCollisionFactory.make(entities);
		JMENode explosionNode = nodeFactory.make("Explosions");
		sceneNode.attachChild(explosionNode);
		float explosionRadius = 10f;
		JMESphere sphere = new JMESphere(null, 5, 5, explosionRadius);
		sphere.setBoundsToSphere();
		ShellObserver shellObserver = new ShellObserver(entitiesToAdd,
				shellCollision, sceneNode, explosionNode, builder, sphere);
		observers.add(shellObserver);

		JMENode bulletNode = nodeFactory.make("Bullets");
		JMENode playerNode = nodeFactory.make("PlayerRelated");
		sPass.addSpatialToOcclude(bulletNode);
		sPass.addSpatialToOcclude(playerNode);
		sceneNode.attachChild(playerNode);
		playerNode.attachChild(bulletNode);
		playerNode.attachChild(builder.buildPlayer("Player", "Barrel"));
		JMESpatial playerSpatial = playerNode.getChild("Player");
		JMESpatial barrelSpatial = playerNode.getChild("Barrel");
		PlayerInputHandler inputHandler = inputHandlerFactory.make(playerSpatial, barrelSpatial);
		PlayerEntity player = playerEntityFactory.make(playerSpatial, barrelSpatial);

		PlayerObserver playerObserver = new PlayerObserver(entitiesToAdd, bulletNode, playerNode, player, inputHandler, shellObserver, builder, shellEntityFactory);
		player.addObserver(playerObserver);
		observers.add(playerObserver);
		entitiesToAdd.add(player);

		JMENode enemyNode = nodeFactory.make("Enemies");
		sPass.addSpatialToOcclude(enemyNode);
		EnemyObserver enemyObserver = EnemyObserver.getObserver(
				entitiesToAdd, player, builder, enemyNode, enemyFactory);
		enemyObserver.createWave(10);
		sceneNode.attachChild(enemyNode);
		observers.add(enemyObserver);

		logger.info("Initialising camera");

		chasecam = new JMEChaseCamera(cam, player.getSelf());
		chasecam.setEnableSpring(false);
		chasecam.setMouseXMultiplier(0.5f);
		chasecam.setMouseYMultiplier(0.1f);
		chasecam.setRotateTarget(false);

		logger.info("Creating GUI");
		JMENode uiNode = nodeFactory.make("UI");
		sceneNode.attachChild(uiNode);
		ui = uiFactory.make(uiNode, width, height);
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

		sceneNode.getChild("Sky").setLocalTranslation(chasecam.getLocation());

		logger.fine("Updating GUI");
		PlayerEntity player = getPlayerObserver().getPlayer();
		ui.update(player);
		getPlayerObserver().updateInput(interpolation);
	}

	private PlayerObserver getPlayerObserver() {
		for (EntityObserver o : observers) {
			if (o instanceof PlayerObserver) {
				return (PlayerObserver)o;
			}
		}
		return null;
	}
}
