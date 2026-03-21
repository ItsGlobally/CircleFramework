package top.itsglobally.CircleFramework.listener;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import top.itsglobally.CircleFramework.annotation.AutoListener;

import java.lang.reflect.Modifier;

public class ListenerManager {

    public static void registerAll(JavaPlugin plugin) {
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(plugin.getClass().getPackageName(), "top.itsglobally.CircleFramework")
                .scan()) {

            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(AutoListener.class.getName())) {
                try {
                    Class<?> clazz = classInfo.loadClass();

                    if (!Listener.class.isAssignableFrom(clazz)) continue;

                    if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) continue;

                    Listener listenerInstance = (Listener) clazz.getDeclaredConstructor().newInstance();
                    Bukkit.getPluginManager().registerEvents(listenerInstance, plugin);
                    plugin.getLogger().info("已註冊事件" + clazz.getName());

                } catch (Exception e) {
                    plugin.getLogger().warning("無法註冊事件: " + classInfo.getName());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}