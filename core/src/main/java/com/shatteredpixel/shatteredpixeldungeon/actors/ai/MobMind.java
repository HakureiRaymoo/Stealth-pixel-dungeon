package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

import com.shatteredpixel.shatteredpixeldungeon.actors.perception.PerceptionEvent;
import com.shatteredpixel.shatteredpixeldungeon.actors.perception.PerceptionType;

/**
 * Cognitive state for a single mob, intended to be owned by that mob in a future
 * integration. It is not an Actor and does not drive the legacy Mob AI.
 *
 * Routine activity and alertness are stored independently. Timers, stimulus
 * memories, investigation targets and return positions can be added here later;
 * sensing and behavior execution belong outside this state container.
 */
public class MobMind {

	public static final float WEAK_SUSPICION_GAIN = 5f;
	public static final float NORMAL_SUSPICION_GAIN = 15f;
	public static final float STRONG_SUSPICION_GAIN = 30f;
	public static final float CONFIRMED_TARGET_GAIN = 100f;

	public static final float LOW_SUSPICION_THRESHOLD = 20f;
	public static final float HIGH_SUSPICION_THRESHOLD = 40f;
	public static final float LOW_ALERT_THRESHOLD = 70f;
	public static final float HIGH_ALERT_THRESHOLD = 90f;

	private ActivityState activityState;
	private AlertState alertState;
	private BehaviorState behaviorState;
	private final SuspicionMemory suspicionMemory;
	private final PerceptionMemory perceptionMemory;

	public MobMind() {
		this(ActivityState.SLEEPING, new SuspicionMemory());
	}

	/** Creates an unaware mind with the mob's configured routine activity. */
	public MobMind(ActivityState initialActivityState) {
		this(initialActivityState, new SuspicionMemory());
	}

	private MobMind(ActivityState initialActivityState, SuspicionMemory suspicionMemory) {
		setActivityState(initialActivityState);
		alertState = AlertState.UNAWARE;
		behaviorState = BehaviorState.IDLE;
		this.suspicionMemory = suspicionMemory;
		perceptionMemory = new PerceptionMemory();
	}

	public ActivityState activityState() {
		return activityState;
	}

	public AlertState alertState() {
		return alertState;
	}

	public BehaviorState behaviorState() {
		return behaviorState;
	}

	public float suspicion() {
		return suspicionMemory.suspicion();
	}

	public SuspicionMemory suspicionMemory() {
		return suspicionMemory;
	}

	public PerceptionMemory perceptionMemory() {
		return perceptionMemory;
	}

	/** Adds suspicion and refreshes the non-combat alert state. */
	public void addSuspicion(float amount) {
		suspicionMemory.addSuspicion(amount);
		updateAlertState();
	}

	/** Applies one configured decay step and refreshes the non-combat alert state. */
	public void tickSuspicionDecay() {
		suspicionMemory.tickDecay();
		updateAlertState();
	}

	/**
	 * Converts one perceived event into suspicion and applies the resulting gain.
	 * Sensor implementations do not modify this memory directly.
	 *
	 * @return the amount added, or zero for a null event
	 */
	public float processPerceptionEvent(PerceptionEvent event) {
		float gain = calculateSuspicionGain(event);
		addSuspicion(gain);
		perceptionMemory.remember(event);
		if (isConfirmedTarget(event)) alertState = AlertState.COMBAT;
		return gain;
	}

	/**
	 * Recomputes alertness from suspicion. Combat is terminal for this state
	 * container and is left for a future behavior system to clear.
	 */
	public void updateAlertState() {
		if (alertState == AlertState.COMBAT) return;

		float currentSuspicion = suspicion();
		if (currentSuspicion >= HIGH_ALERT_THRESHOLD) {
			alertState = AlertState.HIGH_ALERT;
		} else if (currentSuspicion >= LOW_ALERT_THRESHOLD) {
			alertState = AlertState.LOW_ALERT;
		} else if (currentSuspicion >= HIGH_SUSPICION_THRESHOLD) {
			alertState = AlertState.HIGH_SUSPICION;
		} else if (currentSuspicion >= LOW_SUSPICION_THRESHOLD) {
			alertState = AlertState.LOW_SUSPICION;
		} else {
			alertState = AlertState.UNAWARE;
		}
	}

	/**
	 * Provides the initial event-to-suspicion policy for the future AlertSystem.
	 * A visual event with an identified source is considered a confirmed target.
	 */
	public static float calculateSuspicionGain(PerceptionEvent event) {
		if (event == null) return 0f;
		if (isConfirmedTarget(event)) {
			return CONFIRMED_TARGET_GAIN;
		}
		if (event.intensity >= 3f) return STRONG_SUSPICION_GAIN;
		if (event.intensity >= 2f) return NORMAL_SUSPICION_GAIN;
		return WEAK_SUSPICION_GAIN;
	}

	private static boolean isConfirmedTarget(PerceptionEvent event) {
		return event != null
				&& event.perceptionType == PerceptionType.VISION
				&& event.source != null;
	}

	/** Changes routine activity without changing alertness. */
	public final void setActivityState(ActivityState activityState) {
		if (activityState == null) throw new IllegalArgumentException("activityState must not be null");
		this.activityState = activityState;
	}

	/**
	 * Changes alertness without changing routine activity or triggering behavior.
	 * The future COMBAT activity policy is deliberately not enforced here yet.
	 */
	public final void setAlertState(AlertState alertState) {
		if (alertState == null) throw new IllegalArgumentException("alertState must not be null");
		if (this.alertState == AlertState.COMBAT && alertState != AlertState.COMBAT) return;
		this.alertState = alertState;
	}

	/** Changes behavior state without changing activity or alertness. */
	public final void setBehaviorState(BehaviorState behaviorState) {
		if (behaviorState == null) throw new IllegalArgumentException("behaviorState must not be null");
		this.behaviorState = behaviorState;
	}
}
