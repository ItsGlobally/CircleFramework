package top.itsglobally.CircleFramework.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.itsglobally.CircleFramework.data.Predefiend;

public class MsgUtil {

    private static final MiniMessage MINI = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .build();

    public static Component color(String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }

        return LEGACY.deserialize(message);
    }

    public static String colorLegacy(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Component component(String message) {
        return color(message);
    }

    public static void actionbar(Player player, String message) {
        Predefiend.getAdventure().player(player).sendActionBar(
                MINI.deserialize(message)
        );
    }

    public static void send(CommandSender sender, String message) {
        Predefiend.getAdventure().sender(sender).sendMessage(component(message));
    }

    public static void send(CommandSender sender, Component message) {
        Predefiend.getAdventure().sender(sender).sendMessage(message);
    }

    public static void send(Player player, String message) {
        Predefiend.getAdventure().player(player).sendMessage(component(message));
    }

    public static void send(Player player, Component message) {
        Predefiend.getAdventure().player(player).sendMessage(message);
    }

    public static void info(String msg) {
        Predefiend.getPlugin().getLogger().info(strip(msg));
    }

    public static void warn(String msg) {
        Predefiend.getPlugin().getLogger().warning(strip(msg));
    }

    public static void broadcast(String msg) {
        Bukkit.broadcastMessage(colorLegacy(msg));
    }

    private static String strip(String msg) {
        return Component.text()
                .append(component(msg))
                .build()
                .content();
    }
}
