package top.itsglobally.CircleFramework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import top.itsglobally.CircleFramework.util.AsyncUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public abstract class CommandBase implements TabCompleter {
    protected void playerExecute(Player player, String label, String[] args) {
    }

    protected void allExecute(CommandSender sender, String label, String[] args) {
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    private void async(Runnable runnable) {
        AsyncUtil.runAsync(runnable);
    }

    private <T> void supplyAsync(Callable<T> callable, Consumer<T> callback) {
        AsyncUtil.supplyAsync(callable, callback);
    }

}