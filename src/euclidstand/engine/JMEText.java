package euclidstand.engine;

import com.jme.scene.Spatial;
import com.jme.scene.Text;

/**
 *
 */
public class JMEText extends Text {

	public JMEText() {
		super("", "");
	}

	public void setX(float x) {
		getLocalTranslation().setX(x);
	}

	public void setY(float y) {
		getLocalTranslation().setY(y);
	}

	public void setDefaultCulling() {
		setCullHint(Spatial.CullHint.Never);
	}

	public void setDefaultFontBlend() {
		setRenderState(getFontBlend());
	}

	public void setDefaultTexture() {
		setRenderState(getDefaultFontTextureState());
	}
}
