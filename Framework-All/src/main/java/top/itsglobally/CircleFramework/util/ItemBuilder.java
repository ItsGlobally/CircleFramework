package top.itsglobally.CircleFramework.util;

import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import top.itsglobally.CircleFramework.VersionManager;
import top.itsglobally.CircleFramework.annotation.AutoListener;
import top.itsglobally.CircleFramework.data.Predefiend;

import java.util.*;
import java.util.function.Consumer;

public class ItemBuilder {

    private static final Map<String, ClickActions> registered = new HashMap<>();
    private final ItemStack item;
    private final Material material;
    private final String clickId = UUID.randomUUID().toString();
    private ItemMeta meta;
    private Consumer<ClickContext> leftClick;
    private Consumer<ClickContext> rightClick;
    private Consumer<ClickContext> middleClick;

    public ItemBuilder(Material material) {
        this.material = material;
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, DyeColor color) {
        this.material = resolveColoredMaterial(material, color);
        this.item = new ItemStack(this.material);
        applyLegacyColor(material, color, this.item);
        this.meta = item.getItemMeta();
    }


    public ItemBuilder name(String name) {
        meta.setDisplayName(MsgUtil.colorLegacy(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> loreCopy = new ArrayList<>();
        for (String s : lore) {
            loreCopy.add(MsgUtil.colorLegacy(s));
        }
        meta.setLore(loreCopy);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        List<String> loreCopy = new ArrayList<>();
        for (String s : lore) {
            loreCopy.add(MsgUtil.colorLegacy(s));
        }
        meta.setLore(loreCopy);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder unenchant(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addFakeEnchantment() {
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        meta = VersionManager.getAdapter().setUnbreakable(meta, unbreakable);
        return this;
    }

    public ItemBuilder durability(int d) {
        item.setDurability((short) d);
        return this;
    }

    public ItemBuilder customModelMeta(int model) {
        meta = VersionManager.getAdapter().setCustomModelMeta(meta, model);
        return this;
    }

    public <P, C> ItemBuilder setPersistentDataContainer(NamespacedKey namespacedKey, PersistentDataType<P, C> persistentDataType, C id) {
        meta = VersionManager.getAdapter().setPersistentDataContainer(meta, namespacedKey, persistentDataType, id);
        return this;
    }

    public ItemBuilder onLeftClick(Consumer<ClickContext> action) {
        this.leftClick = action;
        return this;
    }

    public ItemBuilder onRightClick(Consumer<ClickContext> action) {
        this.rightClick = action;
        return this;
    }

    public ItemBuilder onMiddleClick(Consumer<ClickContext> action) {
        this.middleClick = action;
        return this;
    }

    private static Material resolveColoredMaterial(Material base, DyeColor color) {
        String coloredName = color.name() + "_" + base.name();
        Material colored = Material.matchMaterial(coloredName);
        return colored != null ? colored : base;
    }

    private static void applyLegacyColor(Material base, DyeColor color, ItemStack item) {
        String name = base.name();
        if (name.equals("WOOL")
                || name.equals("STAINED_GLASS")
                || name.equals("STAINED_GLASS_PANE")
                || name.equals("CARPET")
                || name.equals("STAINED_CLAY")
                || name.equals("CONCRETE_POWDER")
                || name.equals("INK_SACK")) {
            item.setDurability(color.getWoolData());
        }
    }

    private static NamespacedKey getClickKey() {
        return new NamespacedKey(Predefiend.getPlugin(), "itembuilder_click_id");
    }

    public GuiItem asGuiItem() {
        return dev.triumphteam.gui.builder.item.ItemBuilder.from(build())
                .asGuiItem();
    }

    public GuiItem asGuiItem(GuiAction<InventoryClickEvent> action) {
        return dev.triumphteam.gui.builder.item.ItemBuilder.from(build())
                .asGuiItem(action);
    }

    public ItemStack build() {
        if (this.leftClick != null || this.rightClick != null || this.middleClick != null) {
            meta = VersionManager.getAdapter().setPersistentDataContainer(meta, getClickKey(), PersistentDataType.STRING, clickId);
        }
        item.setItemMeta(meta);
        if (this.leftClick != null || this.rightClick != null || this.middleClick != null) {
            registered.put(clickId, new ClickActions(this.leftClick, this.rightClick, this.middleClick));
        }
        return item;
    }

    public record ClickContext(Player player, ItemStack item, PlayerInteractEvent event) {

    }

    @AutoListener
    public static class ItemListener implements Listener {
        @EventHandler
        public void onClick(PlayerInteractEvent e) {
            ItemStack hand = e.getItem();
            if (hand != null && hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null) {
                    String clickId = VersionManager.getAdapter().getPersistentDataContainer(meta, getClickKey(), PersistentDataType.STRING);
                    if (clickId != null) {
                        ClickActions a = ItemBuilder.registered.get(clickId);
                        if (a != null) {
                            switch (e.getAction()) {
                                case LEFT_CLICK_AIR:
                                case LEFT_CLICK_BLOCK:
                                    if (a.left != null) {
                                        a.left.accept(new ClickContext(e.getPlayer(), hand, e));
                                    }
                                    break;
                                case RIGHT_CLICK_AIR:
                                case RIGHT_CLICK_BLOCK:
                                    if (a.right != null) {
                                        a.right.accept(new ClickContext(e.getPlayer(), hand, e));
                                    }
                                    break;
                                case PHYSICAL:
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private record ClickActions(Consumer<ClickContext> left, Consumer<ClickContext> right,
                                Consumer<ClickContext> middle) {
    }
}
