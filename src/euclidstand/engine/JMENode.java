package euclidstand.engine;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 *
 * @author jmtan
 */
public class JMENode {

	private final Node node;

	public JMENode(String name) {
		node = new Node(name);
	}

	public JMENode(Node node) {
		this.node = node;
	}

	public JMESpatial getChild(String name) {
		return new JMESpatial(node.getChild(name));
	}

	public void updateRenderState() {
		node.updateRenderState();
	}

	public void attachToParent(Node parent) {
		parent.attachChild(node);
	}

	public void attachChild(JMENode child) {
		node.attachChild(child.getNode());
	}

	public void attachChild(JMESpatial child) {
		node.attachChild(child.getSpatial());
	}

	public void attachChild(Spatial child) {
		node.attachChild(child);
	}

	protected Node getNode() {
		return node;
	}

	public static class Factory {

		public JMENode make(String name) {
			return new JMENode(name);
		}

		public JMENode make(Node node) {
			return new JMENode(node);
		}
	}
}
