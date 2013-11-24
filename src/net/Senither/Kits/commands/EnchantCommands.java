package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommands implements CommandExecutor
{

    private final Kits _plugin;

    public EnchantCommands(Kits plugin)
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

        if (commandLable.equalsIgnoreCase("enchants")) {
            enchantMenu(player);
            return true;
        }
        
        if(_plugin.playerUsingKits.contains(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't use this command while wearing a special kit a kit!");
            return true;
        } else if(_plugin.duel.isPlayerInBattle(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't use this command while in a duel!");
            return true;
        }
        
        if (commandLable.equalsIgnoreCase("sharp")) {
            sharpness(player);
        } else if (commandLable.equalsIgnoreCase("power")) {
            power(player);
        } else if (commandLable.equalsIgnoreCase("knockback")) {
            knockback(player);
        } else if (commandLable.equalsIgnoreCase("protect")) {
            protection(player);
        }

        return false;
    }

    private void enchantMenu(Player player)
    {
        // Send help menu
        // Format: /command: Information

        _plugin.chatManager.sendMessage(player, "&e -------- &6Enchant Menu&e --------");
        _plugin.chatManager.sendMessage(player, "&6/sharp&f: Price: 5, 40, 100, 255");
        _plugin.chatManager.sendMessage(player, "&6/power&f: Price: 5, 40, 100, 255");
        _plugin.chatManager.sendMessage(player, "&6/knockback&f: Price: 60, 140, 250");
        _plugin.chatManager.sendMessage(player, "&6/protect&f: Price: 100(255 for diamond), 250, 400");
        _plugin.chatManager.sendMessage(player, "");
        _plugin.chatManager.sendMessage(player, "&3Make yourself powerful but get less money for your kills!");
        _plugin.chatManager.sendMessage(player, "&3Use a command twice to get level two, three and four of the enchant for a higher price every upgrade");
    }

    private void sharpness(Player player)
    {
        ItemStack weapon = player.getInventory().getItemInHand();

        if (player.getInventory().getItemInHand() != null) {
            if (!(weapon.getType() == Material.DIAMOND_SWORD || weapon.getType() == Material.DIAMOND_AXE)) {
                _plugin.chatManager.sendMessage(player, "&cPlease hold a Sword or Axe in your hand to use this command");
                return;
            }
        }

        String weaponType = (weapon.getType() == Material.DIAMOND_SWORD) ? "sword" : "axe";

        double cost = 0;
        int enchantment = weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL);

        // Get the cost
        if (enchantment == 0) {
            cost = 5.00d;
        } else if (enchantment == 1) {
            cost = 40.00d;
        } else if (enchantment == 2) {
            cost = 100.00d;
        } else if (enchantment == 3) {
            cost = 255.00d;
        } else if (enchantment == 4) {
            _plugin.chatManager.sendMessage(player, "&bYour &e" + weaponType + "&b is already fully enchanted!");
            return;
        }

        if (!_plugin.controller.playerTransaction(player, cost)) {
            return;
        }

        // End message vars
        double endCost = 0;

        // Set the new data for the /enchants help menu
        if (enchantment == 0) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            endCost = 5.00d;
        } else if (enchantment == 1) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 2);
            endCost = 40.00d;
        } else if (enchantment == 2) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 3);
            endCost = 100.00d;
        } else if (enchantment == 3) {
            weapon.addEnchantment(Enchantment.DAMAGE_ALL, 4);
            endCost = 255.00d;
        }

        player.getInventory().setItemInHand(weapon);
        _plugin.controller.playerFinishTransaction(player, "Sharpness " + (enchantment + 1), endCost);
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void power(Player player)
    {
        ItemStack weapon = player.getInventory().getItemInHand();

        if (player.getInventory().getItemInHand() != null) {
            if (!(weapon.getType() == Material.BOW)) {
                _plugin.chatManager.sendMessage(player, "&cPlease hold a Bow in your hand to use this command");
                return;
            }
        }

        // Create temp variables
        double cost = 0;
        int enchantment = weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);

        // Get the cost
        if (enchantment == 0) {
            cost = 5.0;
        } else if (enchantment == 1) {
            cost = 40.0;
        } else if (enchantment == 2) {
            cost = 100.0;
        } else if (enchantment == 3) {
            cost = 255.0;
        } else if (enchantment == 4) {
            _plugin.chatManager.sendMessage(player, "&bYour &ebow&b is already fully enchanted!");
            return;
        }

        if (!_plugin.controller.playerTransaction(player, cost)) {
            return;
        }

        double endCost = 0;

        if (enchantment == 0) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
            endCost = 5.00d;
        } else if (enchantment == 1) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
            endCost = 40.00d;
        } else if (enchantment == 2) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
            endCost = 100.00d;
        } else if (enchantment == 3) {
            weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
            endCost = 255.00d;
        }

        player.getInventory().setItemInHand(weapon);
        _plugin.controller.playerFinishTransaction(player, "Power " + (enchantment + 1), cost);
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void knockback(Player player)
    {
        ItemStack weapon = player.getInventory().getItemInHand();

        if (player.getInventory().getItemInHand() != null) {
            if (!(weapon.getType() == Material.DIAMOND_SWORD || weapon.getType() == Material.DIAMOND_AXE)) {
                _plugin.chatManager.sendMessage(player, "&cPlease hold a Sword or Axe in your hand to use this command");
                return;
            }
        }

        String weaponType = (weapon.getType() == Material.DIAMOND_SWORD) ? "sword" : "axe";

        double cost = 0;
        int enchantment = weapon.getEnchantmentLevel(Enchantment.KNOCKBACK);

        if (enchantment == 0) {
            cost = 60.00d;
        } else if (enchantment == 1) {
            cost = 140.00d;
        } else if (enchantment == 2) {
            cost = 250.00d;
        } else if (enchantment == 3) {
            _plugin.chatManager.sendMessage(player, "&bYour &e" + weaponType + "&b is already fully enchanted!");
            return;
        }

        if (!_plugin.controller.playerTransaction(player, cost)) {
            return;
        }

        double endCost = 0;

        if (enchantment == 0) {
            weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
            endCost = 60.00d;
        } else if (enchantment == 1) {
            weapon.addEnchantment(Enchantment.KNOCKBACK, 2);
            endCost = 140.00d;
        } else if (enchantment == 2) {
            weapon.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
            endCost = 250.00d;
        }

        player.getInventory().setItemInHand(weapon);
        _plugin.controller.playerFinishTransaction(player, "Knockback " + (enchantment + 1), cost);
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void protection(Player player)
    {
        Material type = null;
        ItemStack item = null;

        if (player.getInventory().getHelmet() != null) {
            type = player.getInventory().getHelmet().getType();
            item = player.getInventory().getHelmet();
        } else if (player.getInventory().getChestplate() != null) {
            type = player.getInventory().getChestplate().getType();
            item = player.getInventory().getChestplate();
        } else if (player.getInventory().getLeggings() != null) {
            type = player.getInventory().getLeggings().getType();
            item = player.getInventory().getLeggings();
        } else if (player.getInventory().getBoots() != null) {
            type = player.getInventory().getBoots().getType();
            item = player.getInventory().getBoots();
        }

        if (type == null) {
            _plugin.chatManager.sendMessage(player, "&bYou don't have any armour on.. Here is some iron armor.");
            type = Material.IRON_HELMET;

            player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
            player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
            player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
            player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
        }

        // Check if the item is a sword
        if (type == Material.DIAMOND_HELMET || type == Material.DIAMOND_CHESTPLATE || type == Material.DIAMOND_LEGGINGS || type == Material.DIAMOND_BOOTS) {

            if (item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) == 1) {
                _plugin.chatManager.sendMessage(player, "&bYour &earmor&b is already fully enchanted");
                return;
            } else {
                if (!_plugin.controller.playerTransaction(player, 250.00d)) {
                    return;
                }

                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

                _plugin.controller.playerFinishTransaction(player, "Protection 1", 250.00d);

            }
        } else {
            int endCost = 0;
            double cost = 0;
            int enchantment = item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);

            if (enchantment == 0) {
                cost = 100.0;
            } else if (enchantment == 1) {
                cost = 250.0;
            } else if (enchantment == 2) {
                cost = 400.0;
            } else if (enchantment == 3) {
                _plugin.chatManager.sendMessage(player, "&bYour &earmor&b is already fully enchanted");
                return;
            }

            if (!_plugin.controller.playerTransaction(player, cost)) {
                return;
            }

            if (enchantment == 0) {
                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

                endCost = 100;
            } else if (enchantment == 1) {
                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

                endCost = 250;
            } else if (enchantment == 2) {
                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);

                endCost = 400;
            }
            _plugin.controller.playerFinishTransaction(player, "Protection " + (enchantment + 1), cost);
        }

        _plugin.controller.calPlayerWorth(player, true);
    }
}
