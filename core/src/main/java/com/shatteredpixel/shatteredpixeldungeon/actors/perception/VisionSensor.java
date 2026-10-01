package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;

public class VisionSensor extends Sensor {

	private float angle;
	private float direction;
	private boolean[] visible;

	public VisionSensor(Mob owner, int range, float angle, float direction) {
		super(owner, PerceptionType.VISION, range);
		this.angle = angle;
		this.direction = direction;
	}

	public void direction(float direction) {
		if (this.direction != direction) {
			this.direction = direction;
			invalidateArea();
		}
	}

	public float direction() { return direction; }
	public float angle() { return angle; }
	public void angle(float angle) {
		if (this.angle != angle) {
			this.angle = angle;
			invalidateArea();
		}
	}

	@Override
	protected void updateArea() {
		if (Dungeon.level == null) return;
		if (visible == null || visible.length != Dungeon.level.length()) visible = new boolean[Dungeon.level.length()];
		ShadowCaster.castShadow(owner.pos % Dungeon.level.width(), owner.pos / Dungeon.level.width(), Dungeon.level.width(), visible, Dungeon.level.losBlocking, range);
		for (int cell = 0; cell < visible.length; cell++) {
			if (visible[cell] && withinAngle(cell)) area.add(cell);
		}
	}

	private boolean withinAngle(int cell) {
		if (cell == owner.pos || angle >= 360) return true;
		int dx = cell % Dungeon.level.width() - owner.pos % Dungeon.level.width();
		int dy = cell / Dungeon.level.width() - owner.pos / Dungeon.level.width();
		float delta = (float)Math.toDegrees(Math.atan2(dy, dx)) - direction;
		while (delta <= -180) delta += 360;
		while (delta > 180) delta -= 360;
		return Math.abs(delta) <= angle / 2f;
	}

	@Override
	protected boolean accepts(Stimulus stimulus) {
		return stimulus.type == StimulusType.MOVEMENT || stimulus.type == StimulusType.PRESENCE || stimulus.type == StimulusType.IMPACT;
	}

	// Future concealment rules belong here, separate from the area's spatial test.
	public boolean canSee(Char target) {
		return target != owner && target.isAlive() && target.invisible <= 0 && area().contains(target.pos);
	}

	@Override
	public PerceptionEvent perceive(Stimulus stimulus) {
		if (stimulus.source != null && !canSee(stimulus.source)) return null;
		return super.perceive(stimulus);
	}

	@Override
	protected boolean identifies(Stimulus stimulus) {
		return true;
	}
}
