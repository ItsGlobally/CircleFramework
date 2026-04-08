package top.itsglobally.CircleFramework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grinderwolf.swm.api.SlimePlugin;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import top.itsglobally.CircleFramework.command.CommandManager;
import top.itsglobally.CircleFramework.listener.ListenerManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePlugin<T extends BasePlugin> extends JavaPlugin {

    @Getter
    protected static Economy econ;
    private static BasePlugin<?> instance;
    @Getter
    private static BukkitAudiences adventure;
    @Getter
    private static Gson gson;
    @Getter
    private static SlimePlugin slimePlugin;

    @SuppressWarnings("unchecked")
    public static <T extends BasePlugin<?>> T getInstance() {
        return (T) instance;
    }

    @Override
    public void onLoad() {
        this.load();
    }

    @Override
    public void onEnable() {
        getLogger().info("正在初始化...");
        instance = this;
        VersionManager.init();
        adventure = BukkitAudiences.create(this);
        gson = new GsonBuilder().create();
        getLogger().info("正在註冊指令...");
        CommandManager.registerAll(this);
        getLogger().info("正在註冊世界...");
        ListenerManager.registerAll(this);
        getLogger().info("----------------------------------------------");
        getLogger().info("CircleFramework - v1.0 by ItsGlobally");
        getLogger().info("奏好可愛 :D");
        getLogger().info("Kanade is so cute :D");
        getLogger().info("----------------------------------------------");
        getLogger().info("正在檢查依賴...");
        List<String> missingDepends = checkDepends();
        if (missingDepends != null) {
            getLogger().warning("有不存在的依賴項! 由於沒有這些依賴項插件將無法工作, 插件將停止載入!");
            StringBuilder sb = new StringBuilder();
            for (String depend : missingDepends) {
                sb.append(depend).append(", ");
            }
            sb.setLength(sb.length() - 2);
            getLogger().warning("缺少: " + sb);
        }
        if (getDescription().getDepend().contains("Vault")) {
            getLogger().info("依賴項中找到Vault! 正在初始化經濟支援...");
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) econ = rsp.getProvider();
            else getLogger().warning("找不到經濟支援提供者! 經濟支援將無法工作!");
        }
        if (getDescription().getDepend().contains("SlimeWorldManager")) {
            getLogger().info("依賴項中找到SlimeWorldManager! 正在初始化Slime世界管理支援...");
            slimePlugin = (SlimePlugin) getServer().getPluginManager().getPlugin("SlimeWorldManager");
        }
        getLogger().info("CircleFramework初始化已完成!");
        this.enable();
        getLogger().info("插件已啟用!");
    }

    @Override
    public void onDisable() {
        this.disable();
    }

    protected void load() {
    }

    protected abstract void enable();

    protected abstract void disable();

    public void disablePlugin() {
        this.getPluginLoader().disablePlugin(this);
    }

    public List<String> checkDepends() {
        List<String> depends = this.getDescription().getDepend();
        List<String> missingPlugins = new ArrayList<>();
        boolean missSomething = false;
        for (String depend : depends) {
            if (Bukkit.getPluginManager().getPlugin(depend) == null) {
                missingPlugins.add(depend);
                missSomething = true;
            }
        }

        if (missSomething) return missingPlugins;
        return null;
    }

}