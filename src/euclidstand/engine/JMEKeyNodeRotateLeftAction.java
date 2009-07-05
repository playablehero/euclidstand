package euclidstand.engine;

import com.jme.input.action.KeyNodeRotateLeftAction;

/**
 * I hate this class
 * @author jmtan
 */
public class JMEKeyNodeRotateLeftAction extends KeyNodeRotateLeftAction {
    public JMEKeyNodeRotateLeftAction(JMESpatial spatial, float speed) {
		super(spatial.getSpatial(), speed);
	}
}
