package com.myrpg.game;

import com.myrpg.character.Hero;
import com.myrpg.items.Equipment;
import com.myrpg.items.EquipmentFactory;

import java.util.Scanner;

public class RandomEvent {
    public static void trigger(Hero hero, Scanner scanner, int tier) {
        int eventType = (int) (Math.random() * 3);

        switch (eventType) {
            case 0:
                ancientShrine(hero, scanner);
                break;
            case 1:
                hiddenCache(hero, scanner, tier);
                break;
            default:
                riskyAltar(hero, scanner);
                break;
        }
    }

    private static void ancientShrine(Hero hero, Scanner scanner) {
        System.out.println("\n========== 随机事件：古老祭坛 ==========");
        System.out.println("你发现了一座古老祭坛，符文仍闪烁着微光。");
        System.out.println("1 - 祈求生命：最大生命 +12");
        System.out.println("2 - 祈求力量：攻击 +2，但立刻失去 8 点生命");
        System.out.println("3 - 离开");

        String input = readOption(scanner);
        switch (input) {
            case "1":
                hero.increaseMaxHp(12);
                break;
            case "2":
                hero.increaseAttackPower(2);
                hero.takeDamage(8);
                break;
            default:
                System.out.println("你谨慎地离开了祭坛。");
                break;
        }
    }

    private static void hiddenCache(Hero hero, Scanner scanner, int tier) {
        Equipment reward = EquipmentFactory.createRandomEquipment(tier + 1);
        System.out.println("\n========== 随机事件：隐秘补给 ==========");
        System.out.println("你找到了一处被遗忘的冒险者补给点。");
        System.out.println("1 - 拿走 25 金币");
        System.out.println("2 - 拿走 2 瓶药水");
        System.out.println("3 - 拿走装备：" + reward.getDisplayName());
        System.out.println("    " + reward.getFullDescription());

        String input = readOption(scanner);
        switch (input) {
            case "1":
                hero.addGold(25);
                break;
            case "2":
                hero.addPotions(2);
                break;
            default:
                reward.use(hero);
                break;
        }
    }

    private static void riskyAltar(Hero hero, Scanner scanner) {
        System.out.println("\n========== 随机事件：危险契约 ==========");
        System.out.println("黑雾中的声音邀请你签下契约，换取更强的力量。");
        System.out.println("1 - 接受契约：暴击率 +6%，防御 -1");
        System.out.println("2 - 拒绝契约：恢复 12 点生命");
        System.out.println("3 - 强行掠夺：获得 35 金币，但受到 10 点伤害");

        String input = readOption(scanner);
        switch (input) {
            case "1":
                hero.increaseCritChance(6);
                hero.increaseDefense(-1);
                break;
            case "2":
                hero.heal(12);
                break;
            default:
                hero.addGold(35);
                hero.takeDamage(10);
                break;
        }
    }

    private static String readOption(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            System.out.println("输入已结束，默认执行最后一个选项。");
            return "3";
        }

        String input = scanner.nextLine().trim();
        if ("1".equals(input) || "2".equals(input) || "3".equals(input)) {
            return input;
        }

        System.out.println("输入无效，默认执行最后一个选项。");
        return "3";
    }
}
