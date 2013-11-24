package net.Senither.Kits.engine;

import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<String, String> challangeList = new HashMap<String, String>();
    protected Queue inProgress = null;
    private int stage = 0, counter = 5;

    public DuelHandler(Kits plugin)
    {
        _plugin = plugin;

        Location spawn1 = new Location(Bukkit.getWorld(
                _plugin.getConfig().getString("duel.1.world")),
                _plugin.getConfig().getDouble("duel.1.x"),
                _plugin.getConfig().getDouble("duel.1.y"),
                _plugin.getConfig().getDouble("duel.1.z"));
        spawn1.setPitch(_plugin.getConfig().getInt("duel.1.pitch"));
        spawn1.setYaw(_plugin.getConfig().getInt("duel.1.yaw"));

        Location spawn2 = new Location(Bukkit.getWorld(
                _plugin.getConfig().getString("duel.2.world")),
                _plugin.getConfig().getDouble("duel.2.x"),
                _plugin.getConfig().getDouble("duel.2.y"),
                _plugin.getConfig().getDouble("duel.2.z"));
        spawn2.setPitch(_plugin.getConfig().getInt("duel.2.pitch"));
        spawn2.setYaw(_plugin.getConfig().getInt("duel.2.yaw"));

        spawnPoints.add(spawn1);
        spawnPoints.add(spawn2);

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
                            _plugin.chatManager.sendMessage(player, "&3You're first in line for your duel!");
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
                        counter = 6;
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
                            counter = 301; // Give the players five minutes
                            stage = 3;
                            break;
                    }
                    counter--;
                } else if (stage == 3) {
                    // Battle stage
                    if (counter == 0) {
                        for (Player player : inProgress.getPlayers()) {
                            _plugin.chatManager.sendMessage(player, "&3Times over, resetting inventorys and teleporting you back..");
                        }

                        endDuel();
                    } else if (counter == 60) {
                        for (Player player : inProgress.getPlayers()) {
                            _plugin.chatManager.sendMessage(player, "&3You have &b1 &3minute left before the match will end in a tie!");
                        }
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
        if (inProgress == null) {
            return false;
        }

        for (String p : inProgress.getPlayerNames()) {
            if (p.equals(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayersInBattle(String challanger, String challange)
    {
        if (inProgress == null) {
            return false;
        }

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

    public String getBattlePartner(String player)
    {
        for (String name : inProgress.getPlayerNames()) {
            if (!name.equals(player)) {
                return name;
            }
        }
        return null;
    }

    public void endDuel()
    {
        if (inProgress == null) {
            return;
        }

        for (String player : inProgress.getPlayerNames()) {
            if (!_plugin.controller.pvpList.contains(player)) {
                _plugin.controller.pvpList.add(player);
            }
        }

        inProgress.cancelQueue(_plugin.mapHandler.getSpawnLocation());
        inProgress.resetPlayerData();
        inProgress = null;
        counter = 6;
        stage = 0;
    }

    public void removePlayer(String player)
    {
        // The only way to get out the dual que is to leave the server
        Queue q = getQueue(player);

        if (q == null) {
            // Player is not in a queue, check if they're in a duel now

            if (inProgress == null) {
                return;
            }

            boolean check = false;
            String other = null;
            for (String sender : inProgress.getPlayerNames()) {
                if (sender.equals(player)) {
                    check = true;
                } else {
                    other = sender;
                }
            }

            if (check) {
                _plugin.chatManager.sendMessage(Bukkit.getPlayer(other), "&b" + player + " &3has logged out!");
                _plugin.chatManager.sendMessage(Bukkit.getPlayer(other), "&3Seems like you win!");

                _plugin.controller.manageDuelRewards(Bukkit.getPlayer(other), Bukkit.getPlayer(player));

                _plugin.chatManager.broadcastPluginMessage("&b" + other + " &3has won a duel against &b" + player + "&3!");

                endDuel();
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

    public void challangePlayer(Player challanger, Player challange)
    {
        if (this.challangeList.containsValue(challange.getName()) && this.challangeList.get(challanger.getName()).equals(challange.getName())) {
            _plugin.chatManager.sendMessage(challanger, "&aYou have accepted " + challange.getName() + "'s duel request!");
            _plugin.chatManager.sendMessage(challanger, "&aYou have been added to the queue!");

            _plugin.chatManager.sendMessage(challange, "&a" + challanger.getName() + " has accepted your duel request!");
            _plugin.chatManager.sendMessage(challange, "&aYou have been added to the queue!");

            this.challangeList.remove(challanger.getName());

            addPlayers(challanger, challange);
            return;
        }

        if (isPlayerInBattle(challange.getName()) || isPlayerInQueue(challange.getName())) {
            _plugin.chatManager.sendMessage(challanger, "&c" + challange.getName() + " is already in a duel queue!");
            return;
        }

        if (this.challangeList.containsKey(challange.getName())) {
            _plugin.chatManager.sendMessage(challanger, "&c" + challange.getName() + " has already been challanged by another player!");
            return;
        }

        this.challangeList.put(challange.getName(), challanger.getName());
        _plugin.chatManager.sendMessage(challange, "&b" + challanger.getName() + " &3has challanged you to a duel!");
        _plugin.chatManager.sendMessage(challange, "&3Use &b/duel " + challanger.getName() + " &3to accept it!");
        _plugin.chatManager.sendMessage(challanger, "&3You have challanged &b" + challange.getName() + " &3to a duel!");

        final String challangerF = challanger.getName();
        final String challangeF = challange.getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
        {
            public void run()
            {
                if (challangeList.containsKey(challangeF)) {
                    challangeList.remove(challangeF);
                    Player playerChallangerF = Bukkit.getPlayer(challangerF);
                    if (playerChallangerF != null) {
                        _plugin.chatManager.sendMessage(playerChallangerF, "&c" + challangeF + " didn't accept your duel request!");
                    }
                }
            }
        }, 300); // 15 seconds
    }
}
