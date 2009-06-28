package euclidstand;

import java.util.Observer;
import java.util.Observable;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.renderer.Renderer;

// TODO: Initialise bad guy attributes (appearance, speed, damage)

public class EnemyObserver extends EntityObserver implements Observer {
	private static final Logger logger = Logger.getLogger(PlayerObserver.class.getName());

	Renderer renderer = null;
	Node enemyNode = null;
	Entity target = null;
	int createdBaddies = 0;
	int currentBaddies = 0;

	public EnemyObserver(LinkedList<Entity> entities, Renderer renderer, Entity target) {
		super(entities);
		this.renderer = renderer;
		this.target = target;
		enemyNode = new Node("Enemies");
	}

	public void initialise(Node sceneNode) {
		sceneNode.attachChild(enemyNode);
		for (int i=0; i<50; i++) {
			createdBaddies += 1;
			String name = "Badguy"+createdBaddies;
			BadGuy badguy = new BadGuy(Factory.buildBaddie(name, renderer), target);
			entities.add(badguy);
			badguy.addObserver(this);
			enemyNode.attachChild(badguy.getSelf());
			currentBaddies += 1;
		}
	}

	public void update(Observable o, Object arg) {
		logger.info("Enemy died");
		currentBaddies -= 1;
		BadGuy badguy = (BadGuy)o;
		enemyNode.detachChild(badguy.getSelf());
		// TODO: Create new enemies
	}
}
