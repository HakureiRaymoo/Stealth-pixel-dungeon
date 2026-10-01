package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

public abstract class Sensor {

	protected final Mob owner;
	protected final PerceptionType type;
	protected int range;
	protected final PerceptionArea area = new PerceptionArea();
	private int lastPos = -1;
	private int lastLosRevision = -1;
	private int revision;

	protected Sensor(Mob owner, PerceptionType type, int range) {
		this.owner = owner;
		this.type = type;
		this.range = range;
	}

	public Mob owner() { return owner; }
	public PerceptionType type() { return type; }
	public int range() { return range; }
	public void range(int range) {
		if (this.range != range) {
			this.range = range;
			invalidateArea();
		}
	}
	public PerceptionArea area() {
		int losRevision = Dungeon.level == null ? -1 : Dungeon.level.losRevision();
		if (lastPos != owner.pos || lastLosRevision != losRevision) {
			area.clear();
			updateArea();
			lastPos = owner.pos;
			lastLosRevision = losRevision;
			revision++;
		}
		return area;
	}

	public int revision() { return revision; }

	protected void invalidateArea() {
		lastPos = -1;
		lastLosRevision = -1;
	}

	protected abstract void updateArea();
	protected abstract boolean accepts(Stimulus stimulus);

	public PerceptionEvent perceive(Stimulus stimulus) {
		if (!accepts(stimulus) || !area().contains(stimulus.pos)) return null;
		return new PerceptionEvent(owner, type, stimulus, identifies(stimulus) ? stimulus.source : null, perceivedIntensity(stimulus));
	}

	protected boolean identifies(Stimulus stimulus) { return false; }

	protected float perceivedIntensity(Stimulus stimulus) {
		return stimulus.intensity;
	}
}
