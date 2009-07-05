package euclidstand;

import com.jme.bounding.BoundingSphere;
import com.jmex.effects.particles.ParticleMesh;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMESphere;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages projectile collisions and creates explosions accordingly
 */
public class ShellObserver extends EntityObserver implements Observer {

	private final JMENode sceneNode;
	private final JMENode explosionNode;
	private final ShellCollision shellCollision;

	public ShellObserver(List<Entity> entitiesToAdd, ShellCollision shellCollision,
			JMENode sceneNode, JMENode explosionNode) {
		super(entitiesToAdd);
		this.shellCollision = shellCollision;
		this.sceneNode = sceneNode;
		this.explosionNode = explosionNode;
	}

	/**
	 * Called when a shell collides.
	 * Finds enemies in the blast radius and damages them accordingly
	 * @param o shell entity
	 * @param arg unused
	 */
	public void update(Observable o, Object arg) {
		ShellEntity shell = (ShellEntity) o;
		ParticleMesh explosion = Builder.getInstance().buildSmallExplosion(
				"ShellExplosion", shell.getSelf());

		// Find enemies and hurt them
		float explosionRadius = 10f;
		JMESphere sphere = new JMESphere(null, shell.getSelf(), 5, 5, explosionRadius);
		sphere.setModelBound(new BoundingSphere());
		sphere.updateModelBound();
		sphere.calculateCollisions(sceneNode, shellCollision);
		explosionNode.attachChild(new JMESpatial(explosion));
		explosion.updateRenderState();
		explosion.forceRespawn();
	}
}
