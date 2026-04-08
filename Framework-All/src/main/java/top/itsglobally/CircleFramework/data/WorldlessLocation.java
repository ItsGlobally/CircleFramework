package top.itsglobally.CircleFramework.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

@Getter
public class WorldlessLocation {
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;

    public WorldlessLocation(double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static WorldlessLocation fromLocation(Location location) {
        return new WorldlessLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z, pitch, yaw);
    }

    public Location toLocation(String world) {
        World world1 = Bukkit.getWorld(world);
        if (world1 == null) return null;
        return toLocation(world1);
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        map.put("yaw", yaw);
        map.put("pitch", pitch);
        return map;
    }

    public static WorldlessLocation fromMap(Map<String, Object> map) {
        try {
            double x = Double.parseDouble(map.get("x").toString());
            double y = Double.parseDouble(map.get("y").toString());
            double z = Double.parseDouble(map.get("z").toString());
            float yaw = Float.parseFloat(map.getOrDefault("yaw", 0).toString());
            float pitch = Float.parseFloat(map.getOrDefault("pitch", 0).toString());

            return new WorldlessLocation(x, y, z, yaw, pitch);
        } catch (Exception e) {
            return null;
        }
    }

}
