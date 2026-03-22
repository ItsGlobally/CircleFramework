package top.itsglobally.CircleFramework.util;

import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import top.itsglobally.CircleFramework.VersionManager;
import top.itsglobally.CircleFramework.annotation.AutoListener;

import java.util.*;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack item;
    private ItemMeta meta;
    private final Material material;
    private static final Map<ItemStack, ClickActions> registered = new HashMap<>();
    private Consumer<ClickContext> leftClick;
    private Consumer<ClickContext> rightClick;
    private Consumer<ClickContext> middleClick;

    public ItemBuilder(Material material) {
        this.material = material;
        this.item = new ItemStack(material);
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

    public ItemBuilder lore(String ...lore) {
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

    public GuiItem asGuiItem() {
        return dev.triumphteam.gui.builder.item.ItemBuilder.from(build())
                .asGuiItem();
    }

    public GuiItem asGuiItem(GuiAction<InventoryClickEvent> action) {
        return dev.triumphteam.gui.builder.item.ItemBuilder.from(build())
                .asGuiItem(action);
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        if (this.leftClick != null || this.rightClick != null || this.middleClick != null) {
            registered.put(item, new ClickActions(this.leftClick, this.rightClick, this.middleClick));
        }
        return item;
    }

    public static class ClickContext {

        private final Player player;
        private final ItemStack item;
        private final PlayerInteractEvent event;

        public ClickContext(Player player, ItemStack item, PlayerInteractEvent event) {
            this.player = player;
            this.item = item;
            this.event = event;
        }

        public Player getPlayer() {
            return player;
        }

        public ItemStack getItem() {
            return item;
        }

        public PlayerInteractEvent getEvent() {
            return event;
        }
    }

    @AutoListener
    public static class ItemListener implements Listener {
        @EventHandler
        public void onClick(PlayerInteractEvent e) {
            ItemStack hand = e.getItem();
            if (hand != null) {
                for (Map.Entry<ItemStack, ClickActions> entry : ItemBuilder.registered.entrySet()) {
                    if (ItemBuilder.isSimilar(hand, entry.getKey())) {
                        ClickActions a = entry.getValue();
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

    public static boolean isSimilar(ItemStack a, ItemStack b) {
        if (a != null && b != null) {
            if (a.getType() != b.getType()) {
                return false;
            } else {
                ItemMeta am = a.getItemMeta();
                ItemMeta bm = b.getItemMeta();
                if (am != null && bm != null) {
                    return Objects.equals(am.getDisplayName(), bm.getDisplayName()) && Objects.equals(am.getLore(), bm.getLore());
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private record ClickActions(Consumer<ClickContext> left, Consumer<ClickContext> right, Consumer<ClickContext> middle) {
    }
}
