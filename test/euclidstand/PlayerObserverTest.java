package euclidstand;

import com.jmex.effects.particles.ParticleMesh;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMESpatial;
import java.util.List;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Eugene
 */
public class PlayerObserverTest {

	/**
	 * Test of updateInput method, of class PlayerObserver.
	 */
	@Test
	public void testUpdateInput() {
		float interpolation = 5.0f;
		boolean isShoot = true;
		List<Entity> entitiesToAdd = mock(List.class);
		JMENode bulletNode = mock(JMENode.class);
		JMENode playerNode = mock(JMENode.class);
		PlayerEntity player = mock(PlayerEntity.class);
		PlayerInputHandler input = mock(PlayerInputHandler.class);
		ShellObserver shellObserver = mock(ShellObserver.class);
		Builder builder = mock(Builder.class);
		ShellEntity.Factory shellEntityFactory = mock(ShellEntity.Factory.class);
		when(input.isShoot()).thenReturn(isShoot);

		PlayerObserver instance = new PlayerObserver(entitiesToAdd, bulletNode,
				playerNode, player, input, shellObserver, builder, shellEntityFactory);
		instance.updateInput(interpolation);

		verify(input).update(interpolation);
		verify(input).isShoot();
		verify(player).charge(isShoot);
		verifyNoMoreInteractions(input, player);
		verifyZeroInteractions(entitiesToAdd, bulletNode, playerNode, shellObserver, builder);
	}

	/**
	 * Test of update method, of class PlayerObserver.
	 */
	@Test
	public void testUpdate() {
		PlayerEntity.State state = PlayerEntity.State.REST;
		List<Entity> entitiesToAdd = mock(List.class);
		JMENode bulletNode = mock(JMENode.class);
		JMENode playerNode = mock(JMENode.class);
		PlayerEntity player = mock(PlayerEntity.class);
		PlayerInputHandler input = mock(PlayerInputHandler.class);
		ShellObserver shellObserver = mock(ShellObserver.class);
		Builder builder = mock(Builder.class);
		ShellEntity.Factory shellEntityFactory = mock(ShellEntity.Factory.class);

		PlayerObserver instance = new PlayerObserver(entitiesToAdd, bulletNode,
				playerNode, player, input, shellObserver, builder, shellEntityFactory);
		instance.update(player, state);

		verifyZeroInteractions(entitiesToAdd, bulletNode, playerNode, player, input, shellObserver, builder, shellEntityFactory);
	}

	@Test
	public void testUpdateFiring() {
		PlayerEntity.State state = PlayerEntity.State.FIRING;
		float firingAngle = 45f;
		float velocity = 10f;
		List<Entity> entitiesToAdd = mock(List.class);
		JMENode bulletNode = mock(JMENode.class);
		JMENode playerNode = mock(JMENode.class);
		PlayerEntity player = mock(PlayerEntity.class);
		PlayerInputHandler input = mock(PlayerInputHandler.class);
		ShellObserver shellObserver = mock(ShellObserver.class);
		Builder builder = mock(Builder.class);
		ShellEntity.Factory shellEntityFactory = mock(ShellEntity.Factory.class);
		JMESpatial barrel = mock(JMESpatial.class);
		JMESpatial shell = mock(JMESpatial.class);
		ShellEntity shellEntity = mock(ShellEntity.class);
		when(player.getBarrel()).thenReturn(barrel);
		when(builder.buildShell(anyString(), eq(barrel))).thenReturn(shell);
		when(player.getFiringAngle()).thenReturn(firingAngle);
		when(player.getVelocity()).thenReturn(velocity);
		when(shellEntityFactory.make(shell, firingAngle, velocity)).thenReturn(shellEntity);

		PlayerObserver instance = new PlayerObserver(entitiesToAdd, bulletNode,
				playerNode, player, input, shellObserver, builder, shellEntityFactory);
		instance.update(player, state);

		verify(player).getBarrel();
		verify(player).getFiringAngle();
		verify(player).getVelocity();
		verify(builder).buildShell(anyString(), eq(barrel));
		verify(shellEntityFactory).make(shell, firingAngle, velocity);
		verify(bulletNode).attachChild(shell);
		verify(shellEntity).addObserver(shellObserver);
		verify(entitiesToAdd).add(shellEntity);
		verify(playerNode).updateRenderState();

		verifyNoMoreInteractions(player, builder, shellEntityFactory, bulletNode, shellEntity, entitiesToAdd, playerNode);
		verifyZeroInteractions(input, barrel, shell);
	}

	@Test
	public void testUpdateDeath() {
		PlayerEntity.State state = PlayerEntity.State.DEAD;
		List<Entity> entitiesToAdd = mock(List.class);
		JMENode bulletNode = mock(JMENode.class);
		JMENode playerNode = mock(JMENode.class);
		PlayerEntity player = mock(PlayerEntity.class);
		PlayerInputHandler input = mock(PlayerInputHandler.class);
		ShellObserver shellObserver = mock(ShellObserver.class);
		Builder builder = mock(Builder.class);
		ShellEntity.Factory shellEntityFactory = mock(ShellEntity.Factory.class);
		ParticleMesh explosion = mock(ParticleMesh.class);
		JMESpatial playerSpatial = mock(JMESpatial.class);
		when(player.getSelf()).thenReturn(playerSpatial);
		when(builder.buildBigExplosion(anyString(), eq(playerSpatial))).thenReturn(explosion);

		PlayerObserver instance = new PlayerObserver(entitiesToAdd, bulletNode,
				playerNode, player, input, shellObserver, builder, shellEntityFactory);
		instance.update(player, state);

		verify(player).getSelf();
		verify(builder).buildBigExplosion(anyString(), eq(playerSpatial));
		verify(playerNode).attachChild(explosion);
		verify(playerNode).updateRenderState();
		verify(explosion).forceRespawn();

		verifyNoMoreInteractions(player, builder, playerNode, explosion);
		verifyZeroInteractions(entitiesToAdd, bulletNode, input, shellObserver, shellEntityFactory);
	}
}