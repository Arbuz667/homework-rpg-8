package com.narxoz.rpg.tower;

public class TowerRunResult {

    private final int floorsCleared;
    private final int heroesSurviving;
    private final boolean reachedTop;

    public TowerRunResult(int floorsCleared, int heroesSurviving, boolean reachedTop) {
        this.floorsCleared = floorsCleared;
        this.heroesSurviving = heroesSurviving;
        this.reachedTop = reachedTop;
    }

    public int getFloorsCleared()     { return floorsCleared; }
    public int getHeroesSurviving()   { return heroesSurviving; }
    public boolean isReachedTop()     { return reachedTop; }
}