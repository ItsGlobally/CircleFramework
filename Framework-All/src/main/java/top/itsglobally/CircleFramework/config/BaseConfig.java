package top.itsglobally.CircleFramework.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.itsglobally.CircleFramework.BasePlugin;
import top.itsglobally.CircleFramework.data.Predefiend;

import java.io.File;
import java.io.IOException;

@Getter
public class BaseConfig {

    private final File file;
    private FileConfiguration fileConfiguration;

    public BaseConfig(String path) {
        this.file = new File(Predefiend.getPlugin().getDataFolder(), path);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        reload();
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
