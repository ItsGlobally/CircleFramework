package top.itsglobally.CircleFramework.data;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class YamlFile {

    private final File file;
    private FileConfiguration config;
    /*
    YamlFile(String path)
    Full path of the file.
    If the file is in plugin data folder use YamlFile(plugin.getDataFolder() + "/name.yml")
     */
    public YamlFile(String path) {
        this.file = new File(path);

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
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }
}