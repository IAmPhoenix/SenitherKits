package net.Senither.Kits.Events;

import java.util.Random;
import net.Senither.Kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

public class PlayerListener implements Listener
{

    private final Kits _plugin;

    public PlayerListener(Kits plugin)
    {
        _plugin = plugin;
    }

    @EventHandler
    public void onNameTag(PlayerReceiveNameTagEvent e)
    {
        String name = e.getNamedPlayer().getName();
        String useName = name;

        if (useName.length() > 14) {
            useName = useName.substring(0, 12);
        }

        if (_plugin.controller.hasPvPEnabled(name)) {
            if (_plugin.playerWorth.get(e.getNamedPlayer().getName()) >= 22) {
                e.setTag(ChatColor.RED + useName);
            } else if (_plugin.playerWorth.get(e.getNamedPlayer().getName()) <= 7) {
                e.setTag(ChatColor.YELLOW + useName);
            } else {
                e.setTag(ChatColor.AQUA + useName);
            }
        } else {
            e.setTag(ChatColor.WHITE + useName);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        e.setDeathMessage(null);
        e.getDrops().clear();

        Player player = e.getEntity();
        Player killer;

        if (e.getEntity().getKiller() instanceof Player) {
            killer = e.getEntity().getKiller();
        } else if (e.getEntity().getKiller() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity().getKiller();
            killer = (Player) arrow.getShooter();
        } else {
            // Player died to lava, void, falldamage or something else..
            return;
        }

        _plugin.playerDeaths.put(player.getName(), (_plugin.playerDeaths.get(player.getName()) + 1));
        _plugin.playerKills.put(killer.getName(), (_plugin.playerKills.get(killer.getName()) + 1));
        _plugin.playerLog.put(player.getName(), 0);

        // Get the money form both players
        double worth = _plugin.playerWorth.get(player.getName());
        double add = _plugin.playerWorth.get(killer.getName());

        // Extra bonus checker for %worth%
        if (add <= 4) {
            worth = worth + 10.35;
        } else if (add <= 6) {
            worth = worth + 6.55;
        } else if (add <= 8) {
            worth = worth + 2.61;
        } else if (add <= 10) {
            // Does nothing
        } else if (add <= 12) {
            worth = worth - 1.09;
        } else if (add <= 14) {
            worth = worth - 2.57;
        } else if (add <= 18) {
            worth = worth - 5.23;
        } else if (add <= 22) {
            worth = worth - 8.74;
        } else { // Higher then 22
            worth = worth - 11.11;
        }
        if (worth <= 1) {
            worth = 0.59;
        }

        add += 0.31;

        // Give the player the money
        _plugin.playerEco.put(killer.getName(), (_plugin.playerEco.get(killer.getName()) + worth));

        String nworth = "" + worth;
        String[] aworth = nworth.split("\\.");

        if (aworth[1].length() < 2) {
            aworth[1] += "0";
        }

        // Killstreak
        int playerKS = _plugin.playerKillstreak.get(player.getName());
        _plugin.playerKillstreak.put(player.getName(), 0);

        int killerKS = _plugin.playerKillstreak.get(killer.getName()) + 1;
        _plugin.playerKillstreak.put(killer.getName(), killerKS);
        
        // Resetting kits
        if(_plugin.playerUsingKits.contains(player.getName())) {
            _plugin.playerUsingKits.remove(player.getName());
        }

        // Killstreak broadcast
        boolean broadcastKillstreak = false;
        for (int i = 5; i < 1000;) {
            if (killerKS == i) {
                broadcastKillstreak = true;
            }
            i = i + 5;
        }

        if (broadcastKillstreak) {
            _plugin.chatManager.broadcastPluginMessage("&6" + killer.getName() + "&a is on a &6" + killerKS + "&a killstreak!");
            _plugin.controller.giveKillstreakReward(killer);
        }

        // Send Killers message
        _plugin.chatManager.sendMessage(killer, "&bYou received &e" + aworth[0] + "." + aworth[1].substring(0, 2) + " &bcredits for killing &e" + player.getName());
        _plugin.chatManager.sendMessage(killer, "&bYou now have a &eKill Streak &bof &e" + killerKS);

        // Send Players message
        _plugin.chatManager.sendMessage(player, "&e" + killer.getName() + " &breceived &e" + aworth[0] + "." + aworth[1].substring(0, 2) + " &bfor killing you");
        _plugin.chatManager.sendMessage(player, "&bYou died with a &eKill Streak &bof &e" + playerKS);

        // Set new player worth
        _plugin.playerWorth.put(killer.getName(), add);
        TagAPI.refreshPlayer(killer);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        e.setRespawnLocation(_plugin.mapHandler.getSpawnLocation());
        _plugin.controller.resetPlayer(e.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e)
    {
        Player target = null;
        Player damager = null;

        if (e.getEntity() instanceof Player) {
            target = (Player) e.getEntity();

            // Checking if the damged player has pvp enabled
            // If he/she has there are no point doing the rest of the code

            if (_plugin.controller.pvpList.contains(target.getName())) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getDamager() instanceof Player) {
            damager = (Player) e.getDamager();
        } else if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                damager = (Player) arrow.getShooter();
            }
        } else if (e.getDamager() instanceof Snowball) {
            Snowball snowball = (Snowball) e.getDamager();
            if (snowball.getShooter() instanceof Player) {
                damager = (Player) snowball.getShooter();

                // Apply snowball effects

                if (target.hasPotionEffect(PotionEffectType.SLOW)) {
                    Random random = new Random();
                    e.setDamage((random.nextInt(3) + 2D));
                    target.removePotionEffect(PotionEffectType.SLOW);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                } else {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                    _plugin.chatManager.sendMessage(damager, "&bYou have slowed " + target.getName() + " for &35 &bseconds!");
                }
            }
        }

        // Checks if the damager has pvp enabled
        if (damager != null && _plugin.controller.pvpList.contains(damager.getName())) {
            // Remove them from the pvp list
            _plugin.controller.pvpList.remove(damager.getName());
            _plugin.chatManager.sendMessage(damager, "You no longer have spawn protection!");
            TagAPI.refreshPlayer(damager);
        }

        if (damager != null) {
            _plugin.playerLog.put(damager.getName(), 5);
        }
        _plugin.playerLog.put(target.getName(), 5);
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent e)
    {
        if (((e.getEntity() instanceof Player)) && (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent e)
    {
        if (!_plugin.controller.hasHungerEnabled(e.getEntity().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e)
    {
        if (e.getItemDrop().getItemStack().getType() == Material.BOWL) {
            e.getItemDrop().remove();
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e)
    {
        if (e.getCurrentItem() == null || e.getCursor() == null || e.getWhoClicked() == null) {
            return;
        }

        if (e.getClickedInventory().getTitle().equals("Player Stats")) {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            _plugin.chatManager.sendMessage((Player) e.getWhoClicked(), "&cYou can't move that item!");
        }

        // Check if an inventory is being moved
        // Disable moving of weapon and armor
        if (!_plugin.controller.canBuild(e.getWhoClicked().getName())) {
            int id = e.getSlot();
            if (id == 0 || id == 36 || id == 37 || id == 38 || id == 39) {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                _plugin.chatManager.sendMessage((Player) e.getWhoClicked(), "&cYou can't move that item!");
            }
        }

    }

    @EventHandler(ignoreCancelled = false)
    public void onRightClick(PlayerInteractEvent e)
    {
        if (e.getItem() == null || e.getItem().getType() != Material.MUSHROOM_SOUP) {
            return;
        }

        Player player = (Player) e.getPlayer();

        if (player.getHealth() == 20 && player.getFoodLevel() == 20) {
            return;
        }

        if (player.getHealth() == 20 && player.getFoodLevel() != 20) { // Manege hunger
            int food = 0;

            if (player.getFoodLevel() + 2 >= 20) {
                food = 20;
            } else {
                food = player.getFoodLevel() + 2;
            }
            player.setFoodLevel(food);
            e.getItem().setType(Material.BOWL);
        } else { // Manege health level

            // Soup issue fix
            if (Bukkit.getPlayer(e.getPlayer().getName()).isDead()) {
                return;
            }

            double health = 0;

            if (player.getHealth() + 6 >= 20) {
                health = 20;
            } else {
                health = player.getHealth() + 6;
            }

            player.setHealth(health);
            e.getItem().setType(Material.BOWL);
        }
    }
}