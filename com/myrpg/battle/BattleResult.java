package com.myrpg.battle;

public class BattleResult {
    public enum Outcome {
        VICTORY,
        DEFEAT,
        ESCAPE
    }

    private final Outcome outcome;
    private final String enemyName;
    private final int goldReward;
    private final int expReward;

    public BattleResult(Outcome outcome, String enemyName, int goldReward, int expReward) {
        this.outcome = outcome;
        this.enemyName = enemyName;
        this.goldReward = goldReward;
        this.expReward = expReward;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public int getExpReward() {
        return expReward;
    }
}
