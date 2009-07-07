package euclidstand;

import com.jmex.effects.particles.ParticleMesh;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESpatial;
import euclidstand.engine.JMESphere;
import java.util.List;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Eugene
 */
public class ShellObserverTest {

	/**
	 * Test of update method, of class ShellObserver.
	 */
	@Test
	public void testUpdate() {

		List<Entity> entitiesToAdd = mock(List.class);
		JMENode sceneNode = mock(JMENode.class);
		JMENode explosionNode = mock(JMENode.class);
		ShellCollision shellCollision = mock(ShellCollision.class);
		Builder builder = mock(Builder.class);
		JMESphere explosionSphere = mock(JMESphere.class);
		ShellEntity shellEntity = mock(ShellEntity.class);
		ParticleMesh explosion = mock(ParticleMesh.class);
		JMESpatial shellSpatial = mock(JMESpatial.class);
		when(builder.buildSmallExplosion(anyString(), eq(shellSpatial))).thenReturn(explosion);
		when(shellEntity.getSelf()).thenReturn(shellSpatial);

		ShellObserver instance = new ShellObserver(entitiesToAdd, shellCollision, sceneNode, explosionNode, builder, explosionSphere);
		instance.update(shellEntity, null);

		verify(shellEntity).getSelf();
		verify(builder).buildSmallExplosion(anyString(), eq(shellSpatial));
		verify(explosionSphere).calculateCollisions(sceneNode, shellCollision);
		verify(explosionNode).attachChild(explosion);
		verify(explosion).updateRenderState();
		verify(explosion).forceRespawn();
		verifyNoMoreInteractions(shellEntity, builder, explosionSphere, explosionNode, explosion);
		verifyZeroInteractions(entitiesToAdd, sceneNode, shellSpatial);
	}
}