package top.itsglobally.CircleFramework.data;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import top.itsglobally.CircleFramework.BasePlugin;

public class Predefiend {
    public static BasePlugin<?> getPlugin() {
        return BasePlugin.getInstance();
    }

    public static BukkitAudiences getAdventure() {
        return BasePlugin.getAdventure();
    }
}
