package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class MagicSensor extends Sensor {

	public MagicSensor(Mob owner, int range) {
		super(owner, PerceptionType.MAGIC, range);
	}

	@Override
	protected void updateArea() {
		// Reserved for a future magic propagation model.
	}

	@Override
	protected boolean accepts(Stimulus stimulus) {
		return stimulus.type == StimulusType.MAGIC;
	}
}
