package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.RegeneratingState;
import java.util.List;

public class RestFloor extends TowerFloor {

    private final String floorName;
    private final int healAmount;

    public RestFloor(String floorName, int healAmount) {
        this.floorName = floorName;
        this.healAmount = healAmount;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] A peaceful sanctuary awaits.");
        for (Hero h : party)
            if (h.isAlive()) System.out.println("        - " + h.statusLine());
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] Heroes rest and recover...");
        int totalHealed = 0;

        for (Hero hero : party) {
            if (!hero.isAlive()) continue;

            if (!hero.getState().getName().equals("Normal")
                    && !hero.getState().getName().startsWith("Regenerating")) {
                System.out.printf("  [CLEANSE] Sanctuary cleanses %s's %s!%n",
                        hero.getName(), hero.getState().getName());
                hero.setState(new NormalState());
            }

            int before = hero.getHp();
            hero.heal(healAmount);
            totalHealed += hero.getHp() - before;
            System.out.printf("  [REST] %s recovers %d HP. (%d/%d)%n",
                    hero.getName(), hero.getHp() - before, hero.getHp(), hero.getMaxHp());

            hero.setState(new RegeneratingState(2));
        }

        String summary = "Rest complete. Party healed " + totalHealed + " total HP.";
        System.out.println("[Challenge] " + summary);
        return new FloorResult(true, 0, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        System.out.println("[Loot] No treasure here — this is a place of rest.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] Heroes leave the sanctuary refreshed:");
        for (Hero hero : party)
            if (hero.isAlive()) System.out.println("          " + hero.statusLine());
    }
}