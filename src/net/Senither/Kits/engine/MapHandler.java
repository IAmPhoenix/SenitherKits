package net.Senither.Kits.engine;

import java.util.ArrayList;
import java.util.List;
import net.Senither.Kits.Kits;
import net.Senither.Kits.api.events.ChangeMapEvent;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class MapHandler implements CommandExecutor
{

    private final Kits _plugin;
    public int countdown;
    public int selectedArenaID = 0;
    public int preSelectedArenaID = 0;
    public List<String> arenas = new ArrayList<String>();
    public String name;
    public int x;
    public int y;
    public int z;
    public int pitch;
    public int yaw;
    public String world;

    public MapHandler(Kits plugin)
    {
        _plugin = plugin;

        arenas = (List<String>) _plugin.getConfig().getList("arenas");
        countdown = _plugin.getConfig().getInt("mapChange");

        if (arenas.size() != 0) {
            _plugin.chatManager.LogInfo("Arenas Loaded Complete!");
        } else {
            _plugin.chatManager.LogInfo("Failed to Load the Arenas!");
            _plugin.getServer().getPluginManager().disablePlugin(_plugin);
        }

        String arenaDefaultName = arenas.get(0);
        name = _plugin.getConfig().getString("arena." + arenaDefaultName + ".name");
        world = _plugin.getConfig().getString("arena." + arenaDefaultName + ".world");
        x = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".x");
        y = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".y");
        z = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".z");
        pitch = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".pitch");
        yaw = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".yaw");

        MapRotate();
    }

    public void MapRotate()
    {
        _plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if (countdown == 3600) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 60 minutes!");
                } else if (countdown == 1800) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 30 minutes!");
                } else if (countdown == 600) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 10 minutes!");
                } else if (countdown == 300) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 5 minutes!");
                } else if (countdown == 60) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 1 minute!");
                } else if (countdown == 30) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 30 seconds!");
                } else if (countdown == 11) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 10 seconds!");
                } else if (countdown == 6) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 5 seconds!");
                } else if (countdown == 5) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 4 seconds!");
                } else if (countdown == 4) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 3 seconds!");
                } else if (countdown == 3) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 2 seconds!");
                } else if (countdown == 2) {
                    _plugin.chatManager.broadcastPluginMessage("&7Cycling to a new map in 1 second!");
                } else if (countdown == 1) {
                    countdown = _plugin.getConfig().getInt("mapChange") + 5;
                    changeMap();
                }

                countdown--;
            }
        }, 40, 20);
    }

    public void changeMap()
    {
        if (selectedArenaID == (arenas.size() - 1)) {
            selectedArenaID = 0;
        } else {
            selectedArenaID++;
        }

        String arenaDefaultName = arenas.get(selectedArenaID);
        name = _plugin.getConfig().getString("arena." + arenaDefaultName + ".name");
        world = _plugin.getConfig().getString("arena." + arenaDefaultName + ".world");
        x = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".x");
        y = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".y");
        z = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".z");
        pitch = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".p");
        yaw = _plugin.getConfig().getInt("arena." + arenaDefaultName + ".w");

        _plugin.getServer().getPluginManager().callEvent(new ChangeMapEvent(getSpawnLocation(), name));

        _plugin.chatManager.broadcastPluginMessage("&5&lNow playing &6&l" + name + "!");

        Location location = getSpawnLocation();

        location.getWorld().setDifficulty(Difficulty.HARD);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(location);

            // Disable PvP
            if (_plugin.controller.hasPvPEnabled(player.getName())) {
                _plugin.controller.pvpList.add(player.getName());
                TagAPI.refreshPlayer(player);
            }
        }
    }

    public Location getSpawnLocation()
    {
        Location location = new Location(Bukkit.getWorld(world), x, y, z);
        location.setPitch(pitch);
        location.setYaw(yaw);
        return location;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args)
    {
        Player player = null;
        
        if(sender instanceof Player) {
            player = (Player) sender;
        } else {
            _plugin.chatManager.sendMessage(sender, "You can't use that command in-game!");
            return true;
        }
        
        if(!player.hasPermission(_plugin.permissions.MANAGE_MAP)) {
            _plugin.chatManager.missingPermission(player, _plugin.permissions.MANAGE_MAP);
            return true;
        }
        
        _plugin.chatManager.sendMessage(player, " &eThis feature has not yet been implemented!");
        
        return true;
    }
}
