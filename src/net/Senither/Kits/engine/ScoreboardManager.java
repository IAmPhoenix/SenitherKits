package net.Senither.Kits.engine;

import net.Senither.Kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager
{

    protected Team playerTeam;
    private final Kits _plugin;

    public ScoreboardManager(Kits plugin)
    {
        _plugin = plugin;

        updateScoreboards();
    }

    public void createPlayer(Player p)
    {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective("Stats", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.GOLD + "   Your Stats   ");

        playerTeam = board.registerNewTeam("player");
        playerTeam.setAllowFriendlyFire(true);

        playerTeam.addPlayer(Bukkit.getOfflinePlayer("Your Worth:"));
        playerTeam.addPlayer(Bukkit.getOfflinePlayer("Credits:"));
        playerTeam.addPlayer(Bukkit.getOfflinePlayer("Kills:"));
        playerTeam.addPlayer(Bukkit.getOfflinePlayer("Deaths:"));
        playerTeam.addPlayer(Bukkit.getOfflinePlayer("Killstreak:"));
        playerTeam.addPlayer(Bukkit.getOfflinePlayer("K/D Ratio:"));
        playerTeam.addPlayer(Bukkit.getOfflinePlayer("PvP Score:"));

        p.setScoreboard(board);

        Scoreboard b = p.getScoreboard();
        Objective o = b.getObjective(DisplaySlot.SIDEBAR);
        Score worthScore = o.getScore(Bukkit.getOfflinePlayer("Your Worth:"));
        Score killstreakScore = o.getScore(Bukkit.getOfflinePlayer("Killstreak:"));
        Score killsScore = o.getScore(Bukkit.getOfflinePlayer("Kills:"));
        Score deathsScore = o.getScore(Bukkit.getOfflinePlayer("Deaths:"));
        Score creditsScore = o.getScore(Bukkit.getOfflinePlayer("Credits:"));
        Score kdScore = o.getScore(Bukkit.getOfflinePlayer("K/D Ratio:"));
        Score pvpScore = o.getScore(Bukkit.getOfflinePlayer("PvP Score:"));

        worthScore.setScore(999);
        creditsScore.setScore(999);
        killsScore.setScore(999);
        deathsScore.setScore(999);
        killstreakScore.setScore(999);
        kdScore.setScore(999);
        pvpScore.setScore(999);
    }

    public void updatePlayer(Player player)
    {
        Scoreboard b = player.getScoreboard();

        if (b != null) {
            Objective o = b.getObjective(DisplaySlot.SIDEBAR);
            if (o != null) {
                String name = player.getName();

                Score worthScore = o.getScore(Bukkit.getOfflinePlayer("Your Worth:"));
                int worth = (int) Math.floor(_plugin.playerWorth.get(name));

                Score killstreakScore = o.getScore(Bukkit.getOfflinePlayer("Killstreak:"));
                int killstreak = _plugin.playerKillstreak.get(name);

                Score killsScore = o.getScore(Bukkit.getOfflinePlayer("Kills:"));
                int kills = _plugin.playerKills.get(name);

                Score deathsScore = o.getScore(Bukkit.getOfflinePlayer("Deaths:"));
                int deaths = _plugin.playerDeaths.get(name);

                Score creditsScore = o.getScore(Bukkit.getOfflinePlayer("Credits:"));
                int credits = (int) Math.floor(_plugin.playerEco.get(name));

                Score kdScore = o.getScore(Bukkit.getOfflinePlayer("K/D Ratio:"));
                int kdRatio = kills;
                if (kills != 0 && deaths != 0) {
                    kdRatio = (int) Math.ceil(kills / deaths);
                }

                Score pvpScore = o.getScore(Bukkit.getOfflinePlayer("PvP Score:"));

                worthScore.setScore(worth);
                creditsScore.setScore(credits);
                killsScore.setScore(kills);
                deathsScore.setScore(deaths);
                killstreakScore.setScore(killstreak);
                kdScore.setScore(kdRatio);
                pvpScore.setScore(kills - deaths);

                player.setScoreboard(b);
            }
        }
    }

    public void updateScoreboards()
    {
        _plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, new Runnable()
        {
            @Override
            public void run()
            {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayer(player);
                }
            }
        }, 0L, 40);
    }
}
