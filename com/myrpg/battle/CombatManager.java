package com.myrpg.battle;

import com.myrpg.character.Enemy;
import com.myrpg.character.Hero;

import java.util.Scanner;

public class CombatManager {
    public static BattleResult startBattle(Hero hero, Enemy enemy, Scanner scanner) {
        System.out.println("\n=================================");
        System.out.println("战斗开始！");
        System.out.println("⚔ " + enemy.getName() + " 出现了！");
        System.out.println("=================================\n");

        while (!hero.isDead() && !enemy.isDead()) {
            if (!hero.processTurnEffects()) {
                break;
            }
            if (!enemy.processTurnEffects()) {
                break;
            }

            System.out.println("====== 当前回合 ======");
            enemy.showStatus();
            hero.showStatus();

            System.out.println("\n请选择你的行动：");
            System.out.println("1 - 攻击");
            System.out.println("2 - 防御");
            System.out.println("3 - 技能：" + hero.getSkillOneName());
            System.out.println("4 - 技能：" + hero.getSkillTwoName());
            System.out.println("5 - 使用药水");
            System.out.println("6 - 逃跑");

            if (!scanner.hasNextLine()) {
                System.out.println("输入已结束，战斗被中断。");
                return new BattleResult(BattleResult.Outcome.ESCAPE, enemy.getName(), 0, 0);
            }

            String input = scanner.nextLine().trim();
            boolean turnConsumed = true;

            if (input.equals("1")) {
                hero.performBasicAttack(enemy);
            } else if (input.equals("2")) {
                System.out.println(hero.getName() + " 摆出了防御姿态！");
                hero.gainGuard(6);
            } else if (input.equals("3")) {
                if (!hero.canUseSkillOne()) {
                    System.out.println("技能还在冷却中，剩余 " + hero.getSkillOneCooldown() + " 回合。");
                    turnConsumed = false;
                } else {
                    hero.useSkillOne(enemy);
                }
            } else if (input.equals("4")) {
                if (!hero.canUseSkillTwo()) {
                    System.out.println("技能还在冷却中，剩余 " + hero.getSkillTwoCooldown() + " 回合。");
                    turnConsumed = false;
                } else {
                    hero.useSkillTwo(enemy);
                }
            } else if (input.equals("5")) {
                if (!hero.usePotion()) {
                    turnConsumed = false;
                }
            } else if (input.equals("6")) {
                boolean escaped = Math.random() < 0.5;
                if (escaped) {
                    System.out.println(hero.getName() + " 成功逃跑了！");
                    return new BattleResult(BattleResult.Outcome.ESCAPE, enemy.getName(), 0, 0);
                } else {
                    System.out.println("逃跑失败！敌人挡住了你的去路。");
                }
            } else {
                System.out.println("无效指令！");
                continue;
            }

            if (!turnConsumed) {
                continue;
            }

            if (!enemy.isDead()) {
                enemy.executeTurn(hero);
            }

            hero.tickSkillCooldowns();
        }

        if (hero.isDead()) {
            System.out.println(hero.getName() + " 被击败了……");
            return new BattleResult(BattleResult.Outcome.DEFEAT, enemy.getName(), 0, 0);
        }

        System.out.println("你击败了" + enemy.getName() + "！");
        return new BattleResult(
                BattleResult.Outcome.VICTORY,
                enemy.getName(),
                enemy.getGoldReward(),
                enemy.getExpReward()
        );
    }
}
