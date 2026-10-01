package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class PerceptionEvent {

	public final Mob observer;
	public final PerceptionType perceptionType;
	public final StimulusType stimulusType;
	public final int pos;
	public final Char source;
	public final float intensity;
	public final float time;
	public final float expiresAt;

	public PerceptionEvent(Mob observer, PerceptionType perceptionType, Stimulus stimulus, Char source, float intensity) {
		this.observer = observer;
		this.perceptionType = perceptionType;
		this.stimulusType = stimulus.type;
		this.pos = stimulus.pos;
		this.source = source;
		this.intensity = intensity;
		this.time = Actor.now();
		this.expiresAt = time + PerceptionSystem.EVENT_LIFETIME;
	}

	public boolean isExpired(float currentTime) {
		return currentTime >= expiresAt;
	}
}
