package com.myrpg.game;

import com.myrpg.battle.BattleResult;
import com.myrpg.battle.CombatManager;
import com.myrpg.character.Enemy;
import com.myrpg.character.Hero;
import com.myrpg.items.Equipment;
import com.myrpg.items.EquipmentFactory;

import java.util.Scanner;

public class GameSession {
    private static final int TOTAL_STAGES = 7;

    public static void startAdventure(Hero hero, Scanner scanner) {
        System.out.println("\n==============================");
        System.out.println("冒险开始！本局共有 " + TOTAL_STAGES + " 个节点。");
        System.out.println("你将通过分支路线经历战斗、事件、商店、宝库与最终 Boss 战。");
        System.out.println("==============================");

        int clearedStages = 0;
        int victoryCount = 0;
        RouteChoice midRoute = chooseMidRoute(scanner);
        NodeType[] midNodes = getMidRouteNodes(midRoute);

        System.out.println("\n========== 节点 1 / " + TOTAL_STAGES + " ==========");
        if (!runBattleNode(hero, scanner, 1, false, false)) {
            finishAdventure(hero, clearedStages, victoryCount, false, midRoute.displayName + " -> 未抵达终局");
            return;
        }
        clearedStages++;
        victoryCount++;

        for (int i = 0; i < midNodes.length; i++) {
            int stage = i + 2;
            System.out.println("\n========== 节点 " + stage + " / " + TOTAL_STAGES + " ==========");
            NodeResult result = executeNode(midNodes[i], hero, scanner, stage);
            if (!result.continueRun || hero.isDead()) {
                finishAdventure(hero, clearedStages, victoryCount, false, midRoute.displayName + " -> 未抵达终局");
                return;
            }
            clearedStages++;
            victoryCount += result.victoryGain;
        }

        System.out.println("\n========== 节点 5 / " + TOTAL_STAGES + " ==========");
        if (!runBattleNode(hero, scanner, 5, false, false)) {
            finishAdventure(hero, clearedStages, victoryCount, false, midRoute.displayName + " -> 未抵达终局");
            return;
        }
        clearedStages++;
        victoryCount++;

        RouteChoice finalRoute = chooseFinalRoute(scanner);
        System.out.println("\n========== 节点 6 / " + TOTAL_STAGES + " ==========");
        NodeResult finalPrepResult = executeNode(getFinalRouteNode(finalRoute), hero, scanner, 6);
        if (!finalPrepResult.continueRun || hero.isDead()) {
            finishAdventure(hero, clearedStages, victoryCount, false, midRoute.displayName + " -> " + finalRoute.displayName);
            return;
        }
        clearedStages++;
        victoryCount += finalPrepResult.victoryGain;

        System.out.println("\n========== 节点 7 / " + TOTAL_STAGES + " ==========");
        System.out.println("最终 Boss 战来临，做好准备！");
        if (!runBattleNode(hero, scanner, 7, false, true)) {
            finishAdventure(hero, clearedStages, victoryCount, false, midRoute.displayName + " -> " + finalRoute.displayName);
            return;
        }
        clearedStages++;
        victoryCount++;

        System.out.println("\n你击败了最终 Boss，完成了整场冒险！");
        finishAdventure(hero, clearedStages, victoryCount, true, midRoute.displayName + " -> " + finalRoute.displayName);
    }

    private static boolean runBattleNode(Hero hero, Scanner scanner, int stage, boolean elite, boolean boss) {
        Enemy enemy = createEnemyForStage(stage, elite, boss);
        BattleResult result = CombatManager.startBattle(hero, enemy, scanner);

        if (result.getOutcome() == BattleResult.Outcome.ESCAPE) {
            System.out.println("\n你选择了撤退，本次冒险到此为止。");
            return false;
        }

        if (result.getOutcome() == BattleResult.Outcome.DEFEAT) {
            System.out.println("\n冒险失败，你倒在了当前节点。");
            return false;
        }

        System.out.println("\n战斗胜利，获得奖励！");
        hero.addGold(result.getGoldReward());
        hero.gainExp(result.getExpReward());
        if (!boss) {
            chooseReward(hero, scanner, stage);
        }
        return true;
    }

    private static NodeResult executeNode(NodeType nodeType, Hero hero, Scanner scanner, int stage) {
        switch (nodeType) {
            case EVENT:
                RandomEvent.trigger(hero, scanner, stage);
                return new NodeResult(true, 0);
            case SHOP:
                Shop.visit(hero, scanner, stage);
                return new NodeResult(true, 0);
            case TREASURE:
                runTreasureNode(hero, scanner, stage);
                return new NodeResult(true, 0);
            case REST:
                runRestNode(hero);
                return new NodeResult(true, 0);
            case ELITE:
                return new NodeResult(runBattleNode(hero, scanner, stage, true, false), 1);
            default:
                return new NodeResult(runBattleNode(hero, scanner, stage, false, false), 1);
        }
    }

