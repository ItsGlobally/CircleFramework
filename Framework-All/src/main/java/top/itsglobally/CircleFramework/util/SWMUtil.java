package top.itsglobally.CircleFramework.util;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import top.itsglobally.CircleFramework.BasePlugin;

import java.io.IOException;

public class SWMUtil {

    public static final SlimePlugin SLIME_PLUGIN = BasePlugin.getSlimePlugin();

    public static void loadWorld(SlimeLoader slimeLoader, String name, boolean readonly, SlimePropertyMap slimePropertyMap) throws CorruptedWorldException, NewerFormatException, WorldInUseException, UnknownWorldException, IOException {
        SLIME_PLUGIN.generateWorld(BasePlugin.getSlimePlugin().loadWorld(slimeLoader, name, readonly, slimePropertyMap));
    }

    public static void loadWorld(SlimeLoader slimeLoader, String name, SlimePropertyMap slimePropertyMap) throws CorruptedWorldException, NewerFormatException, WorldInUseException, UnknownWorldException, IOException {
        loadWorld(slimeLoader, name, false, slimePropertyMap);
    }

    public static void unloadWorld(String name, boolean save) {
        Bukkit.unloadWorld(name, save);
    }

    public static void unloadWorld(String name) {
        unloadWorld(name, true);
    }

    public static void createEmpty(SlimeLoader slimeLoader, String name, boolean readonly, SlimePropertyMap slimePropertyMap) throws IOException, WorldAlreadyExistsException {
        SLIME_PLUGIN.generateWorld(SLIME_PLUGIN.createEmptyWorld(slimeLoader, name, readonly, slimePropertyMap));
    }

    public static void createEmpty(SlimeLoader slimeLoader, String name, SlimePropertyMap slimePropertyMap) throws IOException, WorldAlreadyExistsException {
        createEmpty(slimeLoader, name, false, slimePropertyMap);
    }

    public static void delete(SlimeLoader slimeLoader, String name) throws IOException, UnknownWorldException {
        slimeLoader.deleteWorld(name);
    }

    public static void cloneWorld(SlimeLoader slimeLoader, String name, String newName, SlimePropertyMap slimePropertyMap) throws CorruptedWorldException, NewerFormatException, WorldInUseException, UnknownWorldException, IOException, WorldAlreadyExistsException {
        SlimeWorld slimeWorld = BasePlugin.getSlimePlugin().loadWorld(slimeLoader, name, true, slimePropertyMap);
        SLIME_PLUGIN.generateWorld(slimeWorld.clone(newName, slimeLoader, true));
    }
}
