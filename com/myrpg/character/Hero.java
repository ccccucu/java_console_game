package com.myrpg.character;

import com.myrpg.items.Equipment;

public class Hero {
    private final String name;
    private final HeroClass heroClass;
    private int maxHp;
    private int hp;
    private int potions;
    private int attackPower;
    private int defense;
    private int gold;
    private int level;
    private int exp;
    private int critChance;
    private int skillOneCooldown;
    private int skillTwoCooldown;
    private int temporaryGuard;
    private int burnTurns;
    private int burnDamage;
    private Equipment weapon;
    private Equipment armor;
    private Equipment accessory;

    public Hero(String name, int hp, HeroClass heroClass) {
        this.name = name;
        this.heroClass = heroClass;
        this.maxHp = hp;
        this.hp = hp;
        this.potions = 3;
        this.gold = 0;
        this.level = 1;
        this.exp = 0;
        this.skillOneCooldown = 0;
        this.skillTwoCooldown = 0;
        this.temporaryGuard = 0;
        this.burnTurns = 0;
        this.burnDamage = 0;

        switch (heroClass) {
            case WARRIOR:
                attackPower = 11;
                defense = 2;
                critChance = 10;
                break;
            case ROGUE:
                attackPower = 10;
                defense = 1;
                critChance = 20;
                break;
            default:
                attackPower = 12;
                defense = 1;
                critChance = 12;
                maxHp -= 6;
                this.hp = maxHp;
                break;
        }
    }

    public void showStatus() {
        System.out.println(
                "英雄：" + name
                        + " | 职业：" + heroClass.getDisplayName()
                        + " | 等级：" + level
                        + " | 生命：" + hp + "/" + maxHp
                        + " | 攻击：" + attackPower
                        + " | 防御：" + defense
                        + " | 暴击：" + critChance + "%"
                        + " | 药水：" + potions
                        + " | 金币：" + gold
        );
        System.out.println(
                "装备：武器[" + getEquipmentName(weapon)
                        + "] 护甲[" + getEquipmentName(armor)
                        + "] 饰品[" + getEquipmentName(accessory) + "]"
        );
        System.out.println(
                "技能：" + getSkillOneName() + "(" + skillOneCooldown + ")"
                        + " / " + getSkillTwoName() + "(" + skillTwoCooldown + ")"
                        + (temporaryGuard > 0 ? " | 护盾：" + temporaryGuard : "")
        );
    }

