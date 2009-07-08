
package euclidstand;

import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import euclidstand.engine.JMENode;
import euclidstand.engine.JMEShadowedRenderPass;
import euclidstand.engine.JMESimpleGame;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author jmtan
 */
public class EuclidStandTest {

	/**
	 * Test of initGame method, of class EuclidStand.
	 */
	@Test
	public void testInitGame() {
		int width = 800;
		int height = 600;
		Builder builder = mock(Builder.class);
		JMENode.Factory nodeFactory = mock(JMENode.Factory.class);
		GameScene.Factory sceneFactory = mock(GameScene.Factory.class);
		GameScene scene = mock(GameScene.class);
		JMESimpleGame game = mock(JMESimpleGame.class);
		JMEShadowedRenderPass renderPass = mock(JMEShadowedRenderPass.class);
		EuclidStand instance = new EuclidStand(builder, nodeFactory, sceneFactory, renderPass, game);
		Renderer renderer = mock(Renderer.class);
		JMENode node = mock(JMENode.class);
		Camera cam = mock(Camera.class);
		when(game.getRenderer()).thenReturn(renderer);
		when(nodeFactory.make(anyString())).thenReturn(node);
		when(game.getCamera()).thenReturn(cam);
		when(game.getWidth()).thenReturn(width);
		when(game.getHeight()).thenReturn(height);
		when(sceneFactory.make(builder, node, renderPass, cam, width, height)).thenReturn(scene);

		instance.initGame();

		assertEquals("Scene member is not as expected", scene, instance.scene);
		verify(game).setTitle(anyString());
		verify(game).getRenderer();
		verify(builder).initialise(eq(renderer));
		verify(nodeFactory).make(anyString());
		verify(game).attachScene(node);
		verify(game).getCamera();
		verify(game).getWidth();
		verify(game).getHeight();
		verify(renderPass).addSpatialToRender(node);
		verify(game).addRenderPass(renderPass);
		verify(sceneFactory).make(builder, node, renderPass, cam, width, height);
		verifyNoMoreInteractions(game, builder, nodeFactory, sceneFactory, renderPass);
		verifyZeroInteractions(renderer, node, cam, scene);
	}

	/**
	 * Test of update method, of class EuclidStand.
	 */
	@Test
	public void testUpdate() {
		float interpolation = 10f;
		Builder builder = mock(Builder.class);
		JMENode.Factory nodeFactory = mock(JMENode.Factory.class);
		GameScene.Factory sceneFactory = mock(GameScene.Factory.class);
		GameScene scene = mock(GameScene.class);
		JMESimpleGame game = mock(JMESimpleGame.class);
		JMEShadowedRenderPass renderPass = mock(JMEShadowedRenderPass.class);

		EuclidStand instance = new EuclidStand(builder, nodeFactory, sceneFactory, renderPass, game);
		instance.scene = scene;
		instance.update(interpolation);

		verify(scene).update(interpolation);
		verifyNoMoreInteractions(scene);
		verifyZeroInteractions(builder, nodeFactory, game, renderPass, sceneFactory);
	}

	/**
	 * Test of start method, of class EuclidStand.
	 */
	@Test
	public void testStart() {
		Builder builder = mock(Builder.class);
		JMENode.Factory nodeFactory = mock(JMENode.Factory.class);
		GameScene.Factory sceneFactory = mock(GameScene.Factory.class);
		JMESimpleGame game = mock(JMESimpleGame.class);
		JMEShadowedRenderPass renderPass = mock(JMEShadowedRenderPass.class);

		EuclidStand instance = new EuclidStand(builder, nodeFactory, sceneFactory, renderPass, game);
		instance.run();

		verify(game).run();
		verifyNoMoreInteractions(game);
		verifyZeroInteractions(builder, nodeFactory, sceneFactory, renderPass);
	}
}