package net.Senither.Kits;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.Senither.Kits.Events.BlockListener;
import net.Senither.Kits.Events.BoosterBlockListener;
import net.Senither.Kits.Events.ChatListener;
import net.Senither.Kits.Events.PlayerListener;
import net.Senither.Kits.Events.ServerListener;
import net.Senither.Kits.commands.ArmourCommands;
import net.Senither.Kits.commands.BuffCommands;
import net.Senither.Kits.commands.EnchantCommands;
import net.Senither.Kits.commands.ExtraCommands;
import net.Senither.Kits.commands.ManagerCommands;
import net.Senither.Kits.commands.WeaponCommands;
import net.Senither.Kits.engine.Controller;
import net.Senither.Kits.engine.DuelHandler;
import net.Senither.Kits.engine.MapHandler;
import net.Senither.Kits.engine.ScoreboardManager;
import net.Senither.Kits.ulits.ChatManager;
import net.Senither.Kits.ulits.Permissions;
import net.Senither.Kits.ulits.YAMLManager;
import net.Senither.Kits.vanish.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Kits extends JavaPlugin
{

    // Version number for the plugin
    public String v;
    /*
     * Class handlers
     */
    public ChatManager chatManager;
    public Controller controller;
    public MapHandler mapHandler;
    public ScoreboardManager scoreboard;
    public Permissions permissions;
    public VanishHandler vanish;
    public DuelHandler duel;
    /*
     * Config handler
     */
    public HashMap<String, YAMLManager> playerConfig = new HashMap<String, YAMLManager>();
    /*
     * Plugin wide variables
     */
    public HashMap<String, Double> playerEco = new HashMap<String, Double>();
    public HashMap<String, Double> playerWorth = new HashMap<String, Double>();
    public HashMap<String, Integer> playerKillstreak = new HashMap<String, Integer>();
    public HashMap<String, Integer> playerKills = new HashMap<String, Integer>();
    public HashMap<String, Integer> playerDeaths = new HashMap<String, Integer>();
    public HashMap<String, Integer> playerLog = new HashMap<String, Integer>();
    // Players in these lists will have the debuff enabled
    public List<String> slowList = new ArrayList<String>();
    public List<String> weakList = new ArrayList<String>();

    @Override
    public void onEnable()
    {
        // Save default config
        saveDefaultConfig();

        // Creates the "playerData" folder
        File playerRoot = new File("plugins" + File.separator + "Kits" + File.separator + "playerData");
        playerRoot.mkdir();

        // Creates class holders
        chatManager = new ChatManager(this);
        permissions = new Permissions(this);
        controller = new Controller(this);
        scoreboard = new ScoreboardManager(this);
        mapHandler = new MapHandler(this);
        vanish = new VanishHandler(this);
        duel = new DuelHandler(this);

        // Create command executers
        ArmourCommands ac = new ArmourCommands(this);
        getCommand("armor").setExecutor(ac);
        getCommand("diamond").setExecutor(ac);
        getCommand("iron").setExecutor(ac);
        getCommand("gold").setExecutor(ac);
        getCommand("leather").setExecutor(ac);

        BuffCommands bc = new BuffCommands(this);
        getCommand("buffs").setExecutor(bc);
        getCommand("debuffs").setExecutor(bc);
        getCommand("speed").setExecutor(bc);
        getCommand("strength").setExecutor(bc);
        getCommand("regen").setExecutor(bc);
        getCommand("hunger").setExecutor(bc);
        getCommand("slow").setExecutor(bc);
        getCommand("weak").setExecutor(bc);

        EnchantCommands ec = new EnchantCommands(this);
        getCommand("enchants").setExecutor(ec);
        getCommand("sharp").setExecutor(ec);
        getCommand("power").setExecutor(ec);
        getCommand("knockback").setExecutor(ec);
        getCommand("protect").setExecutor(ec);

        ExtraCommands exc = new ExtraCommands(this);
        getCommand("extra").setExecutor(exc);
        getCommand("soup").setExecutor(exc);
        getCommand("snowball").setExecutor(exc);
        getCommand("tnt").setExecutor(exc);

        ManagerCommands mc = new ManagerCommands(this);
        getCommand("build").setExecutor(mc);
        getCommand("map").setExecutor(mc);
        getCommand("help").setExecutor(mc);
        getCommand("guide").setExecutor(mc);
        getCommand("spectate").setExecutor(mc);
        getCommand("duel").setExecutor(mc);
        getCommand("stats").setExecutor(mc);
        getCommand("addcredits").setExecutor(mc);
        getCommand("modify").setExecutor(mc);

        WeaponCommands wc = new WeaponCommands(this);
        getCommand("weapons").setExecutor(wc);
        getCommand("sword").setExecutor(wc);
        getCommand("bow").setExecutor(wc);
        getCommand("axe").setExecutor(wc);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new ServerListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new BoosterBlockListener(this), this);
    }

    @Override
    public void onDisable()
    {
        if (Bukkit.getOnlinePlayers().length != 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                controller.unloadPLayer(player);
                controller.removePlayer(player);
            }
        }
    }
}
