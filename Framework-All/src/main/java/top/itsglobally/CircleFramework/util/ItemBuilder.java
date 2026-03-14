package top.itsglobally.CircleFramework.util;

import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.itsglobally.CircleFramework.VersionManager;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    ItemStack item;
    Material material;

    public ItemBuilder(Material material) {
        this.material = material;
        this.item = new ItemStack(material);
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setLore(String ...lore) {
        List<String> listLore = Arrays.asList(lore);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(listLore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(VersionManager.getAdapter().setUnbreakable(meta, unbreakable));
        return this;
    }

    public GuiItem asGuiItem(GuiAction<InventoryClickEvent> action) {
        return dev.triumphteam.gui.builder.item.ItemBuilder.from(item)
                .asGuiItem(action);
    }

    public ItemStack build() {
        return item;
    }
}
