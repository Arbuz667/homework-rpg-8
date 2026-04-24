package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public abstract class TowerFloor {

    public final FloorResult explore(List<Hero> party) {
        announce();
        setup(party);
        FloorResult result = resolveChallenge(party);
        if (shouldAwardLoot(result)) {
            awardLoot(party, result);
        }
        cleanup(party);
        return result;
    }

    protected void announce() {
        System.out.println("\n--- Entering " + getFloorName() + " ---");
    }

    protected abstract void setup(List<Hero> party);

    protected abstract FloorResult resolveChallenge(List<Hero> party);

    protected boolean shouldAwardLoot(FloorResult result) {
        return true;
    }

    protected abstract void awardLoot(List<Hero> party, FloorResult result);

    protected void cleanup(List<Hero> party) {}

    protected abstract String getFloorName();
}