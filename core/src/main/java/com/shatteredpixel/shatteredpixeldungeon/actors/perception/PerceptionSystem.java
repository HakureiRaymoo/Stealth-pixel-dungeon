package com.shatteredpixel.shatteredpixeldungeon.actors.perception;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerceptionSystem {

	public static final float EVENT_LIFETIME = 1f;
	private static final int MAX_EVENTS = 128;
	private static final ArrayList<PerceptionEvent> events = new ArrayList<>();

	public static void emit(Stimulus stimulus) {
		if (Dungeon.level == null || stimulus.isExpired(Actor.now())) return;
		removeExpiredEvents();
		for (Mob mob : Dungeon.level.mobs) {
			for (Sensor sensor : mob.sensors()) {
				PerceptionEvent event = sensor.perceive(stimulus);
				addEvent(event);
			}
		}
	}

	public static void observeVisibleTargets(Mob observer) {
		if (Dungeon.level == null || !Dungeon.level.mobs.contains(observer)) return;
		removeExpiredEvents();
		for (Sensor sensor : observer.sensors()) {
			if (!(sensor instanceof VisionSensor)) continue;
			VisionSensor vision = (VisionSensor)sensor;
			for (Char target : Actor.chars()) {
				if (isOnCurrentLevel(target) && vision.canSee(target)) {
					addEvent(sensor.perceive(new Stimulus(StimulusType.PRESENCE, target.pos, target, 1, 0)));
				}
			}
		}
	}

	private static boolean isOnCurrentLevel(Char target) {
		return target == Dungeon.hero || target instanceof Mob && Dungeon.level.mobs.contains(target);
	}

	private static void addEvent(PerceptionEvent event) {
		if (event == null || event.isExpired(Actor.now()) || duplicate(event)) return;
		events.add(event);
		dispatchToMind(event);
		if (events.size() > MAX_EVENTS) events.remove(0);
	}

	private static void dispatchToMind(PerceptionEvent event) {
		if (event.observer == null) return;
		if (event.observer.mindIfCreated() != null) {
			event.observer.mindIfCreated().processPerceptionEvent(event);
		}
	}

	private static boolean duplicate(PerceptionEvent event) {
		for (PerceptionEvent existing : events) {
			if (existing.observer == event.observer
					&& existing.perceptionType == event.perceptionType
					&& existing.stimulusType == event.stimulusType
					&& existing.pos == event.pos
					&& existing.source == event.source) return true;
		}
		return false;
	}

	public static List<PerceptionEvent> events() {
		removeExpiredEvents();
		return Collections.unmodifiableList(events);
	}

	public static void removeExpiredEvents() {
		float now = Actor.now();
		for (int i = events.size() - 1; i >= 0; i--) {
			if (events.get(i).isExpired(now)) events.remove(i);
		}
	}

	public static void clearEvents() {
		events.clear();
	}
}
