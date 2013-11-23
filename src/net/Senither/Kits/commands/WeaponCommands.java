package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WeaponCommands implements CommandExecutor
{

    private final Kits _plugin;

    public WeaponCommands(Kits plugin)
    {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args)
    {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            _plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }

        if (commandLable.equalsIgnoreCase("weapons")) {
            weaponMenu(player);
            return true;
        }
        
        if(_plugin.playerUsingKits.contains(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't use this command while using a kit!");
            return true;
        }
        
        if (commandLable.equalsIgnoreCase("sword")) {
            sword(player);
        } else if (commandLable.equalsIgnoreCase("bow")) {
            bow(player);
        } else if (commandLable.equalsIgnoreCase("axe")) {
            axe(player);
        }

        return false;
    }

    private void weaponMenu(Player player)
    {
        // Send help menu
        // Format: /command: Information
        _plugin.chatManager.sendMessage(player, "&e -------- &6Weapon Menu&e --------");
        _plugin.chatManager.sendMessage(player, "&6/sword&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "&6/bow&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "&6/axe&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "");
        _plugin.chatManager.sendMessage(player, "&3When you change you will lose ALL enchants on your weapon!");
    }

    public void sword(Player player)
    {
        Inventory i = player.getInventory();

        removeWeapons(i);

        i.addItem(new ItemStack(Material.DIAMOND_SWORD, 1));

        // Send the complete message to the player
        _plugin.chatManager.sendMessage(player, "&bYou change your weapon of choice to a &eSword&b!");
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void bow(Player player)
    {
        Inventory i = player.getInventory();

        removeWeapons(i);

        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        i.addItem(bow);
        i.addItem(new ItemStack(Material.ARROW, 1));

        _plugin.chatManager.sendMessage(player, "&bYou change your weapon of choice to a &eBow&b!");
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void axe(Player player)
    {
        Inventory i = player.getInventory();

        removeWeapons(i);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);

        i.addItem(axe);

        _plugin.chatManager.sendMessage(player, "&bYou change your weapon of choice to a &eAxe&b!");
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void removeWeapons(Inventory i)
    {
        i.remove(Material.BOW);
        i.remove(Material.ARROW);
        i.remove(Material.DIAMOND_SWORD);
        i.remove(Material.DIAMOND_AXE);
    }
}
