package euclidstand;

import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Logger;

import com.jmex.effects.particles.ParticleMesh;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESpatial;

/**
 * Manages player objects and responds to player events
 */
public final class PlayerObserver extends EntityObserver implements Observer {

	private static final Logger logger = Logger.getLogger(PlayerObserver.class.getName());
	private final JMENode bulletNode;
	private final JMENode playerNode;
	private final PlayerEntity player;
	private final PlayerInputHandler input;
	private final ShellObserver shellObserver;
	private final Builder builder;
	private final ShellEntity.Factory shellEntityFactory;
	private int fired = 0;

	public PlayerObserver(List<Entity> entitiesToAdd,
			JMENode bulletNode,
			JMENode playerNode,
			PlayerEntity player,
			PlayerInputHandler input,
			ShellObserver shellObserver,
			Builder builder,
			ShellEntity.Factory shellEntityFactory) {
		super(entitiesToAdd);
		this.bulletNode = bulletNode;
		this.playerNode = playerNode;
		this.player = player;
		this.input = input;
		this.shellObserver = shellObserver;
		this.builder = builder;
		this.shellEntityFactory = shellEntityFactory;
	}

	/**
	 * Factory method for creating PlayerObserver instances
	 * @param entitiesToAdd list of entitiesToAdd
	 * @param renderer current renderer
	 * @param sceneNode for current scene
	 * @return instance of PlayerObserver
	 */
	/*public static PlayerObserver getObserver(
			List<Entity> entitiesToAdd,
			JMENode sceneNode,
			ShellObserver shellObserver,
			Builder builder,
			JMENode.Factory nodeFactory,
			PlayerEntity.Factory playerEntityFactory,
			PlayerInputHandler.Factory inputHandlerFactory,
			ShellEntity.Factory shellEntityFactory) {
		JMENode bulletNode = nodeFactory.make("Bullets");
		JMENode playerNode = nodeFactory.make("PlayerRelated");
		sceneNode.attachChild(playerNode);
		playerNode.attachChild(bulletNode);
		playerNode.attachChild(builder.buildPlayer("Player", "Barrel"));
		JMESpatial playerSpatial = playerNode.getChild("Player");
		JMESpatial barrelSpatial = playerNode.getChild("Barrel");

		PlayerInputHandler input = inputHandlerFactory.make(playerSpatial, barrelSpatial);
		PlayerEntity player = playerEntityFactory.make(playerSpatial, barrelSpatial);
		PlayerObserver observer = new PlayerObserver(
				entitiesToAdd, bulletNode, playerNode, player, input, 
				shellObserver, builder, shellEntityFactory);
		player.addObserver(observer);
		entitiesToAdd.add(player);

		return observer;
	}*/

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
		PlayerEntity.State state = (PlayerEntity.State) arg;
		PlayerEntity localPlayer = (PlayerEntity) o;
		switch (state) {
			case FIRING:
				fired += 1;
				JMESpatial shellSpatial = builder.buildShell(
						"Shell" + fired, localPlayer.getBarrel());
				ShellEntity shell = shellEntityFactory.make(shellSpatial,
						localPlayer.getFiringAngle(), localPlayer.getVelocity());
				bulletNode.attachChild(shellSpatial);
				shell.addObserver(shellObserver);
				entitiesToAdd.add(shell);
				playerNode.updateRenderState();
				break;
			case DEAD:
				ParticleMesh explosion = builder.buildBigExplosion(
						"PlayerDeath", localPlayer.getSelf());
				playerNode.attachChild(explosion);
				playerNode.updateRenderState();
				explosion.forceRespawn();
				break;
		}
	}

	/**
	 * @return instance of PlayerEntity
	 */
	public PlayerEntity getPlayer() {
		return player;
	}
}
