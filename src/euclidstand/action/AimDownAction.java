package euclidstand.action;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import euclidstand.engine.JMESpatial;

/**
 * Copied from com.jme.input.action.KeyNodeLookDownAction to implement constraints
 */
public class AimDownAction extends KeyInputAction {

	//temporary variables to handle rotation
	private static final Matrix3f incr = new Matrix3f();
	private static final Matrix3f tempMb = new Matrix3f();
	private static final Vector3f tempV = new Vector3f();
	//the node to manipulate
	private JMESpatial node;
	private float constraint;

	/**
	 * @param node
	 *			the node that will be affected by this action.
	 * @param speed
	 *			the speed at which the node can move.
	 * @param constraint
	 *			the angle limit which this action can go
	 */
	public AimDownAction(JMESpatial node, float speed, float constraint) {
		this.node = node;
		this.speed = speed;
		this.constraint = constraint;
	}

	/**
	 * <code>performAction</code> rotates the node towards the nodes'
	 * negative up axis at a speed of movement speed * time. Where time is the
	 * time between frames and 1 corresponds to 1 second.
	 * 
	 * @see com.jme.input.action.KeyInputAction#performAction(InputActionEvent)
	 */
	public void performAction(InputActionEvent evt) {
		if (node.getXRotation() < constraint) {
			node.getXRotationAsVector(tempV);
			tempV.normalizeLocal();
			incr.fromAngleNormalAxis(speed * evt.getTime(), tempV);
			node.setRotationMatrix(incr.mult(node.getRotationMatrix(), tempMb));
			node.normaliseRotation();
		}
	}
}

