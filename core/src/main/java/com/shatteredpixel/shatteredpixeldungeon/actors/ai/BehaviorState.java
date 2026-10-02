package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

/**
 * A future behavior phase, independent from activity and alertness.
 * These values describe intent only; they do not execute behavior.
 */
public enum BehaviorState {

	/** No special behavior is active. */
	IDLE,

	/** Future weak-suspicion behavior: turn toward the stimulus. */
	TURNING,

	/** Future strong-suspicion behavior: investigate the stimulus position. */
	INVESTIGATING,

	/** Future post-investigation behavior: return to the original position. */
	RETURNING,

	/** Future alert behavior: search the relevant area. */
	SEARCHING,

	/** Future confirmed-target behavior: pursue the target. */
	CHASING
}
