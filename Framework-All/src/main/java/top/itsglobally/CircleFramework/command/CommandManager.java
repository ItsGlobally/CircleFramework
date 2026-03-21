package top.itsglobally.CircleFramework.command;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import top.itsglobally.CircleFramework.annotation.CommandInfo;

import java.lang.reflect.Modifier;
import java.util.*;

public class CommandManager {

    private static final Map<CommandBase, Command> registeredCommands = new HashMap<>();
    private static CommandMap commandMap;

    private static CommandMap getCommandMap() throws Exception {
        if (commandMap != null) return commandMap;
        var field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);
        commandMap = (CommandMap) field.get(Bukkit.getServer());
        return commandMap;
    }

    public static void registerAll(JavaPlugin plugin) {
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(plugin.getClass().getPackageName())
                .scan()) {

            List<ClassInfo> commandClasses = scanResult.getClassesWithAnnotation(CommandInfo.class.getName());
            for (ClassInfo classInfo : commandClasses) {
                try {
                    Class<?> clazz = classInfo.loadClass();
                    if (!CommandBase.class.isAssignableFrom(clazz)) continue;
                    if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) continue;

                    CommandInfo info = clazz.getAnnotation(CommandInfo.class);
                    if (info == null) continue;

                    CommandBase cmdInstance = (CommandBase) clazz.getDeclaredConstructor().newInstance();
                    registerCommand(plugin, cmdInstance, info);

                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to register command: " + classInfo.getName());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerCommand(JavaPlugin plugin, CommandBase cmdInstance, CommandInfo info) throws Exception {
        CommandMap cm = getCommandMap();
        if (info.usage().isEmpty()) cmdInstance.setUsage("/" + info.name());
        else cmdInstance.setUsage(info.usage());

        Command cmd = new Command(info.name()) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                cmdInstance.setSender(sender);

                if (!info.permission().isEmpty() && !sender.hasPermission(info.permission())) {
                    cmdInstance.warn("你沒有足夠的權限(" + info.permission() + ")!");
                    return true;
                }
                if (!(sender instanceof Player player)) {
                    cmdInstance.warn("只有玩家才能運行這個指令!");
                    return true;
                }

                try {
                    cmdInstance.playerExecute(player, label, args);
                } catch (Exception e) {
                    cmdInstance.warn("執行指令時發生錯誤! 請將錯誤原因回報給我們: " + e.getMessage());
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) {

                List<String> raw = cmdInstance.onTabComplete(sender, this, alias, args);

                if (raw == null || raw.isEmpty()) {
                    if (info.tabCompleteReturnPlayerListWhenNull()) {
                        List<String> players = new ArrayList<>();
                        Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
                        raw = players;
                    } else return Collections.emptyList();
                }
                int index = args.length - 1;

                return CommandManager.complete(
                        sender,
                        args,
                        index,
                        raw,
                        s -> s
                );
            }

        };


        cmd.setAliases(Arrays.asList(info.aliases()));
        if (!info.permission().isEmpty()) cmd.setPermission(info.permission());

        cm.register(plugin.getName().toLowerCase(), cmd);
        registeredCommands.put(cmdInstance, cmd);
        plugin.getLogger().info("已註冊指令" + cmd.getName());
    }

    public static void unregisterAll() {
        try {
            CommandMap cm = getCommandMap();
            for (Command cmd : registeredCommands.values()) {
                cmd.unregister(cm);
            }
            registeredCommands.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> List<String> complete(
            CommandSender sender,
            String[] args,
            int index,
            Iterable<T> source,
            java.util.function.Function<T, String> nameMapper
    ) {
        List<String> result = new ArrayList<>();

        if (args.length <= index) return result;

        String input = args[index].toLowerCase();

        for (T t : source) {
            String name = nameMapper.apply(t);
            if (name != null && name.toLowerCase().startsWith(input)) {
                result.add(name);
            }
        }

        return result;
    }
}