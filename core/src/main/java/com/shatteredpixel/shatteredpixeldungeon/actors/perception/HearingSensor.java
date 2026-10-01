package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class HearingSensor extends Sensor {

	public HearingSensor(Mob owner, int range) {
		super(owner, PerceptionType.HEARING, range);
	}

	@Override
	protected void updateArea() {
		for (int cell = 0; cell < Dungeon.level.length(); cell++) {
			if (Dungeon.level.trueDistance(owner.pos, cell) <= range) area.add(cell);
		}
	}

	@Override
	protected boolean accepts(Stimulus stimulus) {
		return stimulus.type == StimulusType.SOUND;
	}

	@Override
	protected float perceivedIntensity(Stimulus stimulus) {
		return Math.max(0, stimulus.intensity - Dungeon.level.trueDistance(owner.pos, stimulus.pos));
	}
}
