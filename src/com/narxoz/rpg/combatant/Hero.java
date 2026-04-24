package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = new NormalState();
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }
    public HeroState getState()    { return state; }

    public void setState(HeroState newState) {
        System.out.printf("  [STATE] %s: %s -> %s%n", name, state.getName(), newState.getName());
        this.state = newState;
    }

    public void takeDamage(int amount) {
        int modified = state.modifyIncomingDamage(amount);
        hp = Math.max(0, hp - modified);
    }

    public void takeDamageRaw(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public int attackMonster(Monster monster) {
        int dmg = state.modifyOutgoingDamage(attackPower);
        monster.takeDamage(dmg);
        return dmg;
    }

    public String statusLine() {
        return String.format("%s [HP: %d/%d | State: %s]",
                name, hp, maxHp, state.getName());
    }
}