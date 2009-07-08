
package euclidstand.engine;

import com.jme.renderer.pass.ShadowedRenderPass;

/**
 *
 * @author jmtan
 */
public class JMEShadowedRenderPass extends ShadowedRenderPass {
	public JMEShadowedRenderPass() {
		setRenderShadows(true);
		setLightingMethod(LightingMethod.Modulative);
	}

	public void addSpatialToRender(JMENode node) {
		add(node.getNode());
	}

	public void addSpatialToOcclude(JMENode node) {
		addOccluder(node.getNode());
	}
}
