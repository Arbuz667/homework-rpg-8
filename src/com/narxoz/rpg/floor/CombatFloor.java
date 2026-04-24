package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.ArrayList;
import java.util.List;

public class CombatFloor extends TowerFloor {

    private final String floorName;
    private final List<Monster> monsters = new ArrayList<>();
    private int totalDamageDealt;

    public CombatFloor(String floorName, Monster... spawnedMonsters) {
        this.floorName = floorName;
        for (Monster m : spawnedMonsters) monsters.add(m);
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        totalDamageDealt = 0;
        System.out.println("[Setup] Monsters appear:");
        for (Monster m : monsters)
            System.out.printf("        - %s (HP: %d | ATK: %d)%n", m.getName(), m.getHp(), m.getAttackPower());
        System.out.println("[Setup] Heroes ready:");
        for (Hero h : party)
            if (h.isAlive()) System.out.println("        - " + h.statusLine());
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] Combat begins!");
        int round = 1;

        while (anyAlive(monsters) && anyHeroAlive(party)) {
            System.out.println("\n  -- Round " + round + " --");

            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.getState().onTurnStart(hero);
                if (!hero.isAlive()) { System.out.printf("  [DEAD] %s falls!%n", hero.getName()); continue; }

                if (!hero.getState().canAct()) {
                    hero.getState().onTurnEnd(hero);
                    continue;
                }

                for (Monster monster : monsters) {
                    if (monster.isAlive()) {
                        int dmg = hero.attackMonster(monster);
                        System.out.printf("  [ATK] %s attacks %s for %d dmg! (%s HP: %d)%n",
                                hero.getName(), monster.getName(), dmg, monster.getName(), monster.getHp());
                        break;
                    }
                }
                hero.getState().onTurnEnd(hero);
            }

            for (Monster monster : monsters) {
                if (!monster.isAlive()) continue;
                Hero target = getFirstAliveHero(party);
                if (target == null) break;

                int before = target.getHp();
                monster.attack(target);
                int dmgDealt = before - target.getHp();
                totalDamageDealt += dmgDealt;

                System.out.printf("  [MON] %s attacks %s for %d dmg! (%s HP: %d/%d)%n",
                        monster.getName(), target.getName(), dmgDealt,
                        target.getName(), target.getHp(), target.getMaxHp());

                if (monster.getAttackPower() >= 18 && dmgDealt > 0)
                    applyStatusEffect(target, monster.getAttackPower());

                if (!target.isAlive())
                    System.out.printf("  [DEAD] %s has been slain!%n", target.getName());
            }
            round++;
        }

        boolean cleared = !anyAlive(monsters);
        String summary = cleared
                ? "All monsters defeated! Party took " + totalDamageDealt + " total damage."
                : "Party wiped.";
        System.out.println("[Challenge] " + summary);
        return new FloorResult(cleared, totalDamageDealt, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (!result.isCleared()) return;
        System.out.println("[Loot] Party finds supplies!");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(5);
                System.out.printf("       %s recovers 5 HP. (%d/%d)%n",
                        hero.getName(), hero.getHp(), hero.getMaxHp());
            }
        }
    }

    private boolean anyAlive(List<Monster> list) {
        return list.stream().anyMatch(Monster::isAlive);
    }

    private boolean anyHeroAlive(List<Hero> party) {
        return party.stream().anyMatch(Hero::isAlive);
    }

    private Hero getFirstAliveHero(List<Hero> party) {
        return party.stream().filter(Hero::isAlive).findFirst().orElse(null);
    }

    private void applyStatusEffect(Hero hero, int monsterAtk) {
        if (monsterAtk >= 22) {
            System.out.printf("  [STATUS] Monster inflicts STUN on %s!%n", hero.getName());
            hero.setState(new StunnedState(2));
        } else if (monsterAtk >= 18) {
            System.out.printf("  [STATUS] Monster inflicts POISON on %s!%n", hero.getName());
            hero.setState(new PoisonedState(3));
        }
    }
}