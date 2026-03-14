package top.itsglobally.CircleFramework.core;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public interface VersionAdapter {
    ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable);
    ItemMeta setCustomModelMeta(ItemMeta meta, int model);
    <P, C> ItemMeta setPersistentDataContainer(ItemMeta meta,
                                        NamespacedKey namespacedKey,
                                        PersistentDataType<P, C> persistentDataType,
                                        C id);
}
