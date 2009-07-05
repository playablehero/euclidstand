package euclidstand.engine;

import com.jme.input.action.KeyNodeRotateRightAction;

/**
 *
 * @author jmtan
 */
public class JMEKeyNodeRotateRightAction extends KeyNodeRotateRightAction {
    public JMEKeyNodeRotateRightAction(JMESpatial spatial, float speed) {
		super(spatial.getSpatial(), speed);
	}
}
