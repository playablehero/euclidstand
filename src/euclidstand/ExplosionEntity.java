package euclidstand;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.terrain.TerrainBlock;

/**
 * An Explosion
 */
public class ExplosionEntity extends Entity {

	private final ParticleMesh mesh;
	private float time;

	private ExplosionEntity(Spatial self, ParticleMesh mesh) {
		super(self);
		this.mesh = mesh;
	}

	/**
	 * @return an explosion
	 */
	public static ExplosionEntity getExplosion(Node explosionNode) {
		ParticleMesh mesh = (ParticleMesh)explosionNode;
		//ParticleMesh mesh = (ParticleMesh) explosionNode.getChild("explosion_mesh");
		return new ExplosionEntity(explosionNode, mesh);
	}

	/**
	 * Is javadoc really necessary for this?
	 */
	public void explode() {
		mesh.forceRespawn();
		time = System.currentTimeMillis();
	}

	public boolean hasExploded() {
		return ((System.currentTimeMillis() - time) > mesh.getMaximumLifeTime());
	}

	@Override
	public void update(float interpolation) {
		if (hasExploded()) {
			System.out.println("FINISHED EXPLODING");
			setRemove(true);
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Do nothing for terrain
	 * @param terrain not used
	 */
	@Override
	public void updateTerrain(TerrainBlock terrain) {
	}
}
