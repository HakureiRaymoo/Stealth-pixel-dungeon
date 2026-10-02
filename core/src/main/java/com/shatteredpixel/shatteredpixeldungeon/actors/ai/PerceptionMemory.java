package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

import com.shatteredpixel.shatteredpixeldungeon.actors.perception.PerceptionEvent;
import com.shatteredpixel.shatteredpixeldungeon.actors.perception.PerceptionType;
import com.shatteredpixel.shatteredpixeldungeon.actors.perception.StimulusType;

/**
 * Stores the latest useful perception for future behavior systems.
 * This class does not detect, evaluate, or act on perceptions.
 */
public class PerceptionMemory {

	private int lastStimulusPos = -1;
	private StimulusType lastStimulusType;
	private PerceptionType lastPerceptionType;
	private float lastStimulusTime = -1f;
	private int lastKnownTargetPos = -1;

	public int lastStimulusPos() {
		return lastStimulusPos;
	}

	public StimulusType lastStimulusType() {
		return lastStimulusType;
	}

	public PerceptionType lastPerceptionType() {
		return lastPerceptionType;
	}

	public float lastStimulusTime() {
		return lastStimulusTime;
	}

	public int lastKnownTargetPos() {
		return lastKnownTargetPos;
	}

	/** Returns whether at least one valid event has been remembered. */
	public boolean hasStimulus() {
		return lastStimulusType != null;
	}

	/**
	 * Records a positive-intensity event. Confirmed visual sources also update the
	 * last known target position.
	 */
	public void remember(PerceptionEvent event) {
		if (event == null || event.intensity <= 0f) return;

		lastStimulusPos = event.pos;
		lastStimulusType = event.stimulusType;
		lastPerceptionType = event.perceptionType;
		lastStimulusTime = event.time;

		if (event.perceptionType == PerceptionType.VISION && event.source != null) {
			lastKnownTargetPos = event.pos;
		}
	}

	/** Clears all remembered perception data and reserved target information. */
	public void clear() {
		lastStimulusPos = -1;
		lastStimulusType = null;
		lastPerceptionType = null;
		lastStimulusTime = -1f;
		lastKnownTargetPos = -1;
	}
}
