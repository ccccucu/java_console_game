package com.myrpg.items;

public class EquipmentFactory {
    public static Equipment createRandomEquipment(int tier) {
        Equipment[] pool;
        Equipment.Rarity rarity = rollRarity(tier);

        if (tier <= 2) {
            pool = new Equipment[]{
                    createEquipment("铁剑", Equipment.Slot.WEAPON, 0, 3, 0, 0, "朴素但可靠的近战武器", 18, rarity),
                    createEquipment("猎弓", Equipment.Slot.WEAPON, 0, 2, 0, 6, "提高暴击率的轻型武器", 18, rarity),
                    createEquipment("皮甲", Equipment.Slot.ARMOR, 8, 0, 1, 0, "轻便护甲，适合长途冒险", 16, rarity),
                    createEquipment("护符", Equipment.Slot.ACCESSORY, 10, 0, 0, 4, "增加生命与暴击", 16, rarity),
                    createEquipment("木盾", Equipment.Slot.ARMOR, 0, 0, 2, 0, "基础的防御装备", 16, rarity)
            };
        } else if (tier <= 4) {
            pool = new Equipment[]{
                    createEquipment("骑士长剑", Equipment.Slot.WEAPON, 0, 5, 0, 3, "稳健的中阶武器", 28, rarity),
                    createEquipment("淬毒匕首", Equipment.Slot.WEAPON, 0, 4, 0, 8, "锋利而致命", 28, rarity),
                    createEquipment("锁子甲", Equipment.Slot.ARMOR, 14, 0, 2, 0, "提供坚实防护", 26, rarity),
                    createEquipment("狩猎披风", Equipment.Slot.ACCESSORY, 6, 1, 1, 8, "提升机动性与暴击", 26, rarity),
                    createEquipment("坚石戒指", Equipment.Slot.ACCESSORY, 12, 0, 2, 0, "增加耐久", 26, rarity)
            };
        } else {
            pool = new Equipment[]{
                    createEquipment("龙牙大剑", Equipment.Slot.WEAPON, 0, 7, 0, 6, "沉重但爆发极强", 42, rarity),
                    createEquipment("星火法杖", Equipment.Slot.WEAPON, 6, 5, 0, 4, "强化法术威力", 40, rarity),
                    createEquipment("圣银战甲", Equipment.Slot.ARMOR, 22, 0, 3, 0, "高阶防具", 40, rarity),
                    createEquipment("影舞坠饰", Equipment.Slot.ACCESSORY, 10, 2, 1, 10, "擅长制造致命一击", 42, rarity),
                    createEquipment("王者徽记", Equipment.Slot.ACCESSORY, 16, 2, 2, 4, "全面强化角色属性", 44, rarity)
            };
        }

        return pool[(int) (Math.random() * pool.length)];
    }

    private static Equipment createEquipment(
            String name,
            Equipment.Slot slot,
            int hpBonus,
            int attackBonus,
            int defenseBonus,
            int critBonus,
            String description,
            int baseValue,
            Equipment.Rarity rarity
    ) {
        double multiplier = rarity.getMultiplier();
        return new Equipment(
                name,
                slot,
                scaleStat(hpBonus, multiplier),
                scaleStat(attackBonus, multiplier),
                scaleStat(defenseBonus, multiplier),
                scaleStat(critBonus, multiplier),
                description,
                rarity,
                Math.max(8, (int) Math.round(baseValue * multiplier))
        );
    }

    private static int scaleStat(int value, double multiplier) {
        if (value == 0) {
            return 0;
        }
        return Math.max(1, (int) Math.round(value * multiplier));
    }

    private static Equipment.Rarity rollRarity(int tier) {
        int roll = (int) (Math.random() * 100);
        if (tier >= 5) {
            if (roll < 15) {
                return Equipment.Rarity.LEGENDARY;
            }
            if (roll < 45) {
                return Equipment.Rarity.EPIC;
            }
            if (roll < 80) {
                return Equipment.Rarity.RARE;
            }
            return Equipment.Rarity.COMMON;
        }

        if (tier >= 3) {
            if (roll < 8) {
                return Equipment.Rarity.LEGENDARY;
            }
            if (roll < 28) {
                return Equipment.Rarity.EPIC;
            }
            if (roll < 65) {
                return Equipment.Rarity.RARE;
            }
            return Equipment.Rarity.COMMON;
        }

        if (roll < 5) {
            return Equipment.Rarity.EPIC;
        }
        if (roll < 30) {
            return Equipment.Rarity.RARE;
        }
        return Equipment.Rarity.COMMON;
    }
}
