package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class RegeneratingState implements HeroState {

    private static final int REGEN_AMOUNT = 10;
    private int turnsRemaining;

    public RegeneratingState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() {
        return "Regenerating(" + turnsRemaining + " turns left)";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return basePower;
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 0.9);
    }

    @Override
    public void onTurnStart(Hero hero) {
        int before = hero.getHp();
        hero.heal(REGEN_AMOUNT);
        System.out.printf("  [REGEN] %s regenerates %d HP! (HP: %d -> %d)%n",
                hero.getName(), REGEN_AMOUNT, before, hero.getHp());
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.printf("  [STATE] %s's regeneration fades.%n", hero.getName());
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}