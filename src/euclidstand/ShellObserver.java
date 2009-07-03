package euclidstand;

import com.jme.bounding.BoundingSphere;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jmex.effects.particles.ParticleMesh;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages projectile collisions and creates explosions accordingly
 */
public class ShellObserver extends EntityObserver implements Observer {

	private final Node searchNode;
	private final Renderer renderer;
	private final ShellCollision shellCollision;

	public ShellObserver(List<Entity> entitiesToAdd, ShellCollision shellCollision,
			Renderer renderer, Node searchNode) {
		super(entitiesToAdd);
		this.shellCollision = shellCollision;
		this.renderer = renderer;
		this.searchNode = searchNode;
	}

	/**
	 * Called when a shell collides.
	 * Finds enemies in the blast radius and damages them accordingly
	 * @param o shell entity
	 * @param arg unused
	 */
	public void update(Observable o, Object arg) {
		ShellEntity shell = (ShellEntity) o;
		ParticleMesh explosion = Factory.getFactory().buildSmallExplosion(
				"ShellExplosion", renderer, shell.getSelf());

		// Find enemies and hurt them
		float explosionRadius = 10f;
		Sphere sphere = new Sphere(null, shell.getSelf().getWorldTranslation(), 5, 5, explosionRadius);
		sphere.setModelBound(new BoundingSphere());
		sphere.updateModelBound();
		sphere.calculateCollisions(searchNode, shellCollision);
		shell.setExplosion(explosion);
		explosion.forceRespawn();
	}
}
