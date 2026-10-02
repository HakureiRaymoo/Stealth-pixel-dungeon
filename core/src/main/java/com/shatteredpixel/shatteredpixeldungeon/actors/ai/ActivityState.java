package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

/**
 * A mob's routine activity, independent of its alert state and the legacy Mob AI.
 * These values describe future behavior; they do not execute it.
 */
public enum ActivityState {

	/** Resting, with a future reduced sensor configuration. */
	SLEEPING,

	/** Awake and stationary at its assigned position. */
	GUARDING,

	/** Awake and moving according to its routine. */
	WANDERING
}
