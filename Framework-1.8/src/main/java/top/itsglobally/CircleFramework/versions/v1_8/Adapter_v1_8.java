package top.itsglobally.CircleFramework.versions.v1_8;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import top.itsglobally.CircleFramework.core.VersionAdapter;


public class Adapter_v1_8 implements VersionAdapter {
    @Override
    public ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable) {
        meta.spigot().setUnbreakable(unbreakable);
        return meta;
    }

    @Override
    public ItemMeta setCustomModelMeta(ItemMeta meta, int model) {
        return meta;
    }

    @Override
    public ItemMeta setAllowAnvilEnchant(ItemMeta meta, boolean allow) {
        return meta;
    }

    @Override
    public ItemMeta setPersistentDataContainer(ItemMeta meta, JavaPlugin plugin, String key, String value) {
        return meta;
    }

    @Override
    public String getPersistentDataContainer(ItemMeta meta, JavaPlugin plugin, String key) {
        return null;
    }

}
