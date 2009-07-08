package euclidstand;

import com.google.inject.Inject;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Logger;

import com.jmex.effects.particles.ParticleMesh;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMEShadowedRenderPass;
import euclidstand.engine.JMESpatial;

/**
 * Manages player objects and responds to player events
 */
public class PlayerObserver extends EntityObserver implements Observer {

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

	public static class Factory {

		private final JMENode.Factory nodeFactory;
		private final PlayerInputHandler.Factory inputHandlerFactory;
		private final PlayerEntity.Factory playerEntityFactory;
		private final ShellEntity.Factory shellEntityFactory;

		@Inject
		public Factory(JMENode.Factory nodeFactory, PlayerInputHandler.Factory inputHandlerFactory, PlayerEntity.Factory playerEntityFactory, ShellEntity.Factory shellEntityFactory) {
			this.nodeFactory = nodeFactory;
			this.inputHandlerFactory = inputHandlerFactory;
			this.playerEntityFactory = playerEntityFactory;
			this.shellEntityFactory = shellEntityFactory;
		}

		public PlayerObserver make(Builder builder, JMEShadowedRenderPass sPass, JMENode sceneNode, ShellObserver shellObserver, List<Entity> entitiesToAdd) {
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
			entitiesToAdd.add(player);
			return playerObserver;
		}
	}
}
