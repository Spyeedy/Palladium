package net.threetag.palladium.util;

import net.minecraft.world.entity.Entity;

public class ScoreboardUtil {

    public static int getScore(Entity entity, String objective) {
        return getScore(entity, objective, 0);
    }

    public static int getScore(Entity entity, String objective, int fallback) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getPlayerScores(entity.getScoreboardName()).get(obj);

            if (score != null) {
                return score.getScore();
            }
        }

        return fallback;
    }

    public static int setScore(Entity entity, String objective, int value) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getPlayerScores(entity.getScoreboardName()).get(obj);

            if (score != null) {
                score.setScore(value);
                return score.getScore();
            }
        }

        return 0;
    }

    public static int addScore(Entity entity, String objective, int amount) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getPlayerScores(entity.getScoreboardName()).get(obj);

            if (score != null) {
                score.add(amount);
                return score.getScore();
            }
        }

        return 0;
    }

    public static int subtractScore(Entity entity, String objective, int amount) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getPlayerScores(entity.getScoreboardName()).get(obj);

            if (score != null) {
                score.add(-amount);
                return score.getScore();
            }
        }

        return 0;
    }

}
