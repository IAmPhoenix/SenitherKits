package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmourCommands implements CommandExecutor
{

    private final Kits _plugin;

    public ArmourCommands(Kits plugin)
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

        if (commandLable.equalsIgnoreCase("armor")) {
            armourMenu(player);
        }

        if (_plugin.duel.isPlayerInBattle(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't use this command while in a duel!");
            return true;
        }

        if (commandLable.equalsIgnoreCase("diamond")) {
            diamond(player);
        } else if (commandLable.equalsIgnoreCase("iron")) {
            iron(player);
        } else if (commandLable.equalsIgnoreCase("gold")) {
            gold(player);
        } else if (commandLable.equalsIgnoreCase("leather")) {
            leather(player);
        }

        return false;
    }

    private void armourMenu(Player player)
    {
        // Send help menu
        // Format: /command: Information

        _plugin.chatManager.sendMessage(player, "&e -------- &6Armor Menu&e --------");
        _plugin.chatManager.sendMessage(player, "&6/diamond&f: Price: 750 Credits");
        _plugin.chatManager.sendMessage(player, "&6/iron&f: Price: 10 Credits");
        _plugin.chatManager.sendMessage(player, "&6/gold&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "&6/leather&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "");
        _plugin.chatManager.sendMessage(player, "&3Strong armor will give you less money per kill!");
        _plugin.chatManager.sendMessage(player, "&3Weak armor will give you more money per kill!");
    }

    public void diamond(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 750.00d)) {
            return;
        }

        player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1));

        _plugin.controller.playerFinishTransaction(player, "Diamond Armor", 750.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void iron(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 10.00d)) {
            return;
        }

        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));

        _plugin.controller.playerFinishTransaction(player, "Iron Armor", 10.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void gold(Player player)
    {
        player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));

        _plugin.chatManager.sendMessage(player, "&bYour &eGold Armor&b has been delivered");
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void leather(Player player)
    {
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS, 1));

        _plugin.chatManager.sendMessage(player, "&bYour &eLeather Armor&b has been delivered");
        _plugin.controller.calPlayerWorth(player, true);
    }
}
