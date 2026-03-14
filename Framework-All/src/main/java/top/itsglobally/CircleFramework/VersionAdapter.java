package top.itsglobally.CircleFramework;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public interface VersionAdapter {
    ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable);
}
