package net.Senither.Kits.Events;

import net.Senither.Kits.Kits;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{

    private final Kits _plugin;

    public ChatListener(Kits plugin)
    {
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        e.setFormat(getPlayerScore(e.getPlayer().getName()) + e.getFormat());
    }

    private String getPlayerScore(String player)
    {
        int kills = _plugin.playerKills.get(player);
        int deaths = _plugin.playerDeaths.get(player);

        int score = kills - deaths;

        if (score >= 200) {
            return _plugin.chatManager.colorize("&8&l[&a" + score + "&8&l] &r");
        }

        return "";
    }
}
