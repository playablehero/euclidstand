package euclidstand;

import java.util.Observer;
import java.util.Observable;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.renderer.Renderer;

public class PlayerObserver extends EntityObserver implements Observer {
	private static final Logger logger = Logger.getLogger(PlayerObserver.class.getName());

	Renderer renderer = null;
	Player player = null;
	PlayerInputHandler input = null;
	Node bulletNode = null;
	Node playerNode = null;
	int fired = 0;

	public PlayerObserver(LinkedList<Entity> entities, Renderer renderer) {
		super(entities);
		this.renderer = renderer;
		bulletNode = new Node("Bullets");
		playerNode = new Node("PlayerRelated");
	}

	public void initialise(Node sceneNode) {
		sceneNode.attachChild(playerNode);
		playerNode.attachChild(bulletNode);
		playerNode.attachChild(Factory.buildPlayer("Player", "Barrel", renderer));
		Spatial playerSpatial = playerNode.getChild("Player");
		Spatial barrelSpatial = playerNode.getChild("Barrel");
		player = new Player(playerSpatial, barrelSpatial);
		player.addObserver(this);
		input = new PlayerInputHandler(playerSpatial, barrelSpatial);
		entities.add(player);
	}

	public void updateInput(float interpolation) {
		input.update(interpolation);
		player.charge(input.isShoot());
	}

	public void update(Observable o, Object arg) {
		logger.info("Player state change: " + arg);
		Player.State state = (Player.State)arg;
		Player player = (Player)o;
		switch (state) {
			case FIRING:
				fired += 1;
				Spatial shellSpatial = Factory.buildShell("Shell"+fired, renderer, player.getBarrel());
				Shell shell = new Shell(shellSpatial, player.getFiringAngle(), 
						player.getVelocity(), player.getFacing());
				bulletNode.attachChild(shellSpatial);
				entities.add(shell);
				playerNode.updateRenderState();
				break;
			case DEAD:
				// TODO: Show game over
				playerNode.detachChild(player.getSelf());
				break;
		}
	}

	public PlayerInputHandler getInput() {
		return input;
	}

	public Player getPlayer() {
		return player;
	}
}
