package top.itsglobally.CircleFramework.util;

import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import top.itsglobally.CircleFramework.data.Predefiend;
import top.itsglobally.CircleFramework.VersionManager;
import top.itsglobally.CircleFramework.annotation.AutoListener;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.function.Consumer;

public class ItemBuilder {

    private static final Map<String, ClickActions> registered = new HashMap<>();
    private static final String LEGACY_LORE_PREFIX = ChatColor.BLACK + "[CF]";
    private final ItemStack item;
    private final Material material;
    private String clickId;
    private ItemMeta meta;
    private Consumer<ClickContext> leftClick;
    private Consumer<ClickContext> rightClick;
    private Consumer<ClickContext> middleClick;
    private Consumer<BreakContext> breakBlock;
    private Consumer<PlaceContext> placeBlock;

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

    public ItemBuilder setAllowAnvilEnchant(boolean allow) {
        meta = VersionManager.getAdapter().setAllowAnvilEnchant(meta, allow);
        return this;
    }

    public ItemBuilder setPersistentDataContainer(String key, String id) {
        meta = VersionManager.getAdapter().setPersistentDataContainer(meta, Predefiend.getPlugin(), key, id);
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

    public ItemBuilder onBreakBlock(Consumer<BreakContext> action) {
        this.breakBlock = action;
        return this;
    }

    public ItemBuilder onPlace(Consumer<PlaceContext> action) {
        this.placeBlock = action;
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

    private static String getClickKey() {
        return "itembuilder_click_id";
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
        if (hasActions()) {
            if (clickId == null) {
                clickId = buildStableClickId();
            }
            meta = VersionManager.getAdapter().setPersistentDataContainer(meta, Predefiend.getPlugin(), getClickKey(), clickId);
            if (isLegacyServer()) {
                List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                lore.add(LEGACY_LORE_PREFIX + clickId);
                meta.setLore(lore);
            }
        }
        item.setItemMeta(meta);
        if (hasActions()) {
            registered.put(clickId, new ClickActions(this.leftClick, this.rightClick, this.middleClick, this.breakBlock, this.placeBlock));
        }
        return item;
    }

    public record ClickContext(Player player, ItemStack item, PlayerInteractEvent event) {

    }

    public record BreakContext(Player player, ItemStack item, BlockBreakEvent event) {

    }

    public record PlaceContext(Player player, ItemStack item, BlockPlaceEvent event) {

    }

    @AutoListener
    public static class ItemListener implements Listener {
        private String resolveClickId(ItemMeta meta) {
            if (isLegacyServer()) {
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    if (lore != null) {
                        for (int i = lore.size() - 1; i >= 0; i--) {
                            String line = ChatColor.stripColor(lore.get(i));
                            if (line != null && line.startsWith("[CF]")) {
                                return line.substring(4);
                            }
                        }
                    }
                }
                return null;
            }
            return VersionManager.getAdapter().getPersistentDataContainer(meta, Predefiend.getPlugin(), getClickKey());
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onClick(PlayerInteractEvent e) {
            if (e.isCancelled()) return;
            ItemStack hand = e.getItem();
            if (hand != null && hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null) {
                    String clickId = resolveClickId(meta);
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

        @EventHandler(priority = EventPriority.LOWEST)
        public void onBreak(BlockBreakEvent e) {
            if (e.isCancelled()) return;
            ItemStack hand = e.getPlayer().getItemInHand();
            if (hand != null && hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null) {
                    String clickId = resolveClickId(meta);
                    if (clickId != null) {
                        ClickActions a = ItemBuilder.registered.get(clickId);
                        if (a != null && a.breakBlock != null) {
                            a.breakBlock.accept(new BreakContext(e.getPlayer(), hand, e));
                        }
                    }
                }
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlace(BlockPlaceEvent e) {
            if (e.isCancelled()) return;
            ItemStack hand = e.getItemInHand();
            if (hand != null && hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null) {
                    String clickId = resolveClickId(meta);
                    if (clickId != null) {
                        ClickActions a = ItemBuilder.registered.get(clickId);
                        if (a != null && a.placeBlock != null) {
                            a.placeBlock.accept(new PlaceContext(e.getPlayer(), hand, e));
                        }
                    }
                }
            }
        }
    }

    private record ClickActions(Consumer<ClickContext> left, Consumer<ClickContext> right,
                                Consumer<ClickContext> middle, Consumer<BreakContext> breakBlock,
                                Consumer<PlaceContext> placeBlock) {
    }

    private boolean hasActions() {
        return this.leftClick != null
                || this.rightClick != null
                || this.middleClick != null
                || this.breakBlock != null
                || this.placeBlock != null;
    }

    private String buildStableClickId() {
        String raw = material.name() + '|' +
                meta.getDisplayName() + '|' +
                meta.getLore() + '|' +
                String.valueOf(item.getDurability()) + '|' +
                meta.getEnchants() + '|' +
                (this.leftClick != null) + '|' +
                (this.rightClick != null) + '|' +
                (this.middleClick != null) + '|' +
                (this.breakBlock != null) + '|' +
                (this.placeBlock != null);
        return sha256(raw);
    }

    private static String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                String h = Integer.toHexString(b & 0xff);
                if (h.length() == 1) {
                    hex.append('0');
                }
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception ignored) {
            return Integer.toHexString(value.hashCode());
        }
    }

    private static boolean isLegacyServer() {
        return org.bukkit.Bukkit.getServer().getBukkitVersion().startsWith("1.8");
    }
}
