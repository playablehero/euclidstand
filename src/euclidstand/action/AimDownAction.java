package euclidstand.action;

import java.util.logging.Logger;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

/**
 * Copied from com.jme.input.action.KeyNodeLookDownAction to implement constraints
 */
public class AimDownAction extends KeyInputAction {
	private static final Logger logger = Logger.getLogger(AimDownAction.class.getName());

	//temporary variables to handle rotation
	private static final Matrix3f incr = new Matrix3f();

	private static final Matrix3f tempMa = new Matrix3f();

	private static final Matrix3f tempMb = new Matrix3f();

	private static final Vector3f tempV = new Vector3f();

	//the node to manipulate
	private Spatial node;

	private float constraint;

	/**
	 * @param node
	 *			the node that will be affected by this action.
	 * @param speed
	 *			the speed at which the node can move.
	 * @param constraint
	 *			the angle limit which this action can go
	 */
	public AimDownAction(Spatial node, float speed, float constraint) {
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
		float[] angles = node.getLocalRotation().toAngles(null);
		//logger.info("x: " + angles[0] + " y: " + angles[1] + " z: " + angles[2]);
		if (angles[0] < constraint) {
			node.getLocalRotation().getRotationColumn(0, tempV);
			tempV.normalizeLocal();
			incr.fromAngleNormalAxis(speed * evt.getTime(), tempV);
			node.getLocalRotation().fromRotationMatrix(
					incr.mult(node.getLocalRotation().toRotationMatrix(tempMa),
							tempMb));
			node.getLocalRotation().normalize();
		}
	}
}

