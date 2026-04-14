package com.myrpg.items;

import com.myrpg.character.Hero;

public class Equipment extends Item {
    public enum Rarity {
        COMMON("普通", 1.0),
        RARE("稀有", 1.25),
        EPIC("史诗", 1.55),
        LEGENDARY("传说", 1.9);

        private final String displayName;
        private final double multiplier;

        Rarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() {
            return displayName;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }

    public enum Slot {
        WEAPON("武器"),
        ARMOR("护甲"),
        ACCESSORY("饰品");

        private final String displayName;

        Slot(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private final Slot slot;
    private final int hpBonus;
    private final int attackBonus;
    private final int defenseBonus;
    private final int critBonus;
    private final String description;
    private final Rarity rarity;
    private final int value;

    public Equipment(
            String name,
            Slot slot,
            int hpBonus,
            int attackBonus,
            int defenseBonus,
            int critBonus,
            String description,
            Rarity rarity,
            int value
    ) {
        super(name);
        this.slot = slot;
        this.hpBonus = hpBonus;
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.critBonus = critBonus;
        this.description = description;
        this.rarity = rarity;
        this.value = value;
    }

    public Slot getSlot() {
        return slot;
    }

    public int getHpBonus() {
        return hpBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getCritBonus() {
        return critBonus;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return "[" + rarity.getDisplayName() + "]" + getName();
    }

    public String getFullDescription() {
        return getDisplayName()
                + " | " + description
                + " | 生命+" + hpBonus
                + " 攻击+" + attackBonus
                + " 防御+" + defenseBonus
                + " 暴击+" + critBonus + "%";
    }

    @Override
    public void use(Hero hero) {
        hero.equip(this);
    }
}
