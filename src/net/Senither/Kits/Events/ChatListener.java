package net.Senither.Kits.Events;

import net.Senither.Kits.Kits;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{

    private final Kits _plugin;
    private final String prefix = "&8&l[";
    private final String sufix = "&8&l] &r";

    public ChatListener(Kits plugin)
    {
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        String pvpScore = getPlayerScore(e.getPlayer().getName());
        String duelScore = getDuelScore(e.getPlayer().getName());

        if (pvpScore == null && duelScore != null) {
            e.setFormat(_plugin.chatManager.colorize(prefix + duelScore + sufix) + e.getFormat());
        } else if (pvpScore != null && duelScore == null) {
            e.setFormat(_plugin.chatManager.colorize(prefix + pvpScore + sufix) + e.getFormat());
        } else if (pvpScore != null && duelScore != null) {
            e.setFormat(_plugin.chatManager.colorize(prefix + pvpScore + "&6-" + duelScore + sufix) + e.getFormat());
        }
    }

    private String getPlayerScore(String player)
    {
        int kills = _plugin.playerKills.get(player);
        int deaths = _plugin.playerDeaths.get(player);

        int score = kills - deaths;

        if (score >= 200) {
            return _plugin.chatManager.colorize("&a" + score);
        }

        return null;
    }

    private String getDuelScore(String name)
    {
        int duelScore = _plugin.playerDuelScore.get(name);

        if (duelScore >= 50) {
            return _plugin.chatManager.colorize("&c" + duelScore);
        }

        return null;
    }
}
