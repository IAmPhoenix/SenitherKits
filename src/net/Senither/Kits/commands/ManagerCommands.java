package net.Senither.Kits.commands;

import java.util.ArrayList;
import java.util.List;
import net.Senither.Kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class ManagerCommands implements CommandExecutor
{

    private final Kits _plugin;

    public ManagerCommands(Kits plugin)
    {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args)
    {
        Player player = null;

        if (commandLable.equalsIgnoreCase("addcredits")) {
            if (sender instanceof Player) {
                player = (Player) sender;

                if (player.hasPermission(_plugin.permissions.MANAGE_ADDCREDITS)) {
                    addCreditsToPlayer(sender, args);
                }
            } else {
                addCreditsToPlayer(sender, args);
            }
            return false;
        }



        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            _plugin.chatManager.LogInfo("You can't use that command from the console!");
            return false;
        }

        if (commandLable.equalsIgnoreCase("build")) {
            buildToggle(player);
        } else if (commandLable.equalsIgnoreCase("help")) {
            helpMenu(player);
        } else if (commandLable.equalsIgnoreCase("guide")) {
            guideMenu(player, args);
        } else if (commandLable.equalsIgnoreCase("spectate")) {
            spectateToggle(player);
        } else if (commandLable.equalsIgnoreCase("duel")) {
            duelPlayer(player, args);
        } else if (commandLable.equalsIgnoreCase("stats")) {
            stats(player, args);
        } else if (commandLable.equalsIgnoreCase("modify")) {
            modify(player, args);
        }

        return false;
    }

    private void addCreditsToPlayer(CommandSender player, String[] args)
    {
        if (args.length == 2) {
            Player target = (Player) Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(args[0] + " is offline!");
                player.sendMessage("Players need to be online for you to be able to add tokens to them!");
                return;
            }

            double add = 0;

            try {
                add = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("Please enter a number next time..");
                return;
            }

            if (add != 0d) {

                _plugin.playerEco.put(target.getName(), (_plugin.playerEco.get(target.getName()) + add));

                player.sendMessage("Added " + add + " credits to " + target.getName() + "'s account!");
                return;
            } else {
                player.sendMessage("You can't add nothing at all..");
                return;
            }
        } else {
            player.sendMessage("Invalid format!");
            player.sendMessage("/addcredits <player> <credits>");
        }
    }

    private void buildToggle(Player player)
    {
        if (player.hasPermission(_plugin.permissions.MANAGE_BUILD)) {

            String name = player.getName();

            if (_plugin.controller.canBuild(name)) {
                _plugin.controller.buildList.remove(name);
                _plugin.chatManager.sendMessage(player, "Building mode have been turned off");
                _plugin.chatManager.sendMessage(player, "You can no longer destory blocks");
            } else {
                _plugin.controller.buildList.add(name);
                _plugin.chatManager.sendMessage(player, "Building mode have been turned on");
                _plugin.chatManager.sendMessage(player, "You can now destory blocks");
            }
        } else {
            _plugin.chatManager.missingPermission(player, _plugin.permissions.MANAGE_BUILD);
        }
    }

    private void helpMenu(Player player)
    {
        // Send help menu
        // Format: /command: Information
        _plugin.chatManager.sendMessage(player, "&e -------- &6Help Menu&e --------");
        _plugin.chatManager.sendMessage(player, "&6/enchants&f: List of Enchants");
        _plugin.chatManager.sendMessage(player, "&6/buffs&f: List of Buffs");
        _plugin.chatManager.sendMessage(player, "&6/debuffs&f: List of Debuffs");
        _plugin.chatManager.sendMessage(player, "&6/armor&f: List of Armor types");
        _plugin.chatManager.sendMessage(player, "&6/weapons&f: List of Weapons");
        _plugin.chatManager.sendMessage(player, "&6/Kits&f: List of Kits");
        _plugin.chatManager.sendMessage(player, "&6/extra&f: List of Extra Suff");
        _plugin.chatManager.sendMessage(player, "&6/duel <name>&f: Duel another player");
        _plugin.chatManager.sendMessage(player, "&6/spectate&f: Goes into spectate mode");
    }

    private void guideMenu(Player p, String[] args)
    {
        // Check if the user uses any arguments
        if (args.length == 0) {
            // Send help menu
            // Format: /command [page]: Infomation
            p.sendMessage(ChatColor.YELLOW + " ---------- " + ChatColor.GOLD + "Guide" + ChatColor.YELLOW + " ----------");
            p.sendMessage(ChatColor.WHITE + "Use /guide [id] for more information");
            p.sendMessage(ChatColor.GOLD + "1" + ChatColor.WHITE + ": How do I enchant my items?");
            p.sendMessage(ChatColor.GOLD + "2" + ChatColor.WHITE + ": How can I change my weapon?");
            p.sendMessage(ChatColor.GOLD + "3" + ChatColor.WHITE + ": Why use debuffs?");
            p.sendMessage(ChatColor.GOLD + "4" + ChatColor.WHITE + ": How do I get more soup?");
            p.sendMessage(ChatColor.GOLD + "5" + ChatColor.WHITE + ": When does the map change and why?");
            p.sendMessage(ChatColor.GOLD + "6" + ChatColor.WHITE + ": Why can't I hit people?");
            p.sendMessage(ChatColor.GOLD + "7" + ChatColor.WHITE + ": What does the colors above people's head mean?");
            p.sendMessage(ChatColor.GOLD + "8" + ChatColor.WHITE + ": What is booster blocks?");
        } else {
            if (args[0].equalsIgnoreCase("1")) { // How do I enchant my items?
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " You can use the command " + ChatColor.GOLD + "/enchants" + ChatColor.GRAY + " to see the list of enchants you can get, some enchants you can upgrade more then once, however it will cost more per upgrade. All enchants will be lost upon death so watch out!");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else if (args[0].equalsIgnoreCase("2")) { // How can I change my weapon?
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " You can use the command " + ChatColor.GOLD + "/weapons" + ChatColor.GRAY + " to see the list of weapons you can get, for quick access use " + ChatColor.GOLD + "/sword" + ChatColor.GRAY + " to get a sword or use " + ChatColor.GOLD + "/bow" + ChatColor.GRAY + " to get a bow.");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else if (args[0].equalsIgnoreCase("3")) { // Why use debuffs?
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " Debuffs gives you a bigger challenge when pvping others while it make you worth less to everyone else it also allows you to get more credits per kill.");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else if (args[0].equalsIgnoreCase("4")) { // How do I get more soup?
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " You can use " + ChatColor.GOLD + "/soup" + ChatColor.GRAY + " to get a ton of soup, however watch out as it cost six credits and will blind you for four seconds!");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else if (args[0].equalsIgnoreCase("5")) { // When does the map change and why?
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " The map changes from time to time to keep everything intersting and new. You can check how long thats left before the map changes again by using the command " + ChatColor.GOLD + "/map");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else if (args[0].equalsIgnoreCase("6")) { // Why can't I hit people?
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " You can't hit people with " + ChatColor.WHITE + "white" + ChatColor.GRAY + " name plates as they have pvp disabled. You must wait for them to enable it by either hitting someone with pvp enabled or cross the pvp line near spawn.");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else if (args[0].equalsIgnoreCase("7")) { // What does the colors above peoples head mean?
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " The colors indicate if a player has pvp enabled and how much they're worth");
                p.sendMessage("");
                p.sendMessage(ChatColor.WHITE + " White " + ChatColor.GRAY + ": PvP is Disabled, you can't hit people with pvp disabled.");
                p.sendMessage(ChatColor.YELLOW + " Yellow " + ChatColor.GRAY + ": PvP is Enabled and worth between 0 - 7 credits.");
                p.sendMessage(ChatColor.AQUA + " Blue " + ChatColor.GRAY + ": PvP is Enabled and worth between 7 - 22 credits.");
                p.sendMessage(ChatColor.RED + " Red " + ChatColor.GRAY + ": PvP is Enabled and worth between 22 - ?? credits.");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else if (args[0].equalsIgnoreCase("8")) { // Booster blocks
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
                p.sendMessage(ChatColor.GRAY + " Booster blocks is blocks that give you some kind of effect when you run over them, the colors of the blocks indicate what type of effect you would get");
                p.sendMessage("");
                p.sendMessage(ChatColor.RED + " Red " + ChatColor.GRAY + ": Enables pvp when you touch it");
                p.sendMessage(ChatColor.YELLOW + " Yellow " + ChatColor.GRAY + ": Give you a jump boost. 30 seconds cooldown");
                p.sendMessage(ChatColor.GREEN + " Green " + ChatColor.GRAY + ": Give you a speed boost. 1.5 minute cooldown");
                p.sendMessage(ChatColor.BLACK + " Black " + ChatColor.GRAY + ": Traps you so you can't move. 2.5 minutes cooldown");
                p.sendMessage(ChatColor.DARK_GRAY + " Gray " + ChatColor.GRAY + ": Give you a wither effect. 1.5 minute cooldown");
                p.sendMessage(ChatColor.LIGHT_PURPLE + " Pink " + ChatColor.GRAY + ": Sets you on fire. 30 seconds cooldown");
                p.sendMessage(ChatColor.DARK_PURPLE + " Magenta " + ChatColor.GRAY + ": Give you a nausea effect. 2 minutes cooldown");
                p.sendMessage(ChatColor.DARK_GREEN + " Dark Green " + ChatColor.GRAY + ": Makes you go BOOM. 30 seconds cooldown");
                p.sendMessage(ChatColor.BLUE + " Blue " + ChatColor.GRAY + ": TP's you up and down. No cooldown.");
                p.sendMessage(ChatColor.YELLOW + " ---------------------------------------------------- ");
            } else {
                p.sendMessage(ChatColor.RED + "Couldn't find the guide you were looking for");
                p.sendMessage(ChatColor.RED + "Please use /guide for a list of guides you can use");
            }
        }
    }

    private void spectateToggle(Player player)
    {
        _plugin.chatManager.sendMessage(player, " &eThis feature has not yet been implemented!");
    }

    private void duelPlayer(Player player, String[] args)
    {
        if (args.length != 1) {
            _plugin.chatManager.sendMessage(player, "&cInvailed format!");
            _plugin.chatManager.sendMessage(player, "&c/<command> <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            _plugin.chatManager.sendMessage(player, "&c" + args[0] + " is not online!");
            return;
        }
        
        if(player.getName().equals(target.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't duel yourself!");
            return;
        }
        
        if(_plugin.duel.isPlayerInBattle(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou can't challange people to a duel when you're in a battle!");
            return;
        }
        
        if(_plugin.duel.isPlayerInQueue(player.getName())) {
            _plugin.chatManager.sendMessage(player, "&cYou're alrady in a duel queue!");
            return;
        }
        
        _plugin.duel.challangePlayer(player, target);
    }

    private void stats(Player player, String[] args)
    {
        if (args.length == 0) {
            // This player
            _plugin.chatManager.sendMessage(player, "&bOi mate.. Just look at your scoreboard -->");
            _plugin.chatManager.sendMessage(player, "&bWant to see player stats? use /stats <player>");
        } else {
            // Another player
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                _plugin.chatManager.sendMessage(player, "&c" + args[0] + " is not online!");
                return;
            }

            _plugin.chatManager.sendMessage(player, "&3Loading player stats for &b" + target.getName() + "&3!");

            // Create the custom Inventory
            Inventory inv = Bukkit.createInventory(null, 54, "Player Stats");

            ItemStack hotbarInv = new ItemStack(Material.ITEM_FRAME, 1);
            ItemMeta hotbarInvMeta = hotbarInv.getItemMeta();
            hotbarInvMeta.setDisplayName(ChatColor.RED + target.getName() + "'s hotbar [v]");
            hotbarInv.setItemMeta(hotbarInvMeta);

            ItemStack hotbarArm = new ItemStack(Material.ITEM_FRAME, 1);
            ItemMeta hotbarArmMeta = hotbarArm.getItemMeta();
            hotbarArmMeta.setDisplayName(ChatColor.RED + target.getName() + "'s armour [>]");
            hotbarArm.setItemMeta(hotbarArmMeta);
            hotbarArmMeta.setDisplayName(ChatColor.RED + target.getName() + "'s armour [<]");

            for (int i = 0; i < 9; i++) {
                if (target.getInventory().getItem(i) != null) {
                    inv.setItem((i + 45), target.getInventory().getItem(i));
                }
            }

            int cal = 38;
            for (ItemStack item : target.getInventory().getArmorContents()) {
                if (item != null) {
                    inv.setItem(cal, item);
                }
                if (cal == 39) {
                    cal++;
                }
                cal++;
            }

            inv.setItem(36, hotbarInv);
            inv.setItem(37, hotbarArm);

            hotbarArm.setItemMeta(hotbarArmMeta);

            inv.setItem(43, hotbarArm);
            inv.setItem(44, hotbarInv);

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1);
            ItemMeta headMeta = head.getItemMeta();
            headMeta.setDisplayName(ChatColor.GOLD + target.getName() + "'s stats!");
            head.setItemMeta(headMeta);
            head.setDurability((short) 3);
            inv.setItem(22, head);

            ItemStack buffs = new ItemStack(Material.POTION, 1);
            ItemMeta buffsMeta = buffs.getItemMeta();
            List<String> buffsList = new ArrayList<String>();

            ItemStack debuffs = new ItemStack(Material.POTION, 1);
            ItemMeta debuffsMeta = debuffs.getItemMeta();
            List<String> debuffsList = new ArrayList<String>();

            // Finish the buffs + debuffs and make it so people can see other peoples cash. :P

            if (!target.isDead()) {
                ItemStack health = new ItemStack(Material.APPLE, (int) target.getHealth());
                ItemMeta healthMeta = health.getItemMeta();
                healthMeta.setDisplayName(ChatColor.GOLD + "Health Level");
                health.setItemMeta(healthMeta);

                ItemStack food = new ItemStack(Material.PORK, target.getFoodLevel());
                ItemMeta foodMeta = food.getItemMeta();
                foodMeta.setDisplayName(ChatColor.GOLD + "Food Level");
                food.setItemMeta(foodMeta);

                inv.setItem(7, health);
                inv.setItem(8, food);

                // Handle buffs
                buffsList.add(ChatColor.GRAY + "Speed : " + ((target.hasPotionEffect(PotionEffectType.SPEED)) ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
                buffsList.add(ChatColor.GRAY + "Strength : " + ((target.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
                buffsList.add(ChatColor.GRAY + "Regen : " + ((target.hasPotionEffect(PotionEffectType.REGENERATION)) ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));

                debuffsList.add(ChatColor.GRAY + "Hunger : " + ((_plugin.controller.hungerList.contains(target.getName())) ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
                debuffsList.add(ChatColor.GRAY + "Slowness : " + ((target.hasPotionEffect(PotionEffectType.SLOW)) ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
                debuffsList.add(ChatColor.GRAY + "Weakness : " + ((target.hasPotionEffect(PotionEffectType.WEAKNESS)) ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
            } else {
                // IF the player is dead there are no reason to check his/her buffs
                buffsList.add(ChatColor.GRAY + "Speed : " + ChatColor.RED + "off");
                buffsList.add(ChatColor.GRAY + "Strength : " + ChatColor.RED + "off");
                buffsList.add(ChatColor.GRAY + "Regen : " + ChatColor.RED + "off");

                debuffsList.add(ChatColor.GRAY + "Hunger : " + ChatColor.RED + "off");
                debuffsList.add(ChatColor.GRAY + "Slowness : " + ChatColor.RED + "off");
                debuffsList.add(ChatColor.GRAY + "Weakness : " + ChatColor.RED + "off");
            }

            // Set lore
            buffsMeta.setLore(buffsList);
            buffsMeta.setDisplayName(ChatColor.RED + "Buffs");
            buffs.setItemMeta(buffsMeta);

            debuffsMeta.setLore(debuffsList);
            debuffsMeta.setDisplayName(ChatColor.RED + "Debuffs");
            debuffs.setItemMeta(debuffsMeta);

            inv.setItem(2, buffs);
            inv.setItem(3, debuffs);

            ItemStack credits = new ItemStack(Material.GOLD_INGOT, 1);
            ItemMeta creditsMeta = credits.getItemMeta();
            creditsMeta.setDisplayName(ChatColor.GOLD + "Credits");
            List<String> creditsList = new ArrayList<String>();
            creditsList.add(ChatColor.GRAY + "Credits : " + ChatColor.RED + _plugin.playerEco.get(target.getName()));
            creditsMeta.setLore(creditsList);
            credits.setItemMeta(creditsMeta);
            inv.setItem(5, credits);


            ItemStack stats = new ItemStack(Material.DIAMOND_SWORD, 1);
            ItemMeta statsMeta = stats.getItemMeta();
            List<String> statsLore = new ArrayList<String>();

            statsLore.add(ChatColor.GRAY + "PvP is " + ((_plugin.controller.hasPvPEnabled(target.getName())) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            statsLore.add(""); // Blank line, just a filler

            int kills = _plugin.playerKills.get(target.getName());
            int deaths = _plugin.playerDeaths.get(target.getName());
            int kdRatio = kills;
            if (kills != 0 && deaths != 0) {
                kdRatio = (int) Math.ceil(kills / deaths);
            }

            statsLore.add(ChatColor.GRAY + "Kills : " + ChatColor.RED + kills);
            statsLore.add(ChatColor.GRAY + "Deaths : " + ChatColor.RED + deaths);
            statsLore.add(ChatColor.GRAY + "K/D Ratio : " + ChatColor.RED + kdRatio);
            statsLore.add(ChatColor.GRAY + "PvP Score : " + ChatColor.RED + (kills - deaths));
            statsLore.add(ChatColor.GRAY + "Killstreak : " + ChatColor.RED + _plugin.playerKillstreak.get(target.getName()));
            statsLore.add(""); // Another blank line

            statsLore.add(ChatColor.GRAY + "Player Worth : " + ChatColor.RED + (_plugin.playerWorth.get(target.getName())));
            
            statsLore.add(""); // Another blank line
            
            statsLore.add(ChatColor.GRAY + "Duel Score : " + ChatColor.RED + _plugin.playerDuelScore.get(target.getName()));

            statsMeta.setLore(statsLore);
            statsMeta.setDisplayName(ChatColor.RED + "Player Stats");

            stats.setItemMeta(statsMeta);

            inv.setItem(0, stats);

            /*
             _plugin.chatManager.sendMessage(player, "&b" + target.getName() + " is currently " + ((_plugin.controller.hasPvPEnabled(player.getName())) ? "" : "&cnot&b ") + "in combat and is worth " + playerWorth + ".");
             _plugin.chatManager.sendMessage(player, "&6Kills &f: " + kills);
             _plugin.chatManager.sendMessage(player, "&6Deaths &f: " + deaths);
             _plugin.chatManager.sendMessage(player, "&6K/D Ratio &f: " + kdRatio);
             _plugin.chatManager.sendMessage(player, "&6PvP Score &f: " + (kills - deaths));
             _plugin.chatManager.sendMessage(player, "&6Killstreak &f: " + killstreak);
             */

            player.openInventory(inv);
        }
    }

    private void modify(Player player, String[] args)
    {
        if (!player.hasPermission(_plugin.permissions.MANAGE_MODIFY)) {
            _plugin.chatManager.missingPermission(player, _plugin.permissions.MANAGE_MODIFY);
            return;
        }

        if (args.length != 3) {
            _plugin.chatManager.sendMessage(player, "Virables you can edit.");
            _plugin.chatManager.sendMessage(player, "kills&f: A players kills");
            _plugin.chatManager.sendMessage(player, "deaths&f: A players kills");
            _plugin.chatManager.sendMessage(player, "killstreak&f: A players kills");
            _plugin.chatManager.sendMessage(player, "duel&f: A players duel score");
            _plugin.chatManager.sendMessage(player, "Format:&f /modify <player> <virable> <value>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            _plugin.chatManager.sendMessage(player, "&c" + args[0] + " is offline!");
            return;
        }

        int value = 0;

        try {
            value = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            _plugin.chatManager.sendMessage(player, "&cInvalid value.");
            return;
        }

        if (value == 0) {
            _plugin.chatManager.sendMessage(player, "&cPlease enter a value other than 0");
            return;
        }

        if (args[1].equalsIgnoreCase("kills")) {
            _plugin.playerKills.put(target.getName(), value);
        } else if (args[1].equalsIgnoreCase("deaths")) {
            _plugin.playerDeaths.put(target.getName(), value);
        } else if (args[1].equalsIgnoreCase("killstreak")) {
            _plugin.playerKillstreak.put(target.getName(), value);
            _plugin.controller.calPlayerWorth(player, true);
        } else if (args[1].equalsIgnoreCase("duel")) {
            _plugin.playerDuelScore.put(target.getName(), value);
        } else {
            _plugin.chatManager.sendMessage(player, "&cInvalid format!");
            _plugin.chatManager.sendMessage(player, "Format:&f /modify <player> <virable> <value>");
            return;
        }

        _plugin.chatManager.sendMessage(player, "&aUpdated " + target.getName() + "'s " + args[1] + " to " + value);
    }
}
