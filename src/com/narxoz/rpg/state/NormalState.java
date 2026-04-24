package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class NormalState implements HeroState {

    @Override
    public String getName() {
        return "Normal";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return basePower;
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return rawDamage;
    }

    @Override
    public void onTurnStart(Hero hero) {}

    @Override
    public void onTurnEnd(Hero hero) {}

    @Override
    public boolean canAct() {
        return true;
    }
}