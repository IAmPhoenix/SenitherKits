package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ExtraCommands implements CommandExecutor
{

    private final Kits _plugin;

    public ExtraCommands(Kits plugin)
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

        if (commandLable.equalsIgnoreCase("extra")) {
            extraMenu(player);
        } else if (commandLable.equalsIgnoreCase("soup")) {
            soup(player);
        } else if (commandLable.equalsIgnoreCase("snowball")) {
            snowball(player);
        } else if (commandLable.equalsIgnoreCase("tnt")) {
            tnt(player);
        }

        return false;
    }

    private void extraMenu(Player player)
    {
        // Send help menu
        // Format: /command: Information
        _plugin.chatManager.sendMessage(player, " -------- &6Extra Menu&e --------");
        _plugin.chatManager.sendMessage(player, "&6/soup&f: Price: 6 - Gives you more soup");
        _plugin.chatManager.sendMessage(player, "&6/snowball&f: Price: 150 - 32 Snowballs of Slowness");
        _plugin.chatManager.sendMessage(player, "&6/tnt&f: Price: Price: 100 - 1 TNT Block");
    }

    private void snowball(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 150.00d)) {
            return;
        }

        Inventory i = player.getInventory();

        i.addItem(new ItemStack(Material.SNOW_BALL, 32));

        _plugin.controller.playerFinishTransaction(player, "32 Snowballs", 150.00d);
    }

    public void soup(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 6.00d)) {
            return;
        }

        Inventory i = player.getInventory();

        i.remove(Material.MUSHROOM_SOUP);
        i.remove(Material.BOWL);

        for (int s = 1; s <= 27; s++) {
            i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
        }

        // Add blindness effect for 90 ticks (4.5 seconds)
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 90, 1));

        _plugin.controller.playerFinishTransaction(player, "Soup", 6.00d);
        _plugin.chatManager.sendMessage(player, "You got blindness for 4 seconds for using /soup");
    }

    public void tnt(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 100.00d)) {
            return;
        }

        Inventory i = player.getInventory();

        i.addItem(new ItemStack(Material.TNT, 1));

        _plugin.controller.playerFinishTransaction(player, "TNT Block", 100.00d);
    }
}
