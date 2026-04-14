package com.myrpg.character;

public enum HeroClass {
    WARRIOR("战士"),
    ROGUE("游侠"),
    MAGE("法师");

    private final String displayName;

    HeroClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
