package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

/**
 * Minimal entity-facing capability required by the behavior layer.
 * Implementations may be backed by a Mob, but the behavior system does not
 * depend on that concrete game object.
 *
 * Direction contract: 0 degrees is map east, angles increase clockwise, and
 * coordinates use the SPD map convention where the Y axis points downward.
 * Implementations must convert between Mob sprite orientation, map direction,
 * and perception direction so all three use this common angular convention.
 */
public interface MobAIAdapter {

	/** Returns the entity's current map cell. */
	int position();

	/** Returns the entity's current observation direction in degrees. */
	float direction();

	/** Changes the entity's observation direction in degrees. */
	void turnTo(float direction);

	/** Returns whether the entity may perform a behavior step this round. */
	boolean canAct();
}
