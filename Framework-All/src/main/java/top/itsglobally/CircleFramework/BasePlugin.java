package top.itsglobally.CircleFramework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private static BasePlugin<?> instance;
    @Getter
    private static BukkitAudiences adventure;
    @Getter
    protected static Economy econ;
    @Getter
    private static Gson gson;

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
        CommandManager.registerAll(this);
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
            sb.setLength(sb.length() -2);
            getLogger().warning("缺少: " + sb);
        }
        if (getDescription().getDepend().contains("Vault")) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) econ = rsp.getProvider();
        }
        getLogger().info("CircleFramework初始化已完成! 正在初始化插件...");
        this.enable();
    }

    @Override
    public void onDisable() {
        this.disable();

    }
    protected void load() {}

    protected abstract void enable();

    protected abstract void disable();

    @SuppressWarnings("unchecked")
    public static <T extends BasePlugin<?>> T getInstance() {
        return (T) instance;
    }

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