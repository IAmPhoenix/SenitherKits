package net.Senither.Kits.commands;

import net.Senither.Kits.Kits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuffCommands implements CommandExecutor
{

    private final Kits _plugin;

    public BuffCommands(Kits plugin)
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

        if (commandLable.equalsIgnoreCase("buffs")) {
            buffMenu(player);
            return true;
        } 
        
        if(_plugin.playerUsingKits.contains(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't use this command while wearing a special kit a kit!");
            return true;
        }
        
        if (commandLable.equalsIgnoreCase("debuffs")) {
            debuffMenu(player);
        } else if (commandLable.equalsIgnoreCase("speed")) {
            speed(player);
        } else if (commandLable.equalsIgnoreCase("strength")) {
            strength(player);
        } else if (commandLable.equalsIgnoreCase("regen")) {
            regen(player);
        } else if (commandLable.equalsIgnoreCase("hunger")) {
            hunger(player);
        } else if (commandLable.equalsIgnoreCase("slow")) {
            slowness(player);
        } else if (commandLable.equalsIgnoreCase("weak")) {
            weakness(player);
        }

        return false;
    }

    private void buffMenu(Player player)
    {
        // Send help menu
        // Format: /command: Information

        _plugin.chatManager.sendMessage(player, "&e -------- &6Buff Menu &e--------");
        _plugin.chatManager.sendMessage(player, "&6/speed&f: Price: 150 Credits - Last for: 8 minutes");
        _plugin.chatManager.sendMessage(player, "&6/strength&f: Price: 200 Credits - Last for: 5 minutes");
        _plugin.chatManager.sendMessage(player, "&6/regen&f: Price: 250 Credits - Last for: 5 minutes");
        _plugin.chatManager.sendMessage(player, "");
        _plugin.chatManager.sendMessage(player, "&3Make yourself powerful but get less money for your kills!");
    }

    private void debuffMenu(Player player)
    {
        // Send help menu
        // Format: /command: Information

        _plugin.chatManager.sendMessage(player, "&e -------- &6Buff Menu &e--------");
        _plugin.chatManager.sendMessage(player, "&6/hunger&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "&6/slow&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "&6/weak&f: Price: Free");
        _plugin.chatManager.sendMessage(player, "");
        _plugin.chatManager.sendMessage(player, "&3Add a litte more challange with de-buffs!");
        _plugin.chatManager.sendMessage(player, "&3And get more money for your kills!");
    }

    private void speed(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 150.00d)) {
            return;
        }

        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9600, 0));

        _plugin.controller.playerFinishTransaction(player, "Speed Boost", 150.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void strength(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 200.00d)) {
            return;
        }

        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 0));

        _plugin.controller.playerFinishTransaction(player, "Damage Boost", 200.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void regen(Player player)
    {
        if (!_plugin.controller.playerTransaction(player, 250.00d)) {
            return;
        }

        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6000, 0));

        _plugin.controller.playerFinishTransaction(player, "Regen Boost", 250.00d);
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void hunger(Player player)
    {
        if (_plugin.controller.hasHungerEnabled(player.getName())) {
            _plugin.controller.hungerList.remove(player.getName());
            _plugin.chatManager.sendMessage(player, "Hunger has been disabled");
        } else {
            _plugin.controller.hungerList.add(player.getName());
            _plugin.chatManager.sendMessage(player, "Hunger has been enabled");
            _plugin.chatManager.sendMessage(player, "Use /hunger again to disable it");
        }
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void slowness(Player player)
    {
        if (_plugin.slowList.contains(player.getName())) {
            _plugin.slowList.remove(player.getName());
            _plugin.chatManager.sendMessage(player, "Slowness has been disabled");
            _plugin.chatManager.sendMessage(player, "Please wait 6 seconds for it to disappear");
            player.removePotionEffect(PotionEffectType.SLOW);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 130, 1));
        } else {
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                _plugin.chatManager.sendMessage(player, "&cPlease wait for the slowness to run out before enabling it again");
                return;
            }

            _plugin.slowList.add(player.getName());
            _plugin.chatManager.sendMessage(player, "Slowness has been enabled");
            _plugin.chatManager.sendMessage(player, "Use /slow again to disable it");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 1));
        }
        _plugin.controller.calPlayerWorth(player, true);
    }

    public void weakness(Player player)
    {
        if (_plugin.weakList.contains(player.getName())) {
            _plugin.weakList.remove(player.getName());
            _plugin.chatManager.sendMessage(player, "Weakness has been disabled");
            _plugin.chatManager.sendMessage(player, "Please wait 6 seconds for it to disappear");
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 130, 1));
        } else {
            if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                _plugin.chatManager.sendMessage(player, "&cPlease wait for the Weakness to run out before enabling it again");
                return;
            }

            _plugin.weakList.add(player.getName());
            _plugin.chatManager.sendMessage(player, "Weakness has been enabled");
            _plugin.chatManager.sendMessage(player, "Use /weak again to disable it");
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10000000, 1));
        }
        _plugin.controller.calPlayerWorth(player, true);
    }
}
