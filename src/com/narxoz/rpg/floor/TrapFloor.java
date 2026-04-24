package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import java.util.List;

public class TrapFloor extends TowerFloor {

    private final String floorName;
    private final int trapDamage;
    private final boolean hasPoisonDarts;

    public TrapFloor(String floorName, int trapDamage, boolean hasPoisonDarts) {
        this.floorName = floorName;
        this.trapDamage = trapDamage;
        this.hasPoisonDarts = hasPoisonDarts;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void announce() {
        System.out.println("\n--- WARNING: " + floorName + " ---");
        System.out.println("    The air reeks of danger...");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] Trap room detected.");
        if (hasPoisonDarts) System.out.println("[Setup] Poison dart launchers line the walls!");
        for (Hero h : party)
            if (h.isAlive()) System.out.println("        - " + h.statusLine());
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] Heroes push through the trap corridor!");
        int totalDamage = 0;

        for (Hero hero : party) {
            if (!hero.isAlive()) continue;
            hero.getState().onTurnStart(hero);

            int before = hero.getHp();
            hero.takeDamage(trapDamage);
            int actual = before - hero.getHp();
            totalDamage += actual;

            System.out.printf("  [TRAP] %s hits a spike trap! -%d HP -> [HP: %d/%d]%n",
                    hero.getName(), actual, hero.getHp(), hero.getMaxHp());

            if (hasPoisonDarts && hero.isAlive()) {
                System.out.printf("  [TRAP] A poison dart strikes %s!%n", hero.getName());
                hero.setState(new PoisonedState(2));
            }

            hero.getState().onTurnEnd(hero);

            if (!hero.isAlive())
                System.out.printf("  [DEAD] %s succumbs to the traps!%n", hero.getName());
        }

        boolean anyAlive = party.stream().anyMatch(Hero::isAlive);
        String summary = anyAlive
                ? "Trap corridor cleared! Took " + totalDamage + " total damage."
                : "The entire party was lost in the traps.";
        System.out.println("[Challenge] " + summary);
        return new FloorResult(anyAlive, totalDamage, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        if (!result.isCleared()) {
            System.out.println("[Loot] No loot — the party barely survived.");
            return false;
        }
        return true;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[Loot] Hidden chest found!");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(8);
                System.out.printf("       %s finds a healing potion (+8 HP -> %d/%d).%n",
                        hero.getName(), hero.getHp(), hero.getMaxHp());
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] Heroes regroup after the traps:");
        for (Hero hero : party)
            if (hero.isAlive()) System.out.println("          " + hero.statusLine());
    }
}