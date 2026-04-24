package com.narxoz.rpg.combatant;

public class Monster {

    private final String name;
    private int hp;
    private final int attackPower;

    public Monster(String name, int hp, int attackPower) {
        this.name = name;
        this.hp = hp;
        this.attackPower = attackPower;
    }

    public String getName()       { return name; }
    public int getHp()            { return hp; }
    public int getAttackPower()   { return attackPower; }
    public boolean isAlive()      { return hp > 0; }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void attack(Hero hero) {
        int damage = Math.max(1, this.attackPower - 2);
        hero.takeDamage(damage);
    }
}