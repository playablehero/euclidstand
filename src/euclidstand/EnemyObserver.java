package euclidstand;

import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.logging.Logger;

import com.jmex.effects.particles.ParticleMesh;
import euclidstand.engine.JMENode;

// TODO: Initialise bad guy attributes (appearance, speed, damage)
/**
 * Initialises and observes the state of enemies in the game, creating new
 * ones when needed.
 */
public final class EnemyObserver extends EntityObserver implements Observer {

	private static final Logger logger =
			Logger.getLogger(EnemyObserver.class.getName());
	private final JMENode enemyNode;
	private final Builder builder;
	private final EnemyEntity.Factory enemyFactory;
	private Entity target = null;
	private int createdBaddies = 0;
	private int currentBaddies = 0;

	private EnemyObserver(List<Entity> entitiesToAdd,
			Entity target,
			JMENode enemyNode,
			Builder builder,
			EnemyEntity.Factory enemyFactory) {
		super(entitiesToAdd);
		this.target = target;
		this.enemyNode = enemyNode;
		this.builder = builder;
		this.enemyFactory = enemyFactory;
	}

	/**
	 * Factory method for EnemyObserver.
	 *
	 * Creates an initial wave of enemies
	 * @param entitiesToAdd the list of entitiesToAdd
	 * @param renderer current renderer
	 * @param target for new enemies
	 * @param sceneNode for current scene
	 * @return an instance of EnemyObserver
	 */
	public static EnemyObserver getObserver(
			List<Entity> entitiesToAdd,
			Entity target,
			Builder builder,
			JMENode enemyNode,
			EnemyEntity.Factory enemyFactory) {
		EnemyObserver observer =
				new EnemyObserver(entitiesToAdd, target, enemyNode, builder, enemyFactory);
		return observer;
	}

	public void createWave(int number) {
		for (int i = 0; i < number; i++) {
			createEnemy();
		}
	}

	private void createEnemy() {
		createdBaddies += 1;
		String name = "Badguy" + createdBaddies;
		EnemyEntity badguy = enemyFactory.make(builder.buildBaddie(name), target);
		entitiesToAdd.add(badguy);
		badguy.addObserver(this);
		enemyNode.attachChild(badguy.getSelf());
		enemyNode.updateRenderState();
		currentBaddies += 1;
	}

	public void update(Observable o, Object arg) {
		logger.info("Enemy died");
		EnemyEntity badguy = (EnemyEntity) o;
		ParticleMesh explosion = builder.buildSmallExplosion(
				badguy.getName() + "Death", badguy.getSelf());
		enemyNode.attachChild(explosion);
		enemyNode.updateRenderState();
		explosion.forceRespawn();
		currentBaddies -= 1;
		if (currentBaddies <= 0) {
			createWave(10);
		}
	}
}
