package euclidstand;

/**
 * Provides magic numbers
 */
class Constants {

	/**
	 * The angle used by the input controller, tuned to the current cannon model
	 */
	public static final float UP_INPUT_MAXIMUM = -0.33009472f;
	/**
	 * The angle used by the input controller, tuned to the current cannon model
	 */
	public static final float DOWN_INPUT_MAXIMUM = 0.1352511f;
	/**
	 * The angle used by game calculations, tuned to the current cannon model
	 */
	public static final float UP_ANGLE_MAXIMUM = 80f;
	/**
	 * The angle used by game calculations, tuned to the current cannon model
	 */
	public static final float DOWN_ANGLE_MAXIMUM = 60f;
	/**
	 * Rate of charging velocity when fire key is held
	 */
	public static final float VELOCITY_INCREMENT = 0.1f;
	/**
	 * Scale down the vertical velocity
	 */
	public static final float VERTICAL_SCALE = 0.00001f;
	/**
	 * Rate of incrementing the vertical velocity
	 */
	public static final float VERTICAL_INCREMENT = 0.01f;
	/**
	 * Height above the ground which shell should register a collision
	 */
	public static final float SHELL_COLLISION_THRESHOLD = 2f;
}
