package net.Senither.Kits.Events;

import net.Senither.Kits.Kits;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener
{

    private final Kits _plugin;

    public BlockListener(Kits plugin)
    {
        _plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (!_plugin.controller.canBuild(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (!_plugin.controller.canBuild(e.getPlayer().getName())) {
            // Check if the block is a tnt block
            if (e.getBlock().getType() == Material.TNT) {
                e.getBlock().setType(Material.AIR);
                TNTPrimed tnt = e.getPlayer().getWorld().spawn(e.getBlock().getLocation().add(0, 3, 0), TNTPrimed.class);
                tnt.setFuseTicks(30);
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosionPrime(EntityExplodeEvent e)
    {
        Entity ent = e.getEntity();
        if ((ent instanceof TNTPrimed) || (ent != null)) {
            e.blockList().clear();
            return;
        }
    }

    @EventHandler
    public void fadeBlockEvent(BlockFadeEvent e)
    {
        Material type = e.getBlock().getType();
        if (type == Material.ICE || type == Material.WATER || type == Material.LEAVES) {
            e.setCancelled(true);
        }
    }
}
