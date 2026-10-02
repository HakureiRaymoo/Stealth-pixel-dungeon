package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

/**
 * A mob's cognitive response to information, separate from routine activity.
 * Reactions and their durations are reserved for future behavior logic.
 */
public enum AlertState {

	/** No active stimulus response; retain routine activity. */
	UNAWARE,

	/** Face a stimulus without travelling to it, then recover. */
	LOW_SUSPICION,

	/** Investigate a stimulus position, then return if nothing new is found. */
	HIGH_SUSPICION,

	/** Elevated concern that may later select a coordinated response. */
	LOW_ALERT,

	/** Highest non-confirmed concern about a hostile situation. */
	HIGH_ALERT,

	/**
	 * A hostile target identity has been confirmed. This is a persistent cognitive
	 * state, not an attack, chase, movement, or other combat action. Future behavior
	 * must retain wandering activity rather than resume sleeping or guarding; this
	 * value alone does not change ActivityState or implement that policy.
	 */
	COMBAT
}
