package net.iambartz.lightrank.game;

import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.game.Countdown;
import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.game.GameState;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class DefaultCountdown implements Countdown {
    private final Game          game;
    private final BukkitTask    task;
    private int                 seconds;

    public DefaultCountdown(RankPlugin plugin, Game game, int seconds) {
        Validate.isTrue(seconds > 0, "the seconds value must be greater than 0");
        this.seconds = seconds;
        game.setGameState(GameState.STARTING);
        this.game = game;
        this.task = this.createCountdownTask(plugin);
    }

    private BukkitTask createCountdownTask(RankPlugin plugin) {
        return Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (seconds <= 0) {
                    cancelTask();
                    game.start(false);
                    return;
                }

                if (seconds == 30 || seconds == 10 || seconds == 3 || seconds == 2 || seconds == 1) {
                    game.message(LightColor.DUEL_BASE + "The match starts in " + LightColor.DUEL_KEY + seconds
                            + LightColor.DUEL_BASE + " second(s)!");
                }
                seconds--;
            }
        }, 1L, 20L);
    }

    private void cancelTask() {
        if (!this.task.isCancelled()) {
            this.task.cancel();
        }
    }

    @Override
    public boolean isFinished() {
        return this.seconds <= 0;
    }

    @Override
    public boolean isCounting() {
        return this.seconds > 0;
    }

    @Override
    public void stop(Game game, boolean forceStop) {
        game.setGameState(GameState.STOPPED);
        this.cancelTask();
    }
}
