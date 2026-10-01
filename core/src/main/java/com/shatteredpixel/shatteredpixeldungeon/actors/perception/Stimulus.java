package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;

public class Stimulus {

	public final StimulusType type;
	public final int pos;
	public final Char source;
	public final float intensity;
	public final float time;
	public final float duration;

	public Stimulus(StimulusType type, int pos, Char source, float intensity, float duration) {
		this.type = type;
		this.pos = pos;
		this.source = source;
		this.intensity = intensity;
		this.time = Actor.now();
		this.duration = duration;
	}

	public static Stimulus at(StimulusType type, int pos, float intensity) {
		return new Stimulus(type, pos, null, intensity, 0);
	}

	public boolean isExpired(float currentTime) {
		return duration > 0 && currentTime >= time + duration;
	}
}
