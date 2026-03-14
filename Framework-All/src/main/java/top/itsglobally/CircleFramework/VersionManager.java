package top.itsglobally.CircleFramework;

import lombok.Getter;
import org.bukkit.Bukkit;
import top.itsglobally.CircleFramework.data.Predefiend;

public class VersionManager {
    @Getter
    private static VersionAdapter adapter;

    public static void init() {

        String version = Bukkit.getServer().getBukkitVersion();
        String className = "";

        if (version.startsWith("1.8")) {
            className = "top.itsglobally.CircleFramework.versions.v1_8.Adapter_v1_8";
        } else if (version.startsWith("1.21")) {
            className = "top.itsglobally.CircleFramework.versions.v1_21.Adapter_v1_21";
        }

        try {
            adapter = (VersionAdapter) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Predefiend.getPlugin().getLogger().warning("版本相容載入失敗! 插件將停止載入");
            e.printStackTrace();
            Predefiend.getPlugin().disablePlugin();
        }
    }
}