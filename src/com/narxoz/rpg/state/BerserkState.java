package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {

    private int turnsRemaining;

    public BerserkState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() {
        return "Berserk(" + turnsRemaining + " turns left)";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int)(basePower * 1.6);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 1.4);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.printf("  [BERSERK] %s rages! [ATK x1.6 / DEF -40%%]%n", hero.getName());
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        boolean hpRecovered = hero.getHp() > hero.getMaxHp() / 2;
        if (turnsRemaining <= 0 || hpRecovered) {
            System.out.printf("  [STATE] %s calms down from berserk rage.%n", hero.getName());
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}