    private static Enemy createEnemyForStage(int stage, boolean elite, boolean boss) {
        if (boss) {
            return new Enemy("深渊领主", 145, 13, 20, 90, 80, false, true);
        }

        if (elite) {
            Enemy[] elites = new Enemy[]{
                    new Enemy("血刃督军", 88, 10, 15, 55, 42, true, false),
                    new Enemy("腐化祭司", 82, 9, 14, 58, 44, true, false),
                    new Enemy("黑爪首领", 92, 11, 14, 60, 46, true, false)
            };
            return elites[(int) (Math.random() * elites.length)];
        }

        Enemy[] normals;
        if (stage <= 2) {
            normals = new Enemy[]{
                    new Enemy("史莱姆", 36, 4, 8, 16, 14, false, false),
                    new Enemy("哥布林斥候", 40, 5, 9, 18, 15, false, false),
                    new Enemy("暗影狼", 44, 6, 10, 22, 17, false, false)
            };
        } else {
            normals = new Enemy[]{
                    new Enemy("骷髅兵", 58, 7, 12, 30, 24, false, false),
                    new Enemy("狂战士", 64, 8, 13, 34, 26, false, false),
                    new Enemy("洞穴法师", 60, 7, 12, 36, 28, false, false)
            };
        }

        return normals[(int) (Math.random() * normals.length)];
    }

    private static void runTreasureNode(Hero hero, Scanner scanner, int stage) {
        Equipment equipment = EquipmentFactory.createRandomEquipment(stage + 2);
        System.out.println("\n========== 宝库节点 ==========");
        System.out.println("你发现了一间尘封宝库，可以拿走一份大奖。");
        System.out.println("1 - 取走 45 金币");
        System.out.println("2 - 取走 3 瓶药水");
        System.out.println("3 - 取走装备：" + equipment.getDisplayName());
        System.out.println("    " + equipment.getFullDescription());

        String input = readThreeWayChoice(scanner);
        switch (input) {
            case "1":
                hero.addGold(45);
                break;
            case "2":
                hero.addPotions(3);
                break;
            default:
                equipment.use(hero);
                break;
        }
    }

    private static void runRestNode(Hero hero) {
        System.out.println("\n========== 休整节点 ==========");
        System.out.println("你抵达了圣疗回廊，古老的泉水治愈了伤势。");
        hero.heal(25);
        hero.addPotion();
        hero.gainGuard(4);
    }

