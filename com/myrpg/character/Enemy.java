package com.myrpg.character;

public class Enemy {
    public enum Intent {
        ATTACK,
        HEAVY_ATTACK,
        RECOVER,
        FIREBALL
    }

    private final String name;
    private final int maxHp;
    private final int minDamage;
    private final int maxDamage;
    private final int goldReward;
    private final int expReward;
    private final boolean elite;
    private final boolean boss;
    private int hp;
    private int poisonTurns;
    private int poisonDamage;
    private int burnTurns;
    private int burnDamage;
    private int weaknessTurns;
    private Intent nextIntent;

    public Enemy(
            String name,
            int hp,
            int minDamage,
            int maxDamage,
            int goldReward,
            int expReward,
            boolean elite,
            boolean boss
    ) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.goldReward = goldReward;
        this.expReward = expReward;
        this.elite = elite;
        this.boss = boss;
        this.poisonTurns = 0;
        this.poisonDamage = 0;
        this.burnTurns = 0;
        this.burnDamage = 0;
        this.weaknessTurns = 0;
        planNextIntent();
    }

    public boolean processTurnEffects() {
        if (poisonTurns > 0) {
            hp -= poisonDamage;
            if (hp < 0) {
                hp = 0;
            }
            poisonTurns--;
            System.out.println(name + " 受到中毒效果，损失了 " + poisonDamage + " 点生命！");
        }

        if (burnTurns > 0 && !isDead()) {
            hp -= burnDamage;
            if (hp < 0) {
                hp = 0;
            }
            burnTurns--;
            System.out.println(name + " 受到灼烧效果，损失了 " + burnDamage + " 点生命！");
        }

        if (weaknessTurns > 0) {
            weaknessTurns--;
        }

        return !isDead();
    }

    public void executeTurn(Hero hero) {
        switch (nextIntent) {
            case ATTACK:
                performAttack(hero, minDamage, maxDamage, "发起了攻击！");
                break;
            case HEAVY_ATTACK:
                performAttack(hero, minDamage + 4, maxDamage + 6, "使出了重击！");
                break;
            case RECOVER:
                int heal = (int) (Math.random() * 8) + 8;
                int oldHp = hp;
                hp = Math.min(maxHp, hp + heal);
                System.out.println(name + " 恢复了 " + (hp - oldHp) + " 点生命！");
                break;
            case FIREBALL:
                System.out.println(name + " 释放了火焰法术！");
                hero.takeDamage(maxDamage + 2 + (int) (Math.random() * 4));
                hero.applyBurn(2, boss ? 6 : 4);
                break;
            default:
                break;
        }

        if (!isDead()) {
            planNextIntent();
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
        System.out.println(name + " 受到了 " + damage + " 点伤害！");
    }

    public void showStatus() {
        System.out.println(
                "敌人：" + name + getTitleTag()
                        + " | 生命：" + hp + "/" + maxHp
                        + " | 意图：" + getIntentDescription()
                        + getStatusDescription()
        );
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public boolean isBoss() {
        return boss;
    }

    public boolean isElite() {
        return elite;
    }

    public void applyPoison(int turns, int damage) {
        poisonTurns = Math.max(poisonTurns, turns);
        poisonDamage = Math.max(poisonDamage, damage);
        System.out.println(name + " 陷入了中毒状态！");
    }

    public void applyBurn(int turns, int damage) {
        burnTurns = Math.max(burnTurns, turns);
        burnDamage = Math.max(burnDamage, damage);
        System.out.println(name + " 陷入了灼烧状态！");
    }

    public void applyWeakness(int turns) {
        weaknessTurns = Math.max(weaknessTurns, turns);
        System.out.println(name + " 的攻击被削弱了！");
    }

    private void planNextIntent() {
        int roll = (int) (Math.random() * 100);

        if (hp <= maxHp / 3 && roll < 30) {
            nextIntent = Intent.RECOVER;
            return;
        }

        if (boss) {
            if (roll < 30) {
                nextIntent = Intent.FIREBALL;
            } else {
                nextIntent = roll < 65 ? Intent.HEAVY_ATTACK : Intent.ATTACK;
            }
        } else if (elite) {
            if (roll < 20) {
                nextIntent = Intent.FIREBALL;
            } else {
                nextIntent = roll < 55 ? Intent.HEAVY_ATTACK : Intent.ATTACK;
            }
        } else {
            nextIntent = roll < 25 ? Intent.HEAVY_ATTACK : Intent.ATTACK;
        }
    }

    private void performAttack(Hero hero, int low, int high, String actionText) {
        int damage = (int) (Math.random() * (high - low + 1)) + low;
        if (weaknessTurns > 0) {
            damage = Math.max(1, damage - 3);
            System.out.println(name + " 处于虚弱状态，造成的伤害下降了。");
        }

        System.out.println(name + actionText);
        hero.takeDamage(damage);
    }

    private String getIntentDescription() {
        switch (nextIntent) {
            case ATTACK:
                return "普通攻击";
            case HEAVY_ATTACK:
                return "重击蓄势";
            case RECOVER:
                return "恢复生命";
            case FIREBALL:
                return "火焰法术";
            default:
                return "未知";
        }
    }

    private String getTitleTag() {
        if (boss) {
            return "(Boss)";
        }
        if (elite) {
            return "(精英)";
        }
        return "";
    }

    private String getStatusDescription() {
        StringBuilder builder = new StringBuilder();
        if (poisonTurns > 0) {
            builder.append(" | 中毒:").append(poisonTurns);
        }
        if (burnTurns > 0) {
            builder.append(" | 灼烧:").append(burnTurns);
        }
        if (weaknessTurns > 0) {
            builder.append(" | 虚弱:").append(weaknessTurns);
        }
        return builder.toString();
    }
}
