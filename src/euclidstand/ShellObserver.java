package euclidstand;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jmex.effects.particles.ParticleMesh;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages projectile collisions and creates explosions accordingly
 */
public class ShellObserver extends EntityObserver implements Observer {

	private final Node explosionNode;
	private final Renderer renderer;

	private ShellObserver(List<Entity> entitiesToAdd, Renderer renderer, Node explosionNode) {
		super(entitiesToAdd);
		this.renderer = renderer;
		this.explosionNode = explosionNode;
	}

	public static ShellObserver getObserver(
			List<Entity> entitiesToAdd,
			Renderer renderer,
			Node explosionNode) {
		return new ShellObserver(entitiesToAdd, renderer, explosionNode);
	}

	/**
	 * Called when a shell collides.
	 * Finds enemies in the blast radius and damages them accordingly
	 * @param o shell entity
	 * @param arg unused
	 */
	public void update(Observable o, Object arg) {
		ShellEntity shell = (ShellEntity) o;
		ParticleMesh explosion = Factory.buildSmallExplosion("ShellExplosion", renderer, shell.getSelf());
		explosionNode.attachChild(explosion);
		explosionNode.updateRenderState();
		explosion.forceRespawn();
		// TODO: Find enemies and hurt them
	}
}
