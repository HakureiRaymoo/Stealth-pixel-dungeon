package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

/**
 * Executes the small set of behavior transitions currently supported by the
 * cognitive layer. The controller is deliberately independent from Mob and can
 * be connected by a future Mob adapter.
 */
public class BehaviorController {

	/**
	 * Number of behavior ticks for TURNING. A tick is one valid behavior update
	 * opportunity, not wall-clock time or elapsed game time.
	 */
	public static final int TURN_DURATION = 5;

	private final MobMind mind;
	private final MobAIAdapter adapter;
	private float originalDirection;
	private int turnsRemaining;

	public BehaviorController(MobMind mind, MobAIAdapter adapter) {
		if (mind == null) throw new IllegalArgumentException("mind must not be null");
		if (adapter == null) throw new IllegalArgumentException("adapter must not be null");
		this.mind = mind;
		this.adapter = adapter;
	}

	public MobMind mind() {
		return mind;
	}

	public MobAIAdapter adapter() {
		return adapter;
	}

	public int turnsRemaining() {
		return turnsRemaining;
	}

	public boolean isTurning() {
		return mind.behaviorState() == BehaviorState.TURNING;
	}

	/**
	 * Starts a fixed-duration turn toward the latest remembered stimulus.
	 * Position values are cells in a row-major map.
	 *
	 * @return false when there is no valid stimulus, the mind is not weakly
	 * suspicious, or another behavior is already active
	 */
	public boolean beginTurning(int mapWidth) {
		if (mind.alertState() != AlertState.LOW_SUSPICION
				|| mind.behaviorState() != BehaviorState.IDLE
				|| mapWidth <= 0 || !adapter.canAct()) return false;

		int stimulusPos = mind.perceptionMemory().lastStimulusPos();
		if (adapter.position() < 0 || stimulusPos < 0) return false;

		originalDirection = adapter.direction();
		adapter.turnTo(directionTo(adapter.position(), stimulusPos, mapWidth));
		turnsRemaining = TURN_DURATION;
		mind.setBehaviorState(BehaviorState.TURNING);
		return true;
	}

	/**
	 * Advances the turn by one behavior round. The final round restores the
	 * direction that was present before turning and returns to IDLE.
	 */
	public void tick() {
		if (!isTurning() || !adapter.canAct()) return;

		turnsRemaining--;
		if (turnsRemaining <= 0) {
			adapter.turnTo(originalDirection);
			turnsRemaining = 0;
			mind.setBehaviorState(BehaviorState.IDLE);
		}
	}

	private static float directionTo(int observerPos, int stimulusPos, int mapWidth) {
		int observerX = observerPos % mapWidth;
		int observerY = observerPos / mapWidth;
		int stimulusX = stimulusPos % mapWidth;
		int stimulusY = stimulusPos / mapWidth;
		float angle = (float)Math.toDegrees(Math.atan2(stimulusY - observerY, stimulusX - observerX));
		return angle < 0 ? angle + 360f : angle;
	}
}
