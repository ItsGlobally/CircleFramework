package top.itsglobally.CircleFramework.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class Request {
    public final Player from;
    public final Player to;
    public final Task task;
    public final Map<String, String> args = new HashMap<>();
    @Setter
    public boolean expired = false;
}