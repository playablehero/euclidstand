package euclidstand;

import java.util.logging.Logger;
import java.util.LinkedList;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.Node;
import com.jme.util.Timer;
import com.jme.input.ChaseCamera;
import com.jme.input.KeyInput;
import com.jme.input.KeyBindingManager;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jmex.terrain.TerrainBlock;

import com.acarter.scenemonitor.SceneMonitor;

import jmetest.TestChooser;

/**
 * Game class handling setup and game logic
 */
public class EuclidStand extends SimpleGame {
	private static final Logger logger = Logger.getLogger(EuclidStand.class
			.getName());

	private Node sceneNode = null;
	private Camera firstperson = null;
	private ChaseCamera chasecam = null;
	private LinkedList<Entity> entities = null;
	private TerrainBlock terrain = null;
	private Text2D angleText = null;
	private Text2D velocityText = null;
	private Text2D facingText = null;
	private PlayerObserver playerObserver = null;
	private EnemyObserver enemyObserver = null;
	private boolean showDebug = false;

	/**
	 * Game constructor
	 */
	public EuclidStand() {
		entities = new LinkedList<Entity>();
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

		sceneNode = new Node("Game Scene");
		rootNode.attachChild(sceneNode);

		terrain = Factory.buildTerrain("Terrain", display.getRenderer());
		sceneNode.attachChild(terrain);
		sceneNode.attachChild(Factory.buildSky("Sky"));
		//sceneNode.setLightCombineMode(Spatial.LightCombineMode.Off);

		playerObserver = new PlayerObserver(entities, display.getRenderer());
		playerObserver.initialise(sceneNode);
		Player player = playerObserver.getPlayer();

		enemyObserver = new EnemyObserver(entities, display.getRenderer(), player);
		enemyObserver.initialise(sceneNode);

		chasecam = new ChaseCamera(cam, player.getSelf());
		chasecam.setEnableSpring(false);
		chasecam.getMouseLook().setMouseXMultiplier(0.5f);
		chasecam.getMouseLook().setMouseYMultiplier(0.1f);
		chasecam.getMouseLook().setRotateTarget(false);

		angleText = new Text2D("Angle", "");
		angleText.top(display.getRenderer(), 10);
		sceneNode.attachChild(angleText.getSpatial());
		velocityText = new Text2D("Velocity", "");
		velocityText.top(display.getRenderer(), 30);
		sceneNode.attachChild(velocityText.getSpatial());
		facingText = new Text2D("Facing", "");
		facingText.top(display.getRenderer(), 50);
		sceneNode.attachChild(facingText.getSpatial());

        KeyBindingManager.getKeyBindingManager().set( "toggle_debug", KeyInput.KEY_U );
		SceneMonitor.getMonitor().registerNode(rootNode, "Root Node");
	}

	/**
	 * Replaces the default key handler defined in BaseSimpleGame
	 */
	@Override
	protected void updateInput() {
		playerObserver.updateInput(tpf);
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
		float interpolation = tpf;
		chasecam.update(interpolation);

		LinkedList<Entity> entitiesToRemove = new LinkedList<Entity>();
		for (Entity e : entities) {
			e.updateTerrain(terrain);
			e.update(interpolation);
			if (e.isRemove())
				entitiesToRemove.add(e);
		}

		for (Entity e : entitiesToRemove)
			entities.remove(e);

		Vector3f camLoc = cam.getLocation();
		float minCamHeight = terrain.getHeightFromWorld(camLoc) + 2;
		if (camLoc.y  < minCamHeight) {
			cam.getLocation().y = minCamHeight;
			cam.update();
		}

		sceneNode.getChild("Sky").setLocalTranslation(cam.getLocation());

		Player player = playerObserver.getPlayer();
		angleText.setText("Angle: " + player.getFiringAngle());
		velocityText.setText("Velocity: " + player.getVelocity());
		facingText.setText("Facing: " + player.getFacing());

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

	/**
	 * Java main for launching the game
	 * @param args unused
	 */
	public static void main(String[] args) {
		new EuclidStand().start();
	}

}
