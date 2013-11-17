package net.Senither.Kits.Events;

import net.Senither.Kits.Kits;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerListener implements Listener
{

    private final Kits _plugin;

    public ServerListener(Kits plugin)
    {
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        _plugin.controller.loadPlayer(e.getPlayer());
        _plugin.controller.resetPlayer(e.getPlayer(), false);
        _plugin.scoreboard.createPlayer(e.getPlayer());

        _plugin.chatManager.sendMessage(e.getPlayer(), _plugin.chatManager.prefix + "&bYou have joined the map &c&l" + _plugin.mapHandler.name);

        e.getPlayer().teleport(_plugin.mapHandler.getSpawnLocation());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e)
    {
        if (_plugin.playerLog.get(e.getPlayer().getName()) != 0) {
            _plugin.controller.resetPlayer(e.getPlayer(), true);
            e.getPlayer().setHealth(0d);
        }

        _plugin.controller.unloadPLayer(e.getPlayer());
        _plugin.controller.removePlayer(e.getPlayer());
    }
}
