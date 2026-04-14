# MyRpg - Java 终端 RPG

这是一个使用 Java 编写的命令行 RPG 小游戏项目。它最初用于练习 `package`、类、方法和控制台交互，当前已经扩展为一款可直接游玩的终端冒险游戏。

---

## 项目简介

当前版本包含以下玩法内容：

- 主菜单与新游戏入口
- 英雄命名与职业选择
- 回合制战斗
- 普通敌人、精英敌人和 Boss
- 双技能系统、防御、药水、暴击与状态效果
- 商店、随机事件、宝库和休整节点
- 装备系统与装备稀有度
- 分支路线选择
- 本局结算与战绩记录

---

## 目录结构

项目按包结构组织：

- `com.myrpg`：程序入口
- `com.myrpg.character`：英雄、敌人、职业等角色相关逻辑
- `com.myrpg.battle`：战斗流程与战斗结果
- `com.myrpg.game`：整局流程、商店、事件、历史记录
- `com.myrpg.items`：道具、装备、装备工厂

---

## 编译运行指南

### 运行环境

- JDK 8 或更高版本
- macOS、Linux、Windows 均可运行

先确认本机 Java 环境可用：

```bash
java -version
javac -version
```

### 进入项目目录

```bash
cd /Users/cuiky/workDir/simple-rpg-game-java
```

### 编译项目

在项目根目录执行：

```bash
javac com/myrpg/Main.java \
  com/myrpg/character/Hero.java \
  com/myrpg/character/HeroClass.java \
  com/myrpg/character/Enemy.java \
  com/myrpg/battle/CombatManager.java \
  com/myrpg/battle/BattleResult.java \
  com/myrpg/game/GameSession.java \
  com/myrpg/game/Shop.java \
  com/myrpg/game/RandomEvent.java \
  com/myrpg/game/HistoryManager.java \
  com/myrpg/items/Item.java \
  com/myrpg/items/Equipment.java \
  com/myrpg/items/EquipmentFactory.java
```

如果你不想逐个列文件，也可以使用：

```bash
find com -name "*.java" -print0 | xargs -0 javac
```

### 启动游戏

编译完成后运行：

```bash
java com.myrpg.Main
```

### 基本游玩流程

启动后按终端提示操作即可：

1. 输入英雄名字
2. 选择职业
3. 选择路线
4. 在战斗、事件、商店和宝库节点中做决策
5. 击败最终 Boss

---

## 游戏指令说明

进入战斗后，通常会看到以下操作：

- `1 - 攻击`：进行一次普通攻击
- `2 - 防御`：本回合进入防御姿态，减少受到的伤害
- `3 - 技能 1`：释放职业的核心进攻技能
- `4 - 技能 2`：释放职业的辅助或爆发技能
- `5 - 使用药水`：恢复生命值
- `6 - 逃跑`：尝试脱离当前战斗

补充说明：

- 技能有冷却时间，使用后需要等待若干回合才能再次释放
- 不同敌人会显示“意图”，例如普通攻击、重击蓄势、恢复生命、火焰法术
- 部分技能和敌人会附带状态效果，例如中毒、灼烧、虚弱、护盾等

---

## 职业说明

### 战士

- 特点：血量更稳、防御更高，适合正面硬扛
- 技能 1 `裂地斩`：造成高额伤害并施加虚弱
- 技能 2 `战吼`：回复生命并获得护盾

### 游侠

- 特点：暴击率更高，输出节奏灵活
- 技能 1 `毒刃`：造成伤害并施加中毒
- 技能 2 `影袭`：快速连续攻击两次

### 法师

- 特点：偏高爆发与控制，但身板更脆
- 技能 1 `火球术`：造成较高伤害并施加灼烧
- 技能 2 `冰霜护盾`：获得护盾并削弱敌人

---

## 节点说明

整局游戏会由多个节点组成，不同节点代表不同玩法：

- `普通战斗`：常规敌人，胜利后可获得金币、经验和成长奖励
- `精英战斗`：更强的敌人，奖励通常更丰厚
- `随机事件`：在收益与风险之间做选择
- `商店`：购买药水、治疗或装备
- `宝库`：获取金币、药水或高品质装备
- `休整节点`：在 Boss 前恢复状态并补给资源
- `Boss 战`：本局最后的关键战斗

---

## 路线说明

当前版本支持中段路线和终段路线选择。

### 中段路线

- `商旅古道`：事件 -> 商店 -> 精英
- `危险遗迹`：精英 -> 宝库 -> 事件
- `猎人山径`：事件 -> 精英 -> 宝库

### 终段路线

- `圣疗回廊`：更稳，适合在 Boss 前恢复状态
- `深渊前哨`：更激进，继续挑战精英获取成长
- `贪欲宝库`：优先拿资源和装备，适合搏一把

---

## 装备说明

装备分为三类：

- `武器`
- `护甲`
- `饰品`

装备还带有稀有度：

- `普通`
- `稀有`
- `史诗`
- `传说`

一般来说，稀有度越高：

- 属性越强
- 售价越高
- 对构筑的影响越明显

---

## 输出文件说明

游戏运行过程中可能会生成以下文件：

- `*.class`：Java 编译产物
- `run-history.txt`：每局战绩记录文件

这些文件已经加入 `.gitignore`，默认不会提交到版本库。

---

## .gitignore

当前项目建议使用以下 `.gitignore`：

```gitignore
.idea/
out/
*.class
run-history.txt
.DS_Store
```

---

## 后续可扩展方向

- 存档与读档
- 地图预览与更多路线分支
- 多 Boss 与多结局
- 装备套装效果
- 职业专属装备池
- 遗物、天赋与更复杂的流派构筑

---

## 许可说明

本项目主要用于学习与练习，可自由修改和扩展。
