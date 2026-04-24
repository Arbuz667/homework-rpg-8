package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public interface HeroState {
    String getName();
    int modifyOutgoingDamage(int basePower);
    int modifyIncomingDamage(int rawDamage);
    void onTurnStart(Hero hero);
    void onTurnEnd(Hero hero);
    boolean canAct();
}