    public String getName() {
        return name;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPotions() {
        return potions;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public int getGold() {
        return gold;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getCritChance() {
        return critChance;
    }

    public int getSkillOneCooldown() {
        return skillOneCooldown;
    }

    public int getSkillTwoCooldown() {
        return skillTwoCooldown;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean processTurnEffects() {
        if (burnTurns > 0) {
            hp -= burnDamage;
            if (hp < 0) {
                hp = 0;
            }
            burnTurns--;
            System.out.println(name + " 受到灼烧效果，损失了 " + burnDamage + " 点生命！");
        }
        return !isDead();
    }

    public void takeDamage(int damage) {
        int guardAbsorb = temporaryGuard;
        int actualDamage = Math.max(1, damage - defense - guardAbsorb);
        if (temporaryGuard > 0) {
            System.out.println(name + " 的护盾吸收了部分伤害！");
            temporaryGuard = 0;
        }

        hp -= actualDamage;
        if (hp < 0) {
            hp = 0;
        }
        System.out.println(name + " 受到了 " + actualDamage + " 点伤害！");
    }

    public void performBasicAttack(Enemy enemy) {
        int damage = baseAttackRoll();
        if (rollCrit()) {
            damage += Math.max(3, damage / 2);
            System.out.println("暴击！" + name + " 的普通攻击造成了更高伤害！");
        }

        System.out.println(name + " 发起攻击！");
        enemy.takeDamage(damage);
    }

    public boolean canUseSkillOne() {
        return skillOneCooldown == 0;
    }

    public boolean canUseSkillTwo() {
        return skillTwoCooldown == 0;
    }

    public String getSkillOneName() {
        switch (heroClass) {
            case WARRIOR:
                return "裂地斩";
            case ROGUE:
                return "毒刃";
            default:
                return "火球术";
        }
    }

    public String getSkillTwoName() {
        switch (heroClass) {
            case WARRIOR:
                return "战吼";
            case ROGUE:
                return "影袭";
            default:
                return "冰霜护盾";
        }
    }

    public void useSkillOne(Enemy enemy) {
        skillOneCooldown = 3;
        switch (heroClass) {
            case WARRIOR:
                System.out.println(name + " 使出裂地斩！");
                enemy.takeDamage(attackPower + 12 + (int) (Math.random() * 6));
                enemy.applyWeakness(2);
                break;
            case ROGUE:
                System.out.println(name + " 用毒刃刺中了敌人！");
                enemy.takeDamage(attackPower + 7 + (int) (Math.random() * 5));
                enemy.applyPoison(3, 4);
                break;
            default:
                System.out.println(name + " 释放了火球术！");
                enemy.takeDamage(attackPower + 10 + (int) (Math.random() * 7));
                enemy.applyBurn(3, 5);
                break;
        }
    }

    public void useSkillTwo(Enemy enemy) {
        skillTwoCooldown = 4;
        switch (heroClass) {
            case WARRIOR:
                System.out.println(name + " 发出了震耳欲聋的战吼！");
                gainGuard(8);
                heal(12);
                break;
            case ROGUE:
                System.out.println(name + " 使出了影袭，快速连击两次！");
                enemy.takeDamage(baseAttackRoll() + 3);
                enemy.takeDamage(baseAttackRoll() + 3);
                break;
            default:
                System.out.println(name + " 施放了冰霜护盾！");
                gainGuard(10);
                enemy.applyWeakness(2);
                break;
        }
    }

    public void tickSkillCooldowns() {
        if (skillOneCooldown > 0) {
            skillOneCooldown--;
        }
        if (skillTwoCooldown > 0) {
            skillTwoCooldown--;
        }
    }

    public void gainGuard(int amount) {
        temporaryGuard += amount;
        System.out.println(name + " 获得了 " + amount + " 点护盾！");
    }

    public void applyBurn(int turns, int damage) {
        burnTurns = Math.max(burnTurns, turns);
        burnDamage = Math.max(burnDamage, damage);
        System.out.println(name + " 被施加了灼烧效果！");
    }

    public boolean usePotion() {
        if (potions <= 0) {
            System.out.println(name + " 想使用药水，但已经没有存货了。");
            return false;
        }

        if (hp == maxHp) {
            System.out.println(name + " 当前生命值已满，无需使用药水。");
            return false;
        }

        potions--;
        heal((int) (Math.random() * 16) + 14);
        return true;
    }

    public void heal(int amount) {
        if (amount <= 0) {
            return;
        }

        int oldHp = hp;
        hp = Math.min(maxHp, hp + amount);
        System.out.println(name + " 恢复了 " + (hp - oldHp) + " 点生命！");
    }

    public void fullRecover() {
        hp = maxHp;
        System.out.println(name + " 的状态完全恢复了！");
    }

    public void addPotion() {
        addPotions(1);
    }

    public void addPotions(int amount) {
        potions += amount;
        System.out.println(name + " 获得了 " + amount + " 瓶药水！");
    }

    public void addGold(int amount) {
        gold += amount;
        System.out.println(name + " 获得了 " + amount + " 枚金币！");
    }

    public boolean spendGold(int amount) {
        if (gold < amount) {
            System.out.println("金币不足，当前只有 " + gold + " 枚。");
            return false;
        }

        gold -= amount;
        System.out.println(name + " 花费了 " + amount + " 枚金币。");
        return true;
    }

    public void increaseAttackPower(int amount) {
        attackPower += amount;
        System.out.println(name + " 的攻击提升了 " + amount + " 点！");
    }

    public void increaseDefense(int amount) {
        defense += amount;
        System.out.println(name + " 的防御提升了 " + amount + " 点！");
    }

    public void increaseCritChance(int amount) {
        critChance += amount;
        System.out.println(name + " 的暴击率提升了 " + amount + "%！");
    }

    public void increaseMaxHp(int amount) {
        maxHp += amount;
        hp = Math.min(maxHp, hp + amount);
        System.out.println(name + " 的最大生命提升了 " + amount + " 点！");
    }

    public void gainExp(int amount) {
        exp += amount;
        System.out.println(name + " 获得了 " + amount + " 点经验！");

        int needExp = level * 25;
        while (exp >= needExp) {
            exp -= needExp;
            level++;
            maxHp += 8;
            hp = maxHp;
            attackPower += 2;
            if (level % 2 == 0) {
                defense++;
            }
            if (heroClass == HeroClass.ROGUE) {
                critChance += 2;
            }

            System.out.println("升级了！" + name + " 达到了 " + level + " 级。");
            System.out.println("升级奖励：生命上限 +8，攻击 +2，生命已回满。");
            if (level % 2 == 0) {
                System.out.println("额外奖励：防御 +1。");
            }
            if (heroClass == HeroClass.ROGUE) {
                System.out.println("职业成长：暴击率 +2%。");
            }

            needExp = level * 25;
        }
    }

    public void equip(Equipment equipment) {
        Equipment oldEquipment;
        switch (equipment.getSlot()) {
            case WEAPON:
                oldEquipment = weapon;
                weapon = equipment;
                break;
            case ARMOR:
                oldEquipment = armor;
                armor = equipment;
                break;
            default:
                oldEquipment = accessory;
                accessory = equipment;
                break;
        }

        if (oldEquipment != null) {
            applyEquipmentBonus(oldEquipment, -1);
            System.out.println(name + " 卸下了 " + oldEquipment.getDisplayName() + "。");
        }

        applyEquipmentBonus(equipment, 1);
        System.out.println(name + " 装备了 " + equipment.getDisplayName() + "。");
        System.out.println("效果：" + equipment.getFullDescription());
    }

    public String getEquipmentSummary() {
        return "武器[" + getEquipmentName(weapon) + "] 护甲[" + getEquipmentName(armor) + "] 饰品[" + getEquipmentName(accessory) + "]";
    }

    private void applyEquipmentBonus(Equipment equipment, int direction) {
        maxHp += equipment.getHpBonus() * direction;
        attackPower += equipment.getAttackBonus() * direction;
        defense += equipment.getDefenseBonus() * direction;
        critChance += equipment.getCritBonus() * direction;

        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    private int baseAttackRoll() {
        return Math.max(1, attackPower + (int) (Math.random() * 7) - 2);
    }

    private boolean rollCrit() {
        return Math.random() * 100 < critChance;
    }

    private String getEquipmentName(Equipment equipment) {
        return equipment == null ? "无" : equipment.getDisplayName();
    }
}
