package top.itsglobally.CircleFramework.versions.v1_21;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
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
    public <P, C> ItemMeta setPersistentDataContainer(ItemMeta meta, NamespacedKey namespacedKey, PersistentDataType<P, C> persistentDataType, C id) {
        meta.getPersistentDataContainer().set(namespacedKey, persistentDataType, id);
        return null;
    }
}
