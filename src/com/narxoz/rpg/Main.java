package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.*;
import com.narxoz.rpg.runner.TowerRunner;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Hero 1: starts in Berserk state
        Hero aragorn = new Hero("Aragorn", 120, 22, 8);
        aragorn.setState(new BerserkState(4));

        // Hero 2: starts in Poisoned state
        Hero legolas = new Hero("Legolas", 100, 18, 5);
        legolas.setState(new PoisonedState(3));

        List<Hero> party = Arrays.asList(aragorn, legolas);

        System.out.println("=== PARTY AT TOWER ENTRANCE ===");
        for (Hero h : party) System.out.println("  " + h.statusLine());

        List<TowerFloor> floors = Arrays.asList(
                // Floor 1: CombatFloor — default hooks
                new CombatFloor(
                        "Floor 1 - Skeleton Crypt",
                        new Monster("Skeleton Warrior", 45, 14),
                        new Monster("Skeleton Archer", 30, 12)
                ),
                // Floor 2: TrapFloor — overrides announce, shouldAwardLoot, cleanup
                new TrapFloor(
                        "Floor 2 - Poison Dart Corridor",
                        15, true
                ),
                // Floor 3: RestFloor — overrides shouldAwardLoot (false), cleanup
                new RestFloor(
                        "Floor 3 - Ancient Sanctuary",
                        25
                ),
                // Floor 4: CombatFloor — tougher enemies that can stun
                new CombatFloor(
                        "Floor 4 - Dark Knight's Chamber",
                        new Monster("Dark Knight", 60, 20),
                        new Monster("Shadow Mage", 40, 18)
                ),
                // Floor 5: BossFloor — overrides announce dramatically
                new BossFloor(
                        "Floor 5 - The Lich King's Throne",
                        new Monster("The Lich King", 120, 25)
                )
        );

        TowerRunner runner = new TowerRunner(floors);
        TowerRunResult result = runner.run(party);

        System.out.println("\n=== FINAL RESULT ===");
        System.out.println("Floors cleared : " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top    : " + result.isReachedTop());
    }
}