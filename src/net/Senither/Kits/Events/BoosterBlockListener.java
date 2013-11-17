package net.Senither.Kits.Events;

import java.util.ArrayList;
import java.util.List;
import net.Senither.Kits.Kits;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.TagAPI;

public class BoosterBlockListener implements Listener
{

    private final Kits _plugin;
    private List<String> speedBoost = new ArrayList<String>();
    private List<String> jumpBoost = new ArrayList<String>();
    private List<String> slowDebuff = new ArrayList<String>();
    private List<String> witherDebuff = new ArrayList<String>();
    private List<String> fireDebuff = new ArrayList<String>();
    private List<String> nauseaDebuff = new ArrayList<String>();
    private List<String> explodeBoom = new ArrayList<String>();
    private List<String> teleportUP = new ArrayList<String>();
    private List<String> teleportDOWN = new ArrayList<String>();

    public BoosterBlockListener(Kits plugin)
    {
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Block block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);

        if (block.getState().getData() instanceof Wool) {

            DyeColor color = ((Wool) block.getState().getData()).getColor();

            final Player player = (Player) e.getPlayer();
            if (color == DyeColor.RED) { // Activate pvp

                if (_plugin.controller.pvpList.contains(player.getName())) {
                    _plugin.controller.pvpList.remove(player.getName());
                    _plugin.chatManager.sendMessage(player, "You no longer have spawn protection!");
                    TagAPI.refreshPlayer(player);
                }
            } else if (color == DyeColor.LIME) { // Speed boost

                if (!speedBoost.contains(player.getName())) {
                    // Give the player the effect
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 4));

                    // Disallow the player from getting this effect for 30 seconds
                    speedBoost.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&aSpeed Block &7has been activated for 6 seconds!");

                    // Start a 1.5 minute
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            speedBoost.remove(player.getName());
                        }
                    }, 1800);
                }
            } else if (color == DyeColor.YELLOW) { // Jump Boost

                if (!jumpBoost.contains(player.getName())) {
                    // Give the player the effect
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 70, 6));

                    // Disallow the player from getting this effect for 30 seconds
                    jumpBoost.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&eJump Block &7has been activated for 3 seconds!");

                    // Start a 30 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            jumpBoost.remove(player.getName());
                        }
                    }, 600);
                }
            } else if (color == DyeColor.BLACK) { // Black - Trap thingy

                if (!slowDebuff.contains(player.getName())) {
                    // Give the player the effect
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 100));

                    // Disallow the player from getting this effect for 30 seconds
                    slowDebuff.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&0Trap Block &7has been activated for 2 seconds!");

                    // Start a 30 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            slowDebuff.remove(player.getName());
                        }
                    }, 3000);
                }
            } else if (color == DyeColor.GRAY) { // Dark Gary - Add wither effect

                if (!witherDebuff.contains(player.getName())) {
                    // Give the player the effect
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));

                    // Disallow the player from getting this effect for 30 seconds
                    witherDebuff.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&8Wither Block &7has been adtivated for 5 seconds!");

                    // Start a 30 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            witherDebuff.remove(player.getName());
                        }
                    }, 1210);
                }
            } else if (color == DyeColor.MAGENTA) { // Magente - Nausea

                if (!nauseaDebuff.contains(player.getName())) {
                    // Give the player the effect
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 2));

                    // Disallow the player from getting this effect for 30 seconds
                    nauseaDebuff.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&5Nasuea Block &7has been activated for 10 seconds");

                    // Start a 30 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            nauseaDebuff.remove(player.getName());
                        }
                    }, 2400);
                }
            } else if (color == DyeColor.PINK) { // Pink - Fire

                if (!fireDebuff.contains(player.getName())) {
                    // Give the player the effect
                    player.setFireTicks(70);

                    // Disallow the player from getting this effect for 30 seconds
                    fireDebuff.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&dFire Block &7has been activated for a few seconds!");

                    // Start a 30 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            fireDebuff.remove(player.getName());
                        }
                    }, 600);
                }
            } else if (color == DyeColor.GREEN) { // Dark Green - TNT Trap

                if (!explodeBoom.contains(player.getName())) {

                    // Give the player the effect
                    Location l = player.getLocation();
                    l.getWorld().createExplosion(l.getBlockX(), (l.getBlockY() + 1), l.getBlockZ(), 2, false, false);

                    // Disallow the player from getting this effect
                    explodeBoom.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&2TNT Block &7just went BOOM!");

                    // Start a 30 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            explodeBoom.remove(player.getName());
                        }
                    }, 600);
                }
            } else if (color == DyeColor.LIGHT_BLUE) { // Light Blue - Teleport 10 blocks up

                if (!teleportUP.contains(player.getName())) {

                    // Teleport the player
                    player.teleport(player.getLocation().add(0d, 10d, 0d));

                    // Disallow the player from getting this effect
                    teleportDOWN.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&bYou just got teleported &310&b blocks up!");

                    // Start a 5 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            teleportDOWN.remove(player.getName());
                        }
                    }, 100);
                }
            } else if (color == DyeColor.BLUE) { // Dark Blue - Teleport 10 blocks down

                if (!teleportDOWN.contains(player.getName())) {

                    // Teleport the player
                    player.teleport(player.getLocation().subtract(0d, 10d, 0d));

                    // Disallow the player from getting this effect
                    teleportUP.add(player.getName());

                    // Send the player a message
                    _plugin.chatManager.sendMessage(player, "&bYou just got teleported &310&b blocks down!");

                    // Start a 5 seconds countdown
                    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            teleportUP.remove(player.getName());
                        }
                    }, 100);
                }
            }
        }
    }
}
