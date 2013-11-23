package net.Senither.Kits.engine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.Senither.Kits.Kits;
import net.Senither.Kits.ulits.YAMLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.TagAPI;

public class Controller
{

    private final Kits _plugin;
    // If a player is in this list they're not in pvp
    public List<String> pvpList = new ArrayList<String>();
    // People in this list is allowed to build
    public List<String> buildList = new ArrayList<String>();
    // People in this list will have hunger enabled
    public List<String> hungerList = new ArrayList<String>();

    public Controller(Kits plugin)
    {
        _plugin = plugin;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            public void run()
            {
                if (Bukkit.getOnlinePlayers().length != 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {

                        String key = player.getName();
                        String useName = player.getName();
                        if (_plugin.playerLog.get(key) != 0) {
                            _plugin.playerLog.put(key, (_plugin.playerLog.get(player.getName()) - 1));
                        }

                        if (key.length() > 14) {
                            useName = useName.substring(0, 12);
                        }

                        if (_plugin.controller.hasPvPEnabled(key)) {
                            if (_plugin.playerWorth.get(key) >= 22) {
                                player.setPlayerListName(ChatColor.RED + useName);
                            } else if (_plugin.playerWorth.get(key) <= 7) {
                                player.setPlayerListName(ChatColor.YELLOW + useName);
                            } else {
                                player.setPlayerListName(ChatColor.AQUA + useName);
                            }
                        } else {
                            player.setPlayerListName(ChatColor.GRAY + useName);
                        }
                    }
                }
            }
        }, 40, 20);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            public void run()
            {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (ItemStack item : player.getInventory().getArmorContents()) {
                        item.setDurability(Short.MIN_VALUE);
                    }
                    if (player.getInventory().getItem(0) != null) {
                        player.getInventory().getItem(0).setDurability(Short.MIN_VALUE);
                    }
                }
            }
        }, 300, 300);
    }

    public void loadPlayer(Player player)
    {
        if (!_plugin.playerConfig.containsKey(player.getName())) {
            _plugin.playerConfig.put(player.getName(), new YAMLManager(_plugin, player.getName() + ".yml"));
        }

        YAMLManager config = _plugin.playerConfig.get(player.getName());

        _plugin.playerKillstreak.put(player.getName(), 0);
        _plugin.playerEco.put(player.getName(), config.getConfig().getDouble("credits"));
        _plugin.playerDeaths.put(player.getName(), config.getConfig().getInt("deaths"));
        _plugin.playerKills.put(player.getName(), config.getConfig().getInt("kills"));
        _plugin.playerWorth.put(player.getName(), 10D);
        _plugin.playerLog.put(player.getName(), 0);

        player.setFoodLevel(20);
        player.setHealth(20D);
        player.setAllowFlight(false);
    }

    public void unloadPLayer(Player player)
    {
        YAMLManager config = _plugin.playerConfig.get(player.getName());

        config.getConfig().set("credits", _plugin.playerEco.get(player.getName()));
        config.getConfig().set("kills", _plugin.playerKills.get(player.getName()));
        config.getConfig().set("deaths", _plugin.playerDeaths.get(player.getName()));

        // This is only TEMP!
        // Set new values for later updates
        config.getConfig().set("lastlogin", 0);

        config.getConfig().set("achivements.killstreak.10", false);
        config.getConfig().set("achivements.killstreak.25", false);
        config.getConfig().set("achivements.killstreak.50", false);
        config.getConfig().set("achivements.killstreak.75", false);
        config.getConfig().set("achivements.killstreak.100", false);
        config.getConfig().set("achivements.enchants.sharpness4", false);
        config.getConfig().set("achivements.enchants.protection3", false);
        config.getConfig().set("achivements.armour.diamond", false);
        config.getConfig().set("achivements.armour.leather", false);
        config.getConfig().set("achivements.kit.ninja", false);
        config.getConfig().set("achivements.kit.medic", false);

        config.saveConfig();

        _plugin.playerConfig.remove(player.getName());
    }

    public void removePlayer(Player player)
    {
        _plugin.duel.removePlayer(player.getName());
        _plugin.playerKillstreak.remove(player.getName());
        _plugin.playerEco.remove(player.getName());
        _plugin.playerDeaths.remove(player.getName());
        _plugin.playerKills.remove(player.getName());
        _plugin.playerWorth.remove(player.getName());
        _plugin.slowList.remove(player.getName());
        _plugin.weakList.remove(player.getName());
        _plugin.playerLog.remove(player.getName());
        hungerList.remove(player.getName());
        buildList.remove(player.getName());
        pvpList.remove(player.getName());

        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }

        if (_plugin.vanish.isVanished(player)) {
            _plugin.vanish.showPlayer(player);
        }

    }

    public void resetPlayer(Player player, boolean completeReset)
    {
        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }

        if (completeReset) {
            Inventory i = player.getInventory();

            i.clear();

            i.addItem(new ItemStack(Material.DIAMOND_SWORD, 1));

            for (int s = 1; s <= 26; s++) {
                i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
            }

            player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
            player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
            player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
            player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));

            _plugin.playerKillstreak.put(player.getName(), 0);
            _plugin.playerWorth.put(player.getName(), 10D);

            if (!pvpList.contains(player.getName())) {
                pvpList.add(player.getName());
            }

            final String name = player.getName();
            Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
            {
                public void run()
                {
                    TagAPI.refreshPlayer(Bukkit.getPlayer(name));
                }
            }, 20);
        } else {
            _plugin.playerKillstreak.put(player.getName(), 0);
            if (!pvpList.contains(player.getName())) {
                pvpList.add(player.getName());
            }

            calPlayerWorth(player, true);
        }
    }

    public void calPlayerWorth(Player player, boolean updateTag)
    {

        if (canBuild(player.getName())) {
            return;
        }

        Inventory i = player.getInventory();
        double worth = 0;
        Material type = null;

        if (player.getInventory().getHelmet() != null) {
            type = player.getInventory().getHelmet().getType();
        } else if (player.getInventory().getChestplate() != null) {
            type = player.getInventory().getChestplate().getType();
        } else if (player.getInventory().getLeggings() != null) {
            type = player.getInventory().getLeggings().getType();
        } else if (player.getInventory().getBoots() != null) {
            type = player.getInventory().getBoots().getType();
        }

        if (type != null) {
            if (type == Material.DIAMOND_HELMET || type == Material.DIAMOND_CHESTPLATE || type == Material.DIAMOND_LEGGINGS || type == Material.DIAMOND_BOOTS) {
                worth = 15.5;
            } else if (type == Material.IRON_HELMET || type == Material.IRON_CHESTPLATE || type == Material.IRON_LEGGINGS || type == Material.IRON_BOOTS) {
                worth = 10.0;
            } else if (type == Material.GOLD_HELMET || type == Material.GOLD_CHESTPLATE || type == Material.GOLD_LEGGINGS || type == Material.GOLD_BOOTS) {
                worth = 8.2;
            } else if (type == Material.CHAINMAIL_HELMET || type == Material.CHAINMAIL_CHESTPLATE || type == Material.CHAINMAIL_LEGGINGS || type == Material.CHAINMAIL_BOOTS) {
                worth = 8.2;
            } else {
                worth = 7.5;
            }

            // Check enchants (Armor)
            int enchantmentArmor = player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);

            if (enchantmentArmor == 1) {
                worth = worth + 1.5;
            } else if (enchantmentArmor == 2) {
                worth = worth + 3.0;
            } else if (enchantmentArmor == 3) {
                worth = worth + 4.8;
            }

            // Check warpons + Enchants
            if (i.contains(Material.BOW)) {
                worth = worth - 1.0;

                // Check bow for enchants
                int enchantment = i.getItem(0).getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
                if (enchantment == 1) {
                    worth = worth + 1.0;
                } else if (enchantment == 2) {
                    worth = worth + 1.8;
                } else if (enchantment == 3) {
                    worth = worth + 2.8;
                } else if (enchantment == 4) {
                    worth = worth + 4.0;
                }
            } else {
                // Getting sword enchants
                int enchantmentSharpness = i.getItem(0).getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                int enchantmentKnockback = i.getItem(0).getEnchantmentLevel(Enchantment.KNOCKBACK);

                // Check sharpness
                if (enchantmentSharpness == 1) {
                    worth = worth + 1.0;
                } else if (enchantmentSharpness == 2) {
                    worth = worth + 1.8;
                } else if (enchantmentSharpness == 3) {
                    worth = worth + 2.8;
                } else if (enchantmentSharpness == 4) {
                    worth = worth + 4.0;
                }

                // Check knockback
                if (enchantmentKnockback == 1) {
                    worth = worth + 1.0;
                } else if (enchantmentKnockback == 2) {
                    worth = worth + 2.0;
                } else if (enchantmentKnockback == 3) {
                    worth = worth + 3.2;
                }
            }

            // Check buffs & debuffs

            // Buffs
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                worth = worth + 0.8;
            }
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                worth = worth + 2.5;
            }
            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                worth = worth + 2.2;
            }

            // Debuffs
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                worth = worth - 1.6;
            }
            if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                worth = worth - 1.5;
            }
            if (hasHungerEnabled(player.getName())) {
                worth = worth - 0.4;
            }

            // add the killstreak bonus
            worth += (_plugin.playerKillstreak.get(player.getName()) * 0.31);

            // Set the new player worth
            _plugin.playerWorth.put(player.getName(), worth);

            if (updateTag == true) {
                TagAPI.refreshPlayer(player);
            }
        } else {
            resetPlayer(player, true);
        }

    }

    public boolean playerTransaction(Player player, double amount)
    {
        double playerBalance = _plugin.playerEco.get(player.getName());

        if (playerBalance <= amount) {
            DecimalFormat df = new DecimalFormat("#.00");

            _plugin.chatManager.sendMessage(player, "&cThis would cost &e" + amount + " credits &cbut you only have &e" + df.format(playerBalance) + " credits&c!");
            return false;
        }

        _plugin.playerEco.put(player.getName(), (playerBalance - amount));

        return true;
    }

    public void playerFinishTransaction(Player player, String purchase, double costs)
    {
        _plugin.chatManager.sendMessage(player, "&bYour &e" + purchase + "&b has been delivered, at a cost of &e" + costs + " credits&b!");
    }

    public boolean hasPvPEnabled(String name)
    {
        return !pvpList.contains(name);
    }

    public boolean canBuild(String name)
    {
        return buildList.contains(name);
    }

    public boolean hasHungerEnabled(String name)
    {
        return hungerList.contains(name);
    }

    public void giveKillstreakReward(Player player)
    {
        Random random = new Random();

        int uid = random.nextInt(4);

        if (uid == 0) {
            // Refills the inventory with soup

            Inventory i = player.getInventory();

            i.remove(Material.MUSHROOM_SOUP);
            i.remove(Material.BOWL);

            for (int s = 1; s <= 27; s++) {
                i.addItem(new ItemStack(Material.MUSHROOM_SOUP, 1));
            }

            _plugin.chatManager.sendMessage(player, "&aYour inventory have been refilled with soup!");
        } else if (uid == 1) {
            // Gives the player 3 TNT Blocks

            Inventory i = player.getInventory();

            i.addItem(new ItemStack(Material.TNT, 1));
            i.addItem(new ItemStack(Material.TNT, 1));
            i.addItem(new ItemStack(Material.TNT, 1));

            _plugin.chatManager.sendMessage(player, "&aYou got 3 TNT Blocks for your awesome killstreak!");
        } else if (uid == 2) {
            // Vanish the player for three seconds
            _plugin.vanish.vanishPlayer(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));

            _plugin.chatManager.sendMessage(player, "&aYou have vanished for 5 seconds!");

            final String name = player.getName();
            Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
            {
                public void run()
                {
                    _plugin.vanish.showPlayer(Bukkit.getPlayer(name));
                }
            }, 100);
        } else if (uid == 3) {
            // Give the player regen
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));

            _plugin.chatManager.sendMessage(player, "&aYou just got extra regen! How luck are you!?");
        }
    }
}
