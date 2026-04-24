package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private static final int POISON_DAMAGE = 8;
    private int turnsRemaining;

    public PoisonedState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() {
        return "Poisoned(" + turnsRemaining + " turns left)";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int)(basePower * 0.7);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 1.1);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.printf("  [POISON] %s takes %d poison damage! (HP: %d -> %d)%n",
                hero.getName(), POISON_DAMAGE, hero.getHp(),
                Math.max(0, hero.getHp() - POISON_DAMAGE));
        hero.takeDamageRaw(POISON_DAMAGE);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.printf("  [STATE] %s has recovered from poison!%n", hero.getName());
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}