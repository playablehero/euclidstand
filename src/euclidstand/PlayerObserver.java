package euclidstand;

import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Logger;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.renderer.Renderer;

/**
 * Manages player objects and responds to player events
 */
public final class PlayerObserver extends EntityObserver implements Observer {
	private static final Logger logger = Logger.getLogger(PlayerObserver.class.getName());

	private final Renderer renderer;
	private final Node bulletNode;
	private final Node playerNode;
	private final PlayerEntity player;
	private final PlayerInputHandler input;
	private int fired = 0;

	private PlayerObserver(List<Entity> entitiesToAdd,
			Renderer renderer,
			Node bulletNode,
			Node playerNode,
			PlayerEntity player,
			PlayerInputHandler input) {
		super(entitiesToAdd);
		this.renderer = renderer;
		this.bulletNode = bulletNode;
		this.playerNode = playerNode;
		this.player = player;
		this.input = input;
	}

	/**
	 * Factory method for creating PlayerObserver instances
	 * @param entitiesToAdd list of entitiesToAdd
	 * @param renderer current renderer
	 * @param sceneNode for current scene
	 * @return instance of PlayerObserver
	 */
	public static PlayerObserver getObserver(
			List<Entity> entitiesToAdd,
			Renderer renderer,
			Node sceneNode) {
		Node bulletNode = new Node("Bullets");
		Node playerNode = new Node("PlayerRelated");
		sceneNode.attachChild(playerNode);
		playerNode.attachChild(bulletNode);
		playerNode.attachChild(Factory.buildPlayer("Player", "Barrel", renderer));
		Spatial playerSpatial = playerNode.getChild("Player");
		Spatial barrelSpatial = playerNode.getChild("Barrel");
		PlayerInputHandler input = PlayerInputHandler.getHandler(playerSpatial, barrelSpatial);
		PlayerEntity player = new PlayerEntity(playerSpatial, barrelSpatial);
		PlayerObserver observer = new PlayerObserver(
				entitiesToAdd, renderer, bulletNode, playerNode, player, input);
		player.addObserver(observer);
		entitiesToAdd.add(player);

		return observer;
	}

	/**
	 * Updates the input with current frame time
	 * @param interpolation time per frame
	 */
	public void updateInput(float interpolation) {
		input.update(interpolation);
		player.charge(input.isShoot());
	}

	/**
	 * Called whenever a player changes state
	 * @param o player being observed
	 * @param arg new player state
	 */
	public void update(Observable o, Object arg) {
		logger.info("Player state change: " + arg);
		PlayerEntity.State state = (PlayerEntity.State)arg;
		PlayerEntity localPlayer = (PlayerEntity)o;
		switch (state) {
			case FIRING:
				fired += 1;
				Spatial shellSpatial = Factory.buildShell("Shell"+fired, renderer, localPlayer.getBarrel());
				Shell shell = Shell.getShell(shellSpatial, 
						localPlayer.getFiringAngle(), localPlayer.getVelocity());
				bulletNode.attachChild(shellSpatial);
				entitiesToAdd.add(shell);
				playerNode.updateRenderState();
				break;
			case DEAD:
				// TODO: Show game over
				playerNode.detachChild(localPlayer.getSelf());
				// TODO: Apparently, enemies can still collide with dead player
				break;
		}
	}

	/**
	 * @return instance of PlayerInputHandler
	 */
	public PlayerInputHandler getInput() {
		return input;
	}

	/**
	 * @return instance of PlayerEntity
	 */
	public PlayerEntity getPlayer() {
		return player;
	}
}
