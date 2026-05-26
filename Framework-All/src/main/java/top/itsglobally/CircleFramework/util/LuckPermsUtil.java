package top.itsglobally.CircleFramework.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class LuckPermsUtil {
    private static final LuckPerms luckPerms = LuckPermsProvider.get();

    public static String getPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return (prefix != null ? prefix : "");
    }
    public static String getPrefixColor(Player player) {
        String prefix = getPrefix(player);
        return prefix.length() >= 2 ? prefix.substring(0, 2) : "&f";
    }
    public static String getPrefixedName(Player player) {
        return getPrefix(player) + player.getName();
    }
    public static String getColoredName(Player player) {
        return getPrefixColor(player) + player.getName();
    }

    public static String getHighestGroup(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";

        return user.getPrimaryGroup();
    }
}
