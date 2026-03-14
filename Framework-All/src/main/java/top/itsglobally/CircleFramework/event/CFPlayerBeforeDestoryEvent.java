package top.itsglobally.CircleFramework.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import top.itsglobally.CircleFramework.data.BasePlayer;

@Getter
public class CFPlayerBeforeDestoryEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final BasePlayer player;

    public CFPlayerBeforeDestoryEvent(BasePlayer player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
