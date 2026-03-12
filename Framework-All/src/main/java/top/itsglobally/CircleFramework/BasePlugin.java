package top.itsglobally.CircleFramework;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class BasePlugin<T extends BasePlugin> extends JavaPlugin {

    private static BasePlugin<?> instance;;

    @Override
    public void onLoad() {
        this.load();
    }

    @Override
    public void onEnable() {
        instance = this;
        VersionManager.init();
        this.enable();
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

}