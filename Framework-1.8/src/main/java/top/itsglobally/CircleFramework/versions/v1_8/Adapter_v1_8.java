package top.itsglobally.CircleFramework.versions.v1_8;

import org.bukkit.inventory.meta.ItemMeta;
import top.itsglobally.CircleFramework.VersionAdapter;

public class Adapter_v1_8 implements VersionAdapter {
    @Override
    public void sendActionBar() {

    }

    @Override
    public ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable) {
        meta.spigot().setUnbreakable(unbreakable);
        return meta;
    }
}
