package com.narxoz.rpg.runner;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.tower.TowerRunResult;
import java.util.List;

public class TowerRunner {

    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = floors;
    }

    public TowerRunResult run(List<Hero> party) {
        int floorsCleared = 0;
        System.out.println("=== THE HAUNTED TOWER — ASCENT BEGINS ===");

        for (int i = 0; i < floors.size(); i++) {
            if (party.stream().noneMatch(Hero::isAlive)) {
                System.out.println("\n[GAME OVER] The entire party has fallen.");
                break;
            }

            FloorResult result = floors.get(i).explore(party);

            if (result.isCleared()) {
                floorsCleared++;
                System.out.printf("%n[CLEARED] Floor %d: %s%n", i + 1, result.getSummary());
            } else {
                System.out.printf("%n[FAILED]  Floor %d: %s%n", i + 1, result.getSummary());
                if (party.stream().noneMatch(Hero::isAlive)) {
                    System.out.println("[GAME OVER] No heroes remain.");
                    break;
                }
            }
        }

        int surviving = (int) party.stream().filter(Hero::isAlive).count();
        boolean reachedTop = floorsCleared == floors.size();

        System.out.println("\n=== TOWER RUN COMPLETE ===");
        System.out.printf("Floors Cleared  : %d / %d%n", floorsCleared, floors.size());
        System.out.printf("Heroes Surviving: %d%n", surviving);
        System.out.printf("Tower Status    : %s%n", reachedTop ? "CONQUERED!" : "FAILED");
        for (Hero h : party)
            if (h.isAlive()) System.out.println("  Survivor: " + h.statusLine());

        return new TowerRunResult(floorsCleared, surviving, reachedTop);
    }
}