    private static void chooseReward(Hero hero, Scanner scanner, int stage) {
        RewardOption[] options = createRewardOptions();

        System.out.println("\n请选择一项成长奖励：");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + " - " + options[i].title + "：" + options[i].description);
        }

        while (true) {
            if (!scanner.hasNextLine()) {
                System.out.println("输入已结束，自动选择第一个奖励。");
                applyReward(hero, options[0], stage);
                return;
            }

            String input = scanner.nextLine().trim();
            if ("1".equals(input) || "2".equals(input) || "3".equals(input)) {
                int selectedIndex = Integer.parseInt(input) - 1;
                applyReward(hero, options[selectedIndex], stage);
                return;
            }

            System.out.println("无效选项，请输入 1、2 或 3。");
        }
    }

    private static RewardOption[] createRewardOptions() {
        RewardOption[] pool = new RewardOption[]{
                new RewardOption("生命祝福", "最大生命 +15，并立即恢复 15 点生命", 1),
                new RewardOption("力量提升", "攻击 +3", 2),
                new RewardOption("钢铁护甲", "防御 +1，并恢复 8 点生命", 3),
                new RewardOption("战地补给", "获得 2 瓶药水，并恢复 20 点生命", 4),
                new RewardOption("战斗磨炼", "攻击 +1，防御 +1", 5),
                new RewardOption("精准猎手", "暴击率 +5%，恢复 8 点生命", 6),
                new RewardOption("神秘装备", "获得一件随机装备", 7)
        };

        for (int i = pool.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            RewardOption temp = pool[i];
            pool[i] = pool[j];
            pool[j] = temp;
        }

        return new RewardOption[]{pool[0], pool[1], pool[2]};
    }

    private static void applyReward(Hero hero, RewardOption option, int stage) {
        System.out.println("\n你选择了奖励：" + option.title);

        switch (option.type) {
            case 1:
                hero.increaseMaxHp(15);
                hero.heal(15);
                break;
            case 2:
                hero.increaseAttackPower(3);
                break;
            case 3:
                hero.increaseDefense(1);
                hero.heal(8);
                break;
            case 4:
                hero.addPotions(2);
                hero.heal(20);
                break;
            case 5:
                hero.increaseAttackPower(1);
                hero.increaseDefense(1);
                break;
            case 6:
                hero.increaseCritChance(5);
                hero.heal(8);
                break;
            case 7:
                Equipment equipment = EquipmentFactory.createRandomEquipment(stage + 1);
                equipment.use(hero);
                break;
            default:
                break;
        }
    }

    private static void finishAdventure(Hero hero, int clearedStages, int victoryCount, boolean victory, String routeSummary) {
        printSummary(hero, clearedStages, victoryCount, victory, routeSummary);
        HistoryManager.appendRunRecord(hero, victory, clearedStages, TOTAL_STAGES, victoryCount, routeSummary);
        System.out.println("本局战绩已记录到 `run-history.txt`。");
    }

    private static void printSummary(Hero hero, int clearedStages, int victoryCount, boolean victory, String routeSummary) {
        System.out.println("\n========== 本局总结 ==========");
        System.out.println("冒险结果：" + (victory ? "胜利" : "结束"));
        System.out.println("已完成节点：" + clearedStages + "/" + TOTAL_STAGES);
        System.out.println("战斗胜场：" + victoryCount);
        System.out.println("路线选择：" + routeSummary);
        System.out.println("英雄职业：" + hero.getHeroClass().getDisplayName());
        System.out.println("英雄等级：" + hero.getLevel());
        System.out.println("剩余生命：" + hero.getHp() + "/" + hero.getMaxHp());
        System.out.println("最终攻击：" + hero.getAttackPower());
        System.out.println("最终防御：" + hero.getDefense());
        System.out.println("最终暴击：" + hero.getCritChance() + "%");
        System.out.println("剩余药水：" + hero.getPotions());
        System.out.println("持有金币：" + hero.getGold());
        System.out.println("当前装备：" + hero.getEquipmentSummary());
        System.out.println("==============================");
    }

    private static RouteChoice chooseMidRoute(Scanner scanner) {
        System.out.println("\n请选择中段路线：");
        System.out.println("1 - 商旅古道：事件 -> 商店 -> 精英");
        System.out.println("2 - 危险遗迹：精英 -> 宝库 -> 事件");
        System.out.println("3 - 猎人山径：事件 -> 精英 -> 宝库");

        while (true) {
            if (!scanner.hasNextLine()) {
                return RouteChoice.MERCHANT_ROAD;
            }

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return RouteChoice.MERCHANT_ROAD;
                case "2":
                    return RouteChoice.RUINS_PATH;
                case "3":
                    return RouteChoice.HUNTER_TRAIL;
                default:
                    System.out.println("无效选项，请输入 1、2 或 3。");
                    break;
            }
        }
    }

    private static RouteChoice chooseFinalRoute(Scanner scanner) {
        System.out.println("\nBoss 前请选择终段路线：");
        System.out.println("1 - 圣疗回廊：恢复生命并补给药水");
        System.out.println("2 - 深渊前哨：挑战精英，换取更高成长");
        System.out.println("3 - 贪欲宝库：直接拿一份高阶奖励");

        while (true) {
            if (!scanner.hasNextLine()) {
                return RouteChoice.SANCTUARY;
            }

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return RouteChoice.SANCTUARY;
                case "2":
                    return RouteChoice.ABYSS_OUTPOST;
                case "3":
                    return RouteChoice.GREED_VAULT;
                default:
                    System.out.println("无效选项，请输入 1、2 或 3。");
                    break;
            }
        }
    }

    private static NodeType[] getMidRouteNodes(RouteChoice routeChoice) {
        switch (routeChoice) {
            case RUINS_PATH:
                return new NodeType[]{NodeType.ELITE, NodeType.TREASURE, NodeType.EVENT};
            case HUNTER_TRAIL:
                return new NodeType[]{NodeType.EVENT, NodeType.ELITE, NodeType.TREASURE};
            default:
                return new NodeType[]{NodeType.EVENT, NodeType.SHOP, NodeType.ELITE};
        }
    }

    private static NodeType getFinalRouteNode(RouteChoice routeChoice) {
        switch (routeChoice) {
            case ABYSS_OUTPOST:
                return NodeType.ELITE;
            case GREED_VAULT:
                return NodeType.TREASURE;
            default:
                return NodeType.REST;
        }
    }

    private static String readThreeWayChoice(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return "3";
        }

        String input = scanner.nextLine().trim();
        if ("1".equals(input) || "2".equals(input) || "3".equals(input)) {
            return input;
        }

        return "3";
    }

    private enum NodeType {
        EVENT,
        SHOP,
        ELITE,
        TREASURE,
        REST,
        NORMAL_BATTLE
    }

    private enum RouteChoice {
        MERCHANT_ROAD("商旅古道"),
        RUINS_PATH("危险遗迹"),
        HUNTER_TRAIL("猎人山径"),
        SANCTUARY("圣疗回廊"),
        ABYSS_OUTPOST("深渊前哨"),
        GREED_VAULT("贪欲宝库");

        private final String displayName;

        RouteChoice(String displayName) {
            this.displayName = displayName;
        }
    }

    private static class NodeResult {
        private final boolean continueRun;
        private final int victoryGain;

        private NodeResult(boolean continueRun, int victoryGain) {
            this.continueRun = continueRun;
            this.victoryGain = victoryGain;
        }
    }

    private static class RewardOption {
        private final String title;
        private final String description;
        private final int type;

        private RewardOption(String title, String description, int type) {
            this.title = title;
            this.description = description;
            this.type = type;
        }
    }
}
