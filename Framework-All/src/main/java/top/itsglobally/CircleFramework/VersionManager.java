package top.itsglobally.CircleFramework;

import lombok.Getter;
import org.bukkit.Bukkit;
import top.itsglobally.CircleFramework.core.VersionAdapter;

import top.itsglobally.CircleFramework.data.Predefiend;
import top.itsglobally.CircleFramework.versions.v1_21.Adapter_v1_21;
import top.itsglobally.CircleFramework.versions.v1_8.Adapter_v1_8;

public class VersionManager {
    @Getter
    private static VersionAdapter adapter;

    public static void init() {

        String version = Bukkit.getServer().getBukkitVersion();

        if (version.startsWith("1.8")) {
            adapter = new Adapter_v1_8();
        } else if (version.startsWith("1.21")) {
            adapter = new Adapter_v1_21();
        } else {
            Predefiend.getPlugin().getLogger().warning("不相容的伺服器! 將停止載入");
            Predefiend.getPlugin().disablePlugin();
        }
    }
}