package top.itsglobally.CircleFramework.command;

import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import top.itsglobally.CircleFramework.BasePlugin;
import top.itsglobally.CircleFramework.data.Predefiend;
import top.itsglobally.CircleFramework.util.AsyncUtil;
import top.itsglobally.CircleFramework.util.MsgUtil;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public abstract class CommandBase implements TabCompleter {
    @Setter
    private String usage;
    @Setter
    private CommandSender sender;

    protected void playerExecute(Player player, String label, String[] args) {
    }

    protected void allExecute(CommandSender sender, String label, String[] args) {
    }

    protected abstract List<String> tabComplete(CommandSender sender, String alias, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabComplete(sender, alias, args);
    }

    private void async(Runnable runnable) {
        AsyncUtil.runAsync(runnable);
    }

    private <T> void supplyAsync(Callable<T> callable, Consumer<T> callback) {
        AsyncUtil.supplyAsync(callable, callback);
    }

    private BasePlugin<?> getPlugin() {
        return Predefiend.getPlugin();
    }

    public void usage() {
        AsyncUtil.runSync(() -> MsgUtil.send(sender, "&c用法: " + usage));
    }

    public void warn(String message) {
        AsyncUtil.runSync(() -> MsgUtil.send(sender, "&7" + message));
    }
    public void ok(String message) {
        AsyncUtil.runSync(() -> MsgUtil.send(sender, "&b" + message));
    }
    public void send(String message) {
        AsyncUtil.runSync(() -> MsgUtil.send(sender, message));
    }

}