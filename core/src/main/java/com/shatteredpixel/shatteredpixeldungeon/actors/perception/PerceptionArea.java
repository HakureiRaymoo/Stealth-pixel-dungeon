package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PerceptionArea {

	private final HashSet<Integer> cells = new HashSet<>();

	public void clear() {
		cells.clear();
	}

	public void add(int cell) {
		cells.add(cell);
	}

	public boolean contains(int cell) {
		return cells.contains(cell);
	}

	public Set<Integer> cells() {
		return Collections.unmodifiableSet(cells);
	}
}
