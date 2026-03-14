package top.itsglobally.CircleFramework.versions.v1_8;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
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
    public <P, C> ItemMeta setPersistentDataContainer(
            ItemMeta meta,
            NamespacedKey key,
            PersistentDataType<P, C> type,
            C value
    ) {
        return meta;
    }

}
