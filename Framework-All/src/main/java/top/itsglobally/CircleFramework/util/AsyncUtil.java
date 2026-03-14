package top.itsglobally.CircleFramework.util;

import org.bukkit.Bukkit;
import top.itsglobally.CircleFramework.data.Predefiend;

public class AsyncUtil {
    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Predefiend.getPlugin(),  runnable);
    }
    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(Predefiend.getPlugin(),  runnable);
    }
    public static <T> void supplyAsync(java.util.concurrent.Callable<T> callable, java.util.function.Consumer<T> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Predefiend.getPlugin(), () -> {
            T result = null;
            try {
                result = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            T finalResult = result;
            Bukkit.getScheduler().runTask(Predefiend.getPlugin(), () -> callback.accept(finalResult));
        });
    }
}
