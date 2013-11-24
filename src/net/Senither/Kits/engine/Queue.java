package net.Senither.Kits.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kitteh.tag.TagAPI;

public class Queue
{

    private List<String> players = new ArrayList<String>();
    private HashMap<String, Inventory> inventorys = new HashMap<String, Inventory>();
    private HashMap<String, ItemStack[]> armorSlots = new HashMap<String, ItemStack[]>();

    /**
     * Add two players to the queue
     * 
     * @param challanger
     * @param challange 
     */
    public Queue(Player challanger, Player challange)
    {
        players.add(challanger.getName());
        players.add(challange.getName());
    }

    /**
     * Returns a list of the players names
     * 
     * @return String
     */
    public List<String> getPlayerNames()
    {
        return players;
    }

    /**
     * Returns a list of the player objects
     * 
     * @return Player
     */
    public List<Player> getPlayers()
    {
        List<Player> players = new ArrayList<Player>();
        players.add(Bukkit.getPlayer(this.players.get(0)));
        players.add(Bukkit.getPlayer(this.players.get(1)));
        return players;
    }

    /**
     * Returns the challanger player object
     * 
     * @return Player
     */
    public Player getChallanger()
    {
        return Bukkit.getPlayer(players.get(0));
    }

    /**
     * Returns the challange player object
     * 
     * @return Player
     */
    public Player getChallange()
    {
        return Bukkit.getPlayer(players.get(1));
    }

    /**
     * Save the players inventory and armor.
     */
    public void savePlayerData()
    {
        inventorys.clear();
        armorSlots.clear();

        for (Player player : getPlayers()) {
            String key = player.getName();
            inventorys.put(key, player.getInventory());
            armorSlots.put(key, player.getInventory().getArmorContents());
        }
    }

    /**
     * Reset both players inventorys and armor
     * See "savePlayerData" for more info.
     */
    public void resetPlayerData()
    {
        for (Player player : getPlayers()) {
            if (player != null) {
                String key = player.getName();
                player.getInventory().setContents(inventorys.get(key).getContents());
                player.getInventory().setArmorContents(armorSlots.get(key));
            }
        }
    }

    /**
     * Reverse everything the query has done
     */
    public void cancelQueue(Location spawnPoint)
    {
        for (Player player : getPlayers()) {
            if (player != null) {
                player.setHealth(20d);
                player.setFoodLevel(20);
                player.setFireTicks(0);
                player.teleport(spawnPoint);
                TagAPI.refreshPlayer(player);
            }
        }
        // Reset the players inventory
        resetPlayerData();
    }
}
