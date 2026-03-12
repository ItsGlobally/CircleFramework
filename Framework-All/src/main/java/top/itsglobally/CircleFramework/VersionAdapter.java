package top.itsglobally.CircleFramework;

import org.bukkit.inventory.meta.ItemMeta;

public interface VersionAdapter {
    void sendActionBar();
    ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable);
}
