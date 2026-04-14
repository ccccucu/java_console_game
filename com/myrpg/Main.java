package com.myrpg;

import com.myrpg.character.Hero;
import com.myrpg.character.HeroClass;
import com.myrpg.game.GameSession;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("        欢迎来到 MyRpg！");
        System.out.println("     一个 Java 终端迷你 RPG");
        System.out.println("=================================");

        boolean running = true;
        while (running) {
            System.out.println("\n主菜单");
            System.out.println("1 - 开始新游戏");
            System.out.println("2 - 退出");

            if (!scanner.hasNextLine()) {
                System.out.println("\n输入已结束，游戏即将关闭。");
                break;
            }

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1":
                    Hero hero = createHero(scanner);
                    if (hero == null) {
                        System.out.println("\n输入已结束，返回主菜单。");
                        break;
                    }
                    GameSession.startAdventure(hero, scanner);
                    break;
                case "2":
                    running = false;
                    System.out.println("冒险结束，欢迎下次再来！");
                    break;
                default:
                    System.out.println("无效选项，请输入 1 或 2。");
                    break;
            }
        }
    }

    private static Hero createHero(Scanner scanner) {
        System.out.print("\n请输入你的英雄名字：");
        if (!scanner.hasNextLine()) {
            return null;
        }

        String heroName = scanner.nextLine().trim();

        while (heroName.isEmpty()) {
            System.out.print("名字不能为空，请重新输入：");
            if (!scanner.hasNextLine()) {
                return null;
            }
            heroName = scanner.nextLine().trim();
        }

        HeroClass heroClass = chooseHeroClass(scanner);
        if (heroClass == null) {
            return null;
        }

        Hero hero = new Hero(heroName, 100, heroClass);
        System.out.println(
                "\n" + hero.getName() + " 作为 " + hero.getHeroClass().getDisplayName()
                        + " 已进入冒险，当前生命值为 " + hero.getHp() + " 点！"
        );
        return hero;
    }

    private static HeroClass chooseHeroClass(Scanner scanner) {
        System.out.println("\n请选择职业：");
        System.out.println("1 - 战士：血厚防高，技能偏向生存与压制");
        System.out.println("2 - 游侠：暴击更高，技能偏向连击与中毒");
        System.out.println("3 - 法师：法术伤害高，技能偏向灼烧与控制");

        while (true) {
            if (!scanner.hasNextLine()) {
                return null;
            }

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return HeroClass.WARRIOR;
                case "2":
                    return HeroClass.ROGUE;
                case "3":
                    return HeroClass.MAGE;
                default:
                    System.out.println("无效选项，请输入 1、2 或 3。");
                    break;
            }
        }
    }
}
