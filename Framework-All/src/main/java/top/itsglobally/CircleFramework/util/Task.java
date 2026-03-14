package top.itsglobally.CircleFramework.util;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import top.itsglobally.CircleFramework.data.Predefiend;

public class Task {
    private BukkitTask bukkitTask;

    private boolean running = false;
    private boolean cancelled = false;

    private long elapsedTicks = 0;
    private long maxTicks = -1;

    private Runnable onFinish;
    private Runnable onCancel;


    public void run(Runnable runnable) {
        start(runnable, 0, -1, -1, null, null);
    }

    public void runLater(long delayTicks, Runnable runnable) {
        start(runnable, delayTicks, -1, -1, null, null);
    }

    public void runTimer(long delayTicks, long periodTicks, Runnable runnable) {
        start(runnable, delayTicks, periodTicks, -1, null, null);
    }

    public void runFor(
            long delayTicks,
            long periodTicks,
            long durationTicks,
            Runnable onTick,
            Runnable onFinish,
            Runnable onCancel
    ) {
        start(onTick, delayTicks, periodTicks, durationTicks, onFinish, onCancel);
    }

    private void start(
            Runnable onTick,
            long delay,
            long period,
            long duration,
            Runnable finish,
            Runnable cancel
    ) {
        if (running) return;

        cancelled = false;
        running = true;
        elapsedTicks = 0;
        maxTicks = duration;
        onFinish = finish;
        onCancel = cancel;

        if (period <= 0) {
            bukkitTask = Bukkit.getScheduler().runTaskLater(Predefiend.getPlugin(), () -> {
                if (cancelled) return;
                onTick.run();
                stopInternal(false);
            }, delay);
            return;
        }

        bukkitTask = Bukkit.getScheduler().runTaskTimer(Predefiend.getPlugin(), () -> {
            if (cancelled) return;

            onTick.run();
            elapsedTicks += period;

            if (maxTicks > 0 && elapsedTicks >= maxTicks) {
                stopInternal(false);
            }
        }, delay, period);
    }

    public void cancel() {
        if (!running) return;
        stopInternal(true);
    }

    private void stopInternal(boolean manualCancel) {
        running = false;
        cancelled = true;

        if (bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
        }

        if (!manualCancel && onFinish != null) {
            onFinish.run();
        } else {
            onCancel.run();
        }

        onFinish = null;
    }

    public boolean isRunning() {
        return running && !cancelled;
    }

    public long getRemainingTicks() {
        if (maxTicks < 0) return -1;
        return Math.max(0, maxTicks - elapsedTicks);
    }
}