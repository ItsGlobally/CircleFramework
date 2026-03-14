package top.itsglobally.CircleFramework.versions.v1_21;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import top.itsglobally.CircleFramework.VersionAdapter;

public class Adapter_v1_21 implements VersionAdapter {
    @Override
    public ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return meta;
    }
}
