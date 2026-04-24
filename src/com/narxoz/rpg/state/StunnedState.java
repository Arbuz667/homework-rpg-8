package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    private int turnsRemaining;

    public StunnedState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() {
        return "Stunned(" + turnsRemaining + " turns left)";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int)(basePower * 0.5);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 1.3);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.printf("  [STUN] %s is stunned and cannot act!%n", hero.getName());
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.printf("  [STATE] %s shakes off the stun!%n", hero.getName());
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return false;
    }
}