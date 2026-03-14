package top.itsglobally.CircleFramework;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BasePlugin<T extends BasePlugin> extends JavaPlugin {

    private static BasePlugin<?> instance;
    @Getter
    private static BukkitAudiences adventure;

    @Override
    public void onLoad() {
        this.load();
    }

    @Override
    public void onEnable() {
        instance = this;
        VersionManager.init();
        this.enable();
        adventure = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {
        this.disable();

    }
    protected void load() {}

    protected abstract void enable();

    protected abstract void disable();

    public static <T extends BasePlugin<?>> T getInstance() {
        return (T) instance;
    }

    public void disablePlugin() {
        this.getPluginLoader().disablePlugin(this);
    }

}