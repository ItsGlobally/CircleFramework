package top.itsglobally.CircleFramework.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.meta.ItemMeta;

public interface VersionAdapter {
    ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable);
    ItemMeta setCustomModelMeta(ItemMeta meta, int model);
    ItemMeta setAllowAnvilEnchant(ItemMeta meta, boolean allow);
    ItemMeta setPersistentDataContainer(ItemMeta meta, JavaPlugin plugin, String key, String id);
    String getPersistentDataContainer(ItemMeta meta, JavaPlugin plugin, String key);
}
