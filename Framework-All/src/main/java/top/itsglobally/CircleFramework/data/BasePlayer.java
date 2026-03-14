package top.itsglobally.CircleFramework.data;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public abstract class BasePlayer {
    @Getter
    public static Map<UUID, BasePlayer> players = new HashMap<>();
    private static Function<Player, ? extends BasePlayer> provider;
    public static void register(Function<Player, ? extends BasePlayer> factory) {
        provider = factory;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BasePlayer> T create(Player player) {

        if (provider == null) {
            throw new IllegalStateException("Player provider is not registered!");
        }

        return (T) provider.apply(player);
    }

    public static void remove(UUID uuid) {
        players.remove(uuid);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BasePlayer> T get(Player player) {
        return (T) players.get(player.getUniqueId());
    }

    @Getter
    private final Player player;

    protected BasePlayer(Player player) {
        this.player = player;
        players.put(player.getUniqueId(), this);
    }

    public String getName() {
        return player.getName();
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

}
