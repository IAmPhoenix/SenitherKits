package net.Senither.Kits.engine;

import java.util.ArrayList;
import java.util.List;
import net.Senither.Kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class DuelHandler
{

    private final Kits _plugin;
    private List<Queue> queue = new ArrayList<Queue>();
    private List<Location> spawnPoints = new ArrayList<Location>();
    protected Queue inProgress = null;
    private int stage = 0, counter = 5;

    public DuelHandler(Kits plugin)
    {
        _plugin = plugin;

        _plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if (stage == 0) {
                    // No dual in progress, looking for one..
                    if (queue.size() != 0) {
                        // There are players ready to dual! :O
                        inProgress = queue.get(0);

                        // Remove them from the queue, no need to keep them two places
                        queue.remove(0);

                        // Tell the players they will be teleported soon
                        for (Player player : inProgress.getPlayers()) {
                            _plugin.chatManager.sendMessage(player, "&3You're first in line for your dual!");
                            _plugin.chatManager.sendMessage(player, "&3You will be teleported shortly!");
                        }

                        // Starts the countdown
                        stage = 1;
                        counter = 5;
                    }
                } else if (stage == 1) {
                    // Teleport Stage

                    if (counter == 0) {
                        inProgress.savePlayerData();

                        int index = 0;
                        for (Player player : inProgress.getPlayers()) {
                            _plugin.chatManager.sendMessage(player, "&3Teleporting...");

                            _plugin.controller.resetPlayer(player, true);

                            player.setHealth(20d);
                            player.setFoodLevel(20);

                            player.teleport(spawnPoints.get(index));
                            index++;
                        }
                        counter = 5;
                        stage = 2;
                    }
                    counter--;
                } else if (stage == 2) {
                    // Starting stage
                    switch (counter) {
                        case 5:
                        case 4:
                        case 3:
                        case 2:
                        case 1:
                            for (Player player : inProgress.getPlayers()) {
                                _plugin.chatManager.sendMessage(player, "&3The battle will begin in &b" + counter + " &3second" + ((counter == 1) ? "" : "s") + "!");
                            }
                            break;
                        case 0:
                            for (Player player : inProgress.getPlayers()) {
                                _plugin.chatManager.sendMessage(player, "&3The battle has begun!");
                                _plugin.controller.pvpList.remove(player.getName());
                                TagAPI.refreshPlayer(player);
                            }
                            counter = 300; // Give the players five minutes
                            stage = 3;
                            break;
                    }
                    counter--;
                } else if (stage == 3) {
                    // Battle stage
                    if (counter == 0) {
                        inProgress.cancelQueue(_plugin.mapHandler.getSpawnLocation());
                        inProgress = null;
                        counter = 6;
                        stage = 0;
                    }
                    counter--;
                }
            }
        }, 40, 20);

    }

    public boolean addPlayers(Player challanger, Player challange)
    {
        if (isPlayersInQueue(challanger.getName(), challange.getName())) {
            return false;
        }

        queue.add(new Queue(challanger, challange));

        return true;
    }

    public boolean isPlayerInQueue(String player)
    {
        for (Queue q : queue) {
            for (String p : q.getPlayerNames()) {
                if (p.equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPlayersInQueue(String challanger, String challange)
    {
        for (Queue q : queue) {
            for (String p : q.getPlayerNames()) {
                if (p.equals(challanger) || p.equals(challange)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPlayerInBattle(String player)
    {
        for (String p : inProgress.getPlayerNames()) {
            if (p.equals(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayersInBattle(String challanger, String challange)
    {
        for (String p : inProgress.getPlayerNames()) {
            if (p.equals(challanger) || p.equals(challange)) {
                return true;
            }
        }
        return false;
    }

    public Queue getQueue(String player)
    {
        for (Queue q : queue) {
            for (String p : q.getPlayerNames()) {
                if (p.equals(player)) {
                    return q;
                }
            }
        }
        return null;
    }

    public void removePlayer(String player)
    {
        // The only way to get out the dual que is to leave the server
        Queue q = getQueue(player);

        if (q == null) {
            // Player is not in a queue, check if they're in a duel now

            boolean check = false;
            String other = null;
            for (String sender : inProgress.getPlayerNames()) {
                if (sender.equals(sender)) {
                    check = true;
                } else {
                    other = sender;
                }
            }

            if (check) {
                _plugin.chatManager.sendMessage(Bukkit.getPlayer(other), "&b" + player + " &3has logged out!");
                _plugin.chatManager.sendMessage(Bukkit.getPlayer(other), "&3You have been kicked from the duel queue!");

                inProgress.cancelQueue(_plugin.mapHandler.getSpawnLocation());
                inProgress = null;
                counter = 6;
                stage = 0;
            }

            return;
        }

        for (String sender : q.getPlayerNames()) {
            if (!sender.equals(player)) {
                _plugin.chatManager.sendMessage(Bukkit.getPlayer(sender), "&b" + player + " &3has logged out!");
                _plugin.chatManager.sendMessage(Bukkit.getPlayer(sender), "&3You have been kicked from the duel queue!");
            }
        }
        queue.remove(q);
    }
}
