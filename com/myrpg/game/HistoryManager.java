package com.myrpg.game;

import com.myrpg.character.Hero;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryManager {
    private static final Path HISTORY_FILE = Path.of("run-history.txt");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void appendRunRecord(
            Hero hero,
            boolean victory,
            int clearedStages,
            int totalStages,
            int victoryCount,
            String routeSummary
    ) {
        String line = String.format(
                "[%s] 结果=%s | 节点=%d/%d | 胜场=%d | 英雄=%s(%s) | 等级=%d | 生命=%d/%d | 攻击=%d | 防御=%d | 暴击=%d%% | 金币=%d | 装备=%s | 路线=%s%n",
                LocalDateTime.now().format(TIME_FORMATTER),
                victory ? "胜利" : "结束",
                clearedStages,
                totalStages,
                victoryCount,
                hero.getName(),
                hero.getHeroClass().getDisplayName(),
                hero.getLevel(),
                hero.getHp(),
                hero.getMaxHp(),
                hero.getAttackPower(),
                hero.getDefense(),
                hero.getCritChance(),
                hero.getGold(),
                hero.getEquipmentSummary(),
                routeSummary
        );

        try {
            Files.writeString(
                    HISTORY_FILE,
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.out.println("写入战绩记录失败：" + e.getMessage());
        }
    }
}
