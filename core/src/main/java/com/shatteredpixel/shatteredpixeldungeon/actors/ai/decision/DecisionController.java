package com.shatteredpixel.shatteredpixeldungeon.actors.ai.decision;

import com.shatteredpixel.shatteredpixeldungeon.actors.ai.BehaviorController;
import com.shatteredpixel.shatteredpixeldungeon.actors.ai.BehaviorState;
import com.shatteredpixel.shatteredpixeldungeon.actors.ai.MobMind;

/**
 * Minimal decision-layer skeleton. It selects only the initial TURNING behavior;
 * it does not move, pathfind, attack, or otherwise replace vanilla Mob AI.
 */
public class DecisionController {

	private final MobMind mind;
	private final BehaviorController behaviorController;

	public DecisionController(MobMind mind, BehaviorController behaviorController) {
		if (mind == null) throw new IllegalArgumentException("mind must not be null");
		if (behaviorController == null) throw new IllegalArgumentException("behaviorController must not be null");
		this.mind = mind;
		this.behaviorController = behaviorController;
	}

	/** Selects TURNING for weak suspicion when no behavior is active. */
	public boolean update(int mapWidth) {
		if (mind.alertState() != com.shatteredpixel.shatteredpixeldungeon.actors.ai.AlertState.LOW_SUSPICION
				|| mind.behaviorState() != BehaviorState.IDLE) return false;
		return behaviorController.beginTurning(mapWidth);
	}
}
