package com.shatteredpixel.shatteredpixeldungeon.actors.ai.adapter;

import com.shatteredpixel.shatteredpixeldungeon.actors.ai.MobAIAdapter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

/** Runtime capability adapter for a Shattered Pixel Dungeon Mob. */
public class SPDMobAdapter implements MobAIAdapter {

	private final Mob mob;
	private float direction;

	public SPDMobAdapter(Mob mob) {
		if (mob == null) throw new IllegalArgumentException("mob must not be null");
		this.mob = mob;
		direction = 0f;
	}

	@Override
	public int position() {
		return mob.pos;
	}

	@Override
	public float direction() {
		return direction;
	}

	@Override
	public void turnTo(float direction) {
		this.direction = normalize(direction);
		mob.visionDirection(this.direction);

		if (mob.sprite != null && mob.pos >= 0 && Math.cos(Math.toRadians(this.direction)) != 0) {
			int step = Math.cos(Math.toRadians(this.direction)) > 0 ? 1 : -1;
			mob.sprite.turnTo(mob.pos, mob.pos + step);
		}
	}

	@Override
	public boolean canAct() {
		return mob.isAlive() && mob.paralysed <= 0;
	}
	
	private static float normalize(float direction) {
		float normalized = direction % 360f;
		return normalized < 0 ? normalized + 360f : normalized;
	}
}
