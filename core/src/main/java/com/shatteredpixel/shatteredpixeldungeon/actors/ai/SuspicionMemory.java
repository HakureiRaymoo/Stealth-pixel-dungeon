package com.shatteredpixel.shatteredpixeldungeon.actors.ai;

/**
 * Stores a mob's accumulated suspicion without interpreting or acting on it.
 * The value is deliberately independent from both activity and alert state.
 */
public class SuspicionMemory {

	public static final float MIN_SUSPICION = 0f;
	public static final float MAX_SUSPICION = 100f;
	public static final float DEFAULT_DECAY_RATE = 1f;

	private float suspicion;
	private float decayRate;

	public SuspicionMemory() {
		this(0f, DEFAULT_DECAY_RATE);
	}

	public SuspicionMemory(float initialSuspicion, float decayRate) {
		this.decayRate = Math.max(0f, decayRate);
		suspicion = clamp(initialSuspicion);
	}

	public float suspicion() {
		return suspicion;
	}

	public float decayRate() {
		return decayRate;
	}

	/** Adds a positive amount of suspicion while keeping the value within the supported range. */
	public void addSuspicion(float amount) {
		if (!(amount > 0f)) return;
		suspicion = clamp(suspicion + amount);
	}

	/** Reduces suspicion by one configured decay step. */
	public void tickDecay() {
		decay(decayRate);
	}

	/** Reduces suspicion by the supplied amount, never below zero. */
	public void decay(float amount) {
		suspicion = clamp(suspicion - Math.max(0f, amount));
	}

	public void decayRate(float decayRate) {
		this.decayRate = Math.max(0f, decayRate);
	}

	private static float clamp(float value) {
		return Math.max(MIN_SUSPICION, Math.min(MAX_SUSPICION, value));
	}
}
