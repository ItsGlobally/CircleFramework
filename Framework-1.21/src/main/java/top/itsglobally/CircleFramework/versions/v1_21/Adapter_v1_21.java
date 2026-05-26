package top.itsglobally.CircleFramework.versions.v1_21;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import top.itsglobally.CircleFramework.core.VersionAdapter;

public class Adapter_v1_21 implements VersionAdapter {
    @Override
    public ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return meta;
    }

    @Override
    public ItemMeta setCustomModelMeta(ItemMeta meta, int model) {
        meta.setCustomModelData(model);
        return meta;
    }

    @Override
    public ItemMeta setAllowAnvilEnchant(ItemMeta meta, boolean allow) {
        meta.setEnchantable(allow ? 1 : 0);
        return meta;
    }

    @Override
    public ItemMeta setPersistentDataContainer(ItemMeta meta, JavaPlugin plugin, String key, String id) {
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), org.bukkit.persistence.PersistentDataType.STRING, id);
        return meta;
    }

    @Override
    public String getPersistentDataContainer(ItemMeta meta, JavaPlugin plugin, String key) {
        return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, key), org.bukkit.persistence.PersistentDataType.STRING);
    }
}
