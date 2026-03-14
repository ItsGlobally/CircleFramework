package top.itsglobally.CircleFramework.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.itsglobally.CircleFramework.annotation.AutoListener;
import top.itsglobally.CircleFramework.data.BasePlayer;
import top.itsglobally.CircleFramework.event.CFPlayerBeforeDestoryEvent;
import top.itsglobally.CircleFramework.event.CFPlayerCreateEvent;

@AutoListener
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        BasePlayer basePlayer = BasePlayer.create(event.getPlayer());
        Bukkit.getPluginManager().callEvent(new CFPlayerCreateEvent(basePlayer));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        BasePlayer basePlayer = BasePlayer.get(event.getPlayer());
        Bukkit.getPluginManager().callEvent(new CFPlayerBeforeDestoryEvent(basePlayer));
        BasePlayer.remove(event.getPlayer().getUniqueId());
    }



}
