package euclidstand;

import java.util.logging.Logger;
import java.util.LinkedList;
import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.input.KeyInput;
import com.jme.input.KeyBindingManager;
import com.acarter.scenemonitor.SceneMonitor;
import euclidstand.engine.JMEChaseCamera;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMESphere;
import euclidstand.engine.JMETerrain;
import java.util.List;
import java.util.Random;


/**
 * Game class handling setup and game logic
 */
public class EuclidStand extends SimpleGame {

	private static final Logger logger =
			Logger.getLogger(EuclidStand.class.getName());
	private final List<Entity> entities;
	private final List<Entity> entitiesToAdd;
	private final List<EntityObserver> observers;
	private final List<Entity> entitiesToRemove;
	private final JMENode.Factory nodeFactory;
	private final PlayerEntity.Factory playerEntityFactory;
	private final PlayerInputHandler.Factory inputHandlerFactory;
	private final ShellEntity.Factory shellEntityFactory;
	private final EnemyEntity.Factory enemyFactory;
	private JMENode sceneNode = null;
	private JMEChaseCamera chasecam = null;
	private JMETerrain terrain = null;
	private Text2D angleText = null;
	private Text2D velocityText = null;
	private Text2D facingText = null;
	private boolean showDebug = false;

	/**
	 * Game constructor
	 */
	public EuclidStand() {
		entities = new LinkedList<Entity>();
		entitiesToAdd = new LinkedList<Entity>();
		observers = new LinkedList<EntityObserver>();
		entitiesToRemove = new LinkedList<Entity>();
		nodeFactory = new JMENode.Factory();
		playerEntityFactory = new PlayerEntity.Factory();
		inputHandlerFactory = new PlayerInputHandler.Factory();
		shellEntityFactory = new ShellEntity.Factory();
		enemyFactory = new EnemyEntity.Factory();
	}

	/**
	 * Initialises the game scene.
	 * <ol>
	 * <li>Scene node</li>
	 * <li>Terrain</li>
	 * <li>Sky</li>
	 * <li>Game observers/entities</li>
	 * <li>Chase Camera</li>
	 * <li>UI</li>
	 * </ol>
	 */
	@Override
	protected void simpleInitGame() {
		display.setTitle("Euclid's Last Stand");
		Builder.setInstance(new Builder(new Random(), display.getRenderer()));
		Builder builder = Builder.getInstance();
		sceneNode = nodeFactory.make("Game Scene");
		sceneNode.attachToParent(rootNode);

		logger.info("Building world");
		terrain = builder.buildTerrain("Terrain");
		sceneNode.attachChild(terrain);
		sceneNode.attachChild(builder.buildSky("Sky"));

		logger.info("Building entities");

		ShellCollision shellCollision = new ShellCollision(entities);
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
		int width = display.getRenderer().getWidth();
		int height = display.getRenderer().getHeight();
		angleText = Text2D.getText2D("Angle", "", width, height);
		angleText.top(10);
		sceneNode.attachChild(angleText.getSpatial());
		velocityText = Text2D.getText2D("Velocity", "", width, height);
		velocityText.top(30);
		sceneNode.attachChild(velocityText.getSpatial());
		facingText = Text2D.getText2D("Facing", "", width, height);
		facingText.top(50);
		sceneNode.attachChild(facingText.getSpatial());

		SceneMonitor.getMonitor().registerNode(rootNode, "Root Node");
		KeyBindingManager.getKeyBindingManager().set("toggle_debug", KeyInput.KEY_U);
		logger.info("SceneMonitor is available");
	}

	/**
	 * Replaces the default key handler defined in BaseSimpleGame
	 */
	@Override
	protected void updateInput() {
		getPlayerObserver().updateInput(tpf);
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
	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();
		SceneMonitor.getMonitor().updateViewer(tpf);

		logger.fine("Updating camera");
		float interpolation = tpf;
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
		Vector3f camLoc = cam.getLocation();
		float minCamHeight = terrain.getHeightFromWorld(camLoc) + 2;
		if (camLoc.y < minCamHeight) {
			cam.getLocation().y = minCamHeight;
			cam.update();
		}

		sceneNode.getChild("Sky").setLocalTranslation(cam.getLocation());

		logger.fine("Updating GUI");
		PlayerEntity player = getPlayerObserver().getPlayer();
		angleText.setText("Angle: " + player.getFiringAngle());
		velocityText.setText("Velocity: " + player.getVelocity());
		facingText.setText("Facing: " + player.getFacing());

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"toggle_debug", false)) {
			logger.fine("Toggling SceneMonitor");
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
		logger.info("Cleaning up");
		super.cleanup();
		SceneMonitor.getMonitor().cleanup();
	}

	private PlayerObserver getPlayerObserver() {
		for (EntityObserver o : observers) {
			if (o instanceof PlayerObserver) {
				return (PlayerObserver)o;
			}
		}
		return null;
	}

	/**
	 * Java main for launching the game
	 * @param args unused
	 */
	public static void main(String[] args) {
		new EuclidStand().start();
	}
}
