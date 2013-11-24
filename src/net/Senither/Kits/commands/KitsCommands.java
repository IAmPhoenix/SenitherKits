package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class KitsCommands implements CommandExecutor
{

    private final Kits _plugin;

    public KitsCommands(Kits plugin)
    {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't execute that command from the console!");
            return true;
        }

        Player player = (Player) sender;

        if (commandLable.equalsIgnoreCase("kits") || commandLable.equalsIgnoreCase("kit")) {
            _plugin.chatManager.sendMessage(player, "&e -------- &6Weapon Menu&e --------");
            _plugin.chatManager.sendMessage(player, "&6/Knight&f: Price: 10 Credits");
            _plugin.chatManager.sendMessage(player, "&6/Archer&f: Price: 10 Credits");
            _plugin.chatManager.sendMessage(player, "&6/Medic&f: Price: 100 Credits");
            _plugin.chatManager.sendMessage(player, "&6/Mage&f: Price: 200 Credits");
            _plugin.chatManager.sendMessage(player, "&6/Ninja&f: Price: 300 Credits");
            return true;
        }

        if (_plugin.duel.isPlayerInBattle(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't use this command while in a duel!");
            return true;
        }

        if (commandLable.equalsIgnoreCase("Knight")) {
            knight(player);
        } else if (commandLable.equalsIgnoreCase("Archer")) {
            archer(player);
        } else if (commandLable.equalsIgnoreCase("Medic")) {
            medic(player);
        } else if (commandLable.equalsIgnoreCase("Mage")) {
            mage(player);
        } else if (commandLable.equalsIgnoreCase("Ninja")) {
            ninja(player);
        }

        return true;
    }

    private void knight(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 10.00d)) {
            return;
        }

        player.getInventory().clear();

        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));

        Inventory i = player.getInventory();

        i.setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));

        for (int s = 1; s <= 27; s++) {
            i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
        }

        _plugin.controller.playerFinishTransaction(player, "Knight Kit", 10.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void archer(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 10.00d)) {
            return;
        }

        player.getInventory().clear();

        ItemStack healmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemStack leggins = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);

        healmet.addUnsafeEnchantment(Enchantment.THORNS, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        boots.addUnsafeEnchantment(Enchantment.THORNS, 1);

        player.getInventory().setHelmet(healmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);

        Inventory i = player.getInventory();

        ItemStack weapon = new ItemStack(Material.BOW, 1);
        weapon.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        i.addItem(weapon);

        for (int s = 1; s <= 27; s++) {
            i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
        }

        i.addItem(new ItemStack(Material.ARROW, 1));

        _plugin.controller.playerFinishTransaction(player, "Archer Kit", 10.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void medic(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 100.00d)) {
            return;
        }

        if (!_plugin.playerUsingKits.contains(player.getName())) {
            _plugin.playerUsingKits.add(player.getName());
        }

        player.getInventory().clear();

        player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));

        Inventory i = player.getInventory();

        ItemStack weapon = new ItemStack(Material.GOLD_SWORD, 1);
        weapon.addEnchantment(Enchantment.KNOCKBACK, 1);

        i.addItem(weapon);

        i.addItem(new ItemStack(Material.POTION, 6, (short) 16453)); // Health I
        i.addItem(new ItemStack(Material.POTION, 6, (short) 16385)); // Rege I
        i.addItem(new ItemStack(Material.POTION, 3, (short) 16421)); // Health II
        i.addItem(new ItemStack(Material.POTION, 3, (short) 16417)); // Regen II

        for (int s = 1; s <= 27; s++) {
            i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
        }

        _plugin.controller.playerFinishTransaction(player, "Medic Kit", 100.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void mage(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 200.00d)) {
            return;
        }

        if (!_plugin.playerUsingKits.contains(player.getName())) {
            _plugin.playerUsingKits.add(player.getName());
        }

        player.getInventory().clear();

        ItemStack healmet = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);

        healmet.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);

        player.getInventory().setHelmet(healmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);

        Inventory i = player.getInventory();

        ItemStack weapon = new ItemStack(Material.GOLD_SWORD, 1);
        weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
        weapon.addEnchantment(Enchantment.FIRE_ASPECT, 1);
        weapon.addEnchantment(Enchantment.DURABILITY, 2);

        i.addItem(weapon);

        i.addItem(new ItemStack(Material.POTION, 4, (short) 8225)); // Regen II
        i.addItem(new ItemStack(Material.POTION, 4, (short) 8261)); // Health I
        i.addItem(new ItemStack(Material.POTION, 2, (short) 8226)); // Speed II
        i.addItem(new ItemStack(Material.POTION, 2, (short) 8227)); // Fire Res I
        i.addItem(new ItemStack(Material.POTION, 2, (short) 16388)); // Splash Posion I
        i.addItem(new ItemStack(Material.POTION, 2, (short) 16426)); // Slowness I
        i.addItem(new ItemStack(Material.POTION, 1, (short) 8238)); // Potion of Invs I

        for (int s = 1; s <= 27; s++) {
            i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
        }

        _plugin.controller.playerFinishTransaction(player, "Mage Kit", 200.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    private void ninja(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 300.00d)) {
            return;
        }

        if (!_plugin.playerUsingKits.contains(player.getName())) {
            _plugin.playerUsingKits.add(player.getName());
        }

        player.getInventory().clear();

        ItemStack healmet = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);

        healmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        healmet.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggins.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);

        LeatherArmorMeta healmetMeta = (LeatherArmorMeta) healmet.getItemMeta();
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        LeatherArmorMeta legginsMeta = (LeatherArmorMeta) leggins.getItemMeta();
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

        healmetMeta.setColor(Color.BLACK);
        chestplateMeta.setColor(Color.BLACK);
        legginsMeta.setColor(Color.BLACK);
        bootsMeta.setColor(Color.BLACK);

        healmet.setItemMeta(healmetMeta);
        chestplate.setItemMeta(chestplateMeta);
        leggins.setItemMeta(legginsMeta);
        boots.setItemMeta(bootsMeta);

        player.getInventory().setHelmet(healmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);

        Inventory i = player.getInventory();

        ItemStack weapon = new ItemStack(Material.DIAMOND_SWORD, 1);
        weapon.addEnchantment(Enchantment.DAMAGE_ALL, 2);

        i.addItem(weapon);

        for (int s = 1; s <= 27; s++) {
            i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
        }

        _plugin.controller.playerFinishTransaction(player, "Ninja Kit", 300.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }
}
