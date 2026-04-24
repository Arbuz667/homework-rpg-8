package com.narxoz.rpg.floor;

public class FloorResult {

    private final boolean cleared;
    private final int damageTaken;
    private final String summary;

    public FloorResult(boolean cleared, int damageTaken, String summary) {
        this.cleared = cleared;
        this.damageTaken = damageTaken;
        this.summary = summary;
    }

    public boolean isCleared()      { return cleared; }
    public int getDamageTaken()     { return damageTaken; }
    public String getSummary()      { return summary; }
}