package net.Senither.Kits.api;

import net.Senither.Kits.Kits;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class KitsAPI
{

    private static Kits _plugin;

    public KitsAPI(Kits plugin)
    {
        _plugin = plugin;
    }

    public static FileConfiguration getPlayerConfig(String player)
    {
        return _plugin.playerConfig.get(player).getConfig();
    }
    
    public static Location getSpawnLocation()
    {
        return _plugin.mapHandler.getSpawnLocation();
    }
}
