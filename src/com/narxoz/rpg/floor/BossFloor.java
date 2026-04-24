package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class BossFloor extends TowerFloor {

    private final String floorName;
    private final Monster boss;
    private int totalDamageDealt;

    public BossFloor(String floorName, Monster boss) {
        this.floorName = floorName;
        this.boss = boss;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void announce() {
        System.out.println("\n=== BOSS FLOOR: " + floorName + " ===");
        System.out.println("    The ground trembles. A shadow looms...");
        System.out.printf("    %s awakens! HP: %d | ATK: %d%n",
                boss.getName(), boss.getHp(), boss.getAttackPower());
    }

    @Override
    protected void setup(List<Hero> party) {
        totalDamageDealt = 0;
        System.out.println("[Setup] Heroes steel themselves:");
        for (Hero h : party)
            if (h.isAlive()) System.out.println("        - " + h.statusLine());
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] Boss fight begins!");
        int round = 1;

        while (boss.isAlive() && anyHeroAlive(party)) {
            System.out.println("\n  -- Round " + round + " --");

            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.getState().onTurnStart(hero);
                if (!hero.isAlive()) { System.out.printf("  [DEAD] %s falls!%n", hero.getName()); continue; }

                if (hero.getHp() < hero.getMaxHp() * 0.3
                        && !hero.getState().getName().startsWith("Berserk")) {
                    System.out.printf("  [RAGE] %s is near death — entering BERSERK!%n", hero.getName());
                    hero.setState(new BerserkState(3));
                }

                if (!hero.getState().canAct()) {
                    hero.getState().onTurnEnd(hero);
                    continue;
                }

                int dmg = hero.attackMonster(boss);
                System.out.printf("  [ATK] %s strikes the %s for %d dmg! (Boss HP: %d)%n",
                        hero.getName(), boss.getName(), dmg, boss.getHp());
                hero.getState().onTurnEnd(hero);
            }

            if (!boss.isAlive()) break;

            System.out.printf("  [BOSS] %s unleashes a devastating attack!%n", boss.getName());
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                int before = hero.getHp();
                boss.attack(hero);
                int dmgDealt = before - hero.getHp();
                totalDamageDealt += dmgDealt;
                System.out.printf("      -> %s takes %d dmg! (HP: %d/%d)%n",
                        hero.getName(), dmgDealt, hero.getHp(), hero.getMaxHp());

                if (round % 3 == 0 && hero.isAlive()) {
                    System.out.printf("  [BOSS] Shockwave STUNS %s!%n", hero.getName());
                    hero.setState(new StunnedState(1));
                }
                if (!hero.isAlive())
                    System.out.printf("  [DEAD] %s has been defeated!%n", hero.getName());
            }
            round++;
        }

        boolean cleared = !boss.isAlive();
        String summary = cleared
                ? boss.getName() + " defeated! Total dmg taken: " + totalDamageDealt
                : "The party was defeated by " + boss.getName() + "!";
        System.out.println("[Challenge] " + summary);
        return new FloorResult(cleared, totalDamageDealt, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (!result.isCleared()) return;
        System.out.println("[Loot] Boss drops legendary treasure!");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(20);
                System.out.printf("       %s receives blessed healing (+20 HP -> %d/%d).%n",
                        hero.getName(), hero.getHp(), hero.getMaxHp());
            }
        }
    }

    private boolean anyHeroAlive(List<Hero> party) {
        return party.stream().anyMatch(Hero::isAlive);
    }
}