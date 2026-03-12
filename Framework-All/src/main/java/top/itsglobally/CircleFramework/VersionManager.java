package top.itsglobally.CircleFramework;

import lombok.Getter;
import org.bukkit.Bukkit;

public class VersionManager {
    @Getter
    private static VersionAdapter adapter;

    public static void init() {

        String version = Bukkit.getServer().getBukkitVersion();
        String className;

        if (version.startsWith("1.8")) {
            className = "top.itsglobally.CircleFramework.versions.v1_8.Adapter_v1_8";
        } else {
            className = "top.itsglobally.CircleFramework.versions.v1_21.Adapter_v1_21";
        }

        try {
            adapter = (VersionAdapter) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}