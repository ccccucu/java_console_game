package com.myrpg.game;

import com.myrpg.character.Hero;
import com.myrpg.items.Equipment;
import com.myrpg.items.EquipmentFactory;

import java.util.Scanner;

public class Shop {
    public static void visit(Hero hero, Scanner scanner, int tier) {
        Equipment offerOne = EquipmentFactory.createRandomEquipment(tier);
        Equipment offerTwo = EquipmentFactory.createRandomEquipment(tier + 1);
        int priceOne = offerOne.getValue();
        int priceTwo = offerTwo.getValue();
        int potionPrice = 12 + tier * 2;
        int healPrice = 18 + tier * 3;

        System.out.println("\n========== 行脚商店 ==========");
        System.out.println("商人笑着说道：欢迎，勇士。看看你需要点什么。");

        boolean shopping = true;
        while (shopping) {
            System.out.println("\n当前金币：" + hero.getGold());
            System.out.println("1 - 购买药水（" + potionPrice + " 金币）");
            System.out.println("2 - 完全治疗（" + healPrice + " 金币）");
            System.out.println("3 - 购买装备：" + offerOne.getDisplayName() + "（" + priceOne + " 金币）");
            System.out.println("    " + offerOne.getFullDescription());
            System.out.println("4 - 购买装备：" + offerTwo.getDisplayName() + "（" + priceTwo + " 金币）");
            System.out.println("    " + offerTwo.getFullDescription());
            System.out.println("5 - 离开商店");

            if (!scanner.hasNextLine()) {
                System.out.println("输入已结束，离开商店。");
                return;
            }

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    if (hero.spendGold(potionPrice)) {
                        hero.addPotion();
                    }
                    break;
                case "2":
                    if (hero.spendGold(healPrice)) {
                        hero.fullRecover();
                    }
                    break;
                case "3":
                    if (hero.spendGold(priceOne)) {
                        offerOne.use(hero);
                        offerOne = EquipmentFactory.createRandomEquipment(tier + 1);
                        priceOne = offerOne.getValue();
                    }
                    break;
                case "4":
                    if (hero.spendGold(priceTwo)) {
                        offerTwo.use(hero);
                        offerTwo = EquipmentFactory.createRandomEquipment(tier + 2);
                        priceTwo = offerTwo.getValue();
                    }
                    break;
                case "5":
                    shopping = false;
                    System.out.println("商店之旅结束。");
                    break;
                default:
                    System.out.println("无效选项，请重新输入。");
                    break;
            }
        }
    }
}
