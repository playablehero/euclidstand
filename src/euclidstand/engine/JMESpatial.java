package euclidstand.engine;

import com.jme.bounding.BoundingVolume;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 *
 * @author jmtan
 */
public class JMESpatial {
	private final Spatial spatial;

	public JMESpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public void lockBounds() {
		spatial.lockBounds();
	}

	public String getName() {
		return spatial.getName();
	}

	public void removeFromParent() {
		spatial.removeFromParent();
	}

	public boolean hasCollision(JMESpatial other, boolean checkTriangles) {
		return spatial.hasCollision(other.getSpatial(), checkTriangles);
	}

	public void setLocalTranslation(Vector3f translation) {
		spatial.setLocalTranslation(translation);
	}

	public Vector3f getLocalTranslation() {
		return spatial.getLocalTranslation();
	}

	public Vector3f getWorldTranslation() {
		return spatial.getWorldTranslation();
	}

	public Quaternion getWorldRotation() {
		return spatial.getWorldRotation();
	}

	public BoundingVolume getWorldBound() {
		return spatial.getWorldBound();
	}

	public void normaliseRotation() {
		spatial.getLocalRotation().normalize();
	}

	public void setRotationMatrix(Matrix3f rotationMatrix) {
		spatial.getLocalRotation().fromRotationMatrix(rotationMatrix);
	}

	public Matrix3f getRotationMatrix() {
		return spatial.getLocalRotation().toRotationMatrix();
	}

	public void addTranslation(float x, float y, float z) {
		spatial.getLocalTranslation().addLocal(x, y, z);
	}

	public float getX() {
		return spatial.getLocalTranslation().getX();
	}

	public float getY() {
		return spatial.getLocalTranslation().getY();
	}

	public float getZ() {
		return spatial.getLocalTranslation().getZ();
	}

	public void setX(float x) {
		spatial.getLocalTranslation().setX(x);
	}

	public void setY(float y) {
		spatial.getLocalTranslation().setY(y);
	}

	public void setZ(float z) {
		spatial.getLocalTranslation().setZ(z);
	}

	public Vector3f getXRotationAsVector(Vector3f optional) {
		return getRotationAsVector(0, optional);
	}

	public Vector3f getYRotationAsVector(Vector3f optional) {
		return getRotationAsVector(1, optional);
	}

	public Vector3f getZRotationAsVector(Vector3f optional) {
		return getRotationAsVector(2, optional);
	}

	private Vector3f getRotationAsVector(int column, Vector3f optional) {
		return spatial.getLocalRotation().getRotationColumn(column, optional);
	}

	public float getXRotation() {
		return getRotation(0);
	}

	public float getYRotation() {
		return getRotation(1);
	}

	public float getZRotation() {
		return getRotation(2);
	}

	private float getRotation(int column) {
		return spatial.getLocalRotation().toAngles(null)[column];
	}

	public void moveXAxis(float amount) {
		moveAxis(amount, 0);
	}

	public void moveYAxis(float amount) {
		moveAxis(amount, 1);
	}

	public void moveZAxis(float amount) {
		moveAxis(amount, 2);
	}

	protected Spatial getSpatial() {
		return spatial;
	}

	private void moveAxis(float amount, int column) {
		spatial.getLocalTranslation().addLocal(spatial.getLocalRotation().
				getRotationColumn(column).mult(amount));
	}
}
