package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Group;

public class PerceptionDebugRenderer extends Group {

	private int areaRevision = -1;

	@Override
	public void update() {
		visible = SPDSettings.perceptionDebug();
		if (visible) rebuildIfNeeded();
		super.update();
	}

	private void rebuildIfNeeded() {
		int revision = 0;
		for (Mob mob : Dungeon.level.mobs) {
			revision = 31 * revision + (visibleToHero(mob) ? 1 : 0);
			if (!visibleToHero(mob)) continue;
			for (Sensor sensor : mob.sensors()) {
				sensor.area();
				revision = 31 * revision + sensor.revision();
			}
		}
		if (revision == areaRevision) return;
		clear();
		areaRevision = revision;
		for (Mob mob : Dungeon.level.mobs) {
			if (!visibleToHero(mob)) continue;
			for (Sensor sensor : mob.sensors()) {
				int color = sensor.type() == PerceptionType.VISION ? 0x3322AAFF : 0x33FFAA22;
				for (int cell : sensor.area().cells()) {
					ColorBlock block = new ColorBlock(DungeonTilemap.SIZE, DungeonTilemap.SIZE, color);
					block.point(DungeonTilemap.tileToWorld(cell));
					add(block);
				}
			}
		}
	}

	private boolean visibleToHero(Mob mob) {
		return mob.pos >= 0
				&& mob.pos < Dungeon.level.heroFOV.length
				&& Dungeon.level.heroFOV[mob.pos];
	}
}
