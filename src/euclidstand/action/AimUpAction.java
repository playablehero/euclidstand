package euclidstand.action;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import euclidstand.engine.JMESpatial;

/**
 * Copied from com.jme.input.action.KeyNodeLookUpAction to implement constraints
 */
public class AimUpAction extends KeyInputAction {

	//temporary variables to handle rotation
	private static final Matrix3f incr = new Matrix3f();
	private static final Matrix3f tempMb = new Matrix3f();
	private static final Vector3f tempVa = new Vector3f();
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
	public AimUpAction(JMESpatial node, float speed, float constraint) {
		this.node = node;
		this.speed = speed;
		this.constraint = constraint;
	}

	/**
	 * <code>performAction</code> rotates the node towards the world's
	 * positive y-axis at a speed of movement speed * time. Where time is the
	 * time between frames and 1 corresponds to 1 second.
	 * 
	 * @see com.jme.input.action.KeyInputAction#performAction(InputActionEvent)
	 */
	public void performAction(InputActionEvent evt) {
		if (node.getXRotation() > constraint) {
			node.getXRotationAsVector(tempVa);
			tempVa.normalizeLocal();
			incr.fromAngleNormalAxis(-speed * evt.getTime(), tempVa);
			node.setRotationMatrix(incr.mult(node.getRotationMatrix(), tempMb));
			node.normaliseRotation();
		}
	}
}

