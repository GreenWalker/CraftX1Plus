package me.floydz69.CraftX1Plus;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.Core;
import com.onarandombox.MultiverseCore.api.MVPlugin;
import me.floydz69.CraftX1Plus.arena.ArenaManager;
import me.floydz69.CraftX1Plus.clan.ClanPlusManager;
import me.floydz69.CraftX1Plus.commands.ClanCommands;
import me.floydz69.CraftX1Plus.commands.ArenaCommands;
import me.floydz69.CraftX1Plus.commands.PlayerCommands;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.events.ClanEventHandler;
import me.floydz69.CraftX1Plus.events.ClickInventoryHandlerEvent;
import me.floydz69.CraftX1Plus.events.EventManager;
import me.floydz69.CraftX1Plus.menu.GuiManager;
import me.floydz69.CraftX1Plus.mysql.DaoManager;
import me.floydz69.CraftX1Plus.mysql.MySQL;
import me.floydz69.CraftX1Plus.player.PlayerManager;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtilImpl;
import me.floydz69.CraftX1Plus.util.Prefix;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.x1.clan.ClanX1Helper;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Helper;
import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class CraftPlus extends JavaPlugin {

    private final String mutiVerse = "Multiverse-Core";

    private ConsoleCommandSender cs = getServer().getConsoleSender();
    private Economy economy = null;
    private SimpleClans sc = null;
    private MVPlugin mvcore = null;
    private ConfigHandler lang = null;
    private ConfigHandler playerDb = null;
    private ConfigHandler locations = null;
    private ConfigHandler clanDb = null;
    private static CraftPlus main;
    public PlayerManager playerManager = null;
    private ArenaManager arenaManager = null;
    private Loaders loader;
    private boolean mysql;

    private static int ALREADY_LOAD_ARENAS = 0;

    @Override
    public void onLoad() {
        try {
            sc = (SimpleClans) getServer().getPluginManager().getPlugin("SimpleClans");

            if(sc != null){
                getServer().getLogger().log(Level.INFO,"SimpleClans v. " + ((Plugin) sc).getDescription().getVersion() + " is founded in this server! hooked.");
            }else{
                getServer().getLogger().warning("Is not possible hook this plugin to simpleclans! disabling it.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            mvcore = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");

            if(mvcore != null){
                getServer().getLogger().log(Level.INFO, "Multiverse-Core v. " + ((Plugin)mvcore).getDescription().getVersion() + " founded in this server! Multi Worlds is now supported.");
            }else{
                getServer().getLogger().warning("Multiverse-core was not found. Multi Worlds support will not be provided. errors be happen");
                //getServer().getPluginManager().disablePlugin(this);
            }

            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        main = this;
        setupConfig();
        Prefix.setPrefix(getLang().getString("prefix"));
        if (getPConfig().getBoolean("Usar-mysql")) {
            this.mysql = true;
            MySQL.getInstance();
        }
        setupInstancies();
        cs.sendMessage(Util.getPrefix() + "§aPlugin §c" + getName() + "§a is Started!");
    }

    @Override
    public void onDisable() {
        try {
            Util.saveArenas(arenaManager.getArenas());
            HandlerList.unregisterAll();
            Bukkit.getScheduler().cancelAllTasks();
            for (Player on : getServer().getOnlinePlayers()) {
                on.closeInventory();
            }
        }catch (Exception ignored){}
    }

    private void setupInstancies() {
        PlayerX1Helper.setX1Handlers(new HashMap<>());
        PlayerX1Helper.setPendente(new HashMap<>());
        PlayerX1Helper.setInGame(new HashMap<>());
        PlayerX1Helper.setEspera(new HashSet<>());
        PlayerX1Helper.setIniciando(new HashSet<>());
        PlayerX1Helper.setSaindo(new HashSet<>());

        ClanX1Helper clanX1Helper = ClanX1Helper.getInstance();
        clanX1Helper.setClansEmParty(new HashMap<>());
        clanX1Helper.setPedidosAbertos(new HashMap<>());
        clanX1Helper.setNaArena(new HashMap<>());

        PluginManager pm = this.getServer().getPluginManager();

        ClanPlusManager plusManager = new ClanPlusManager(getSimpleClans().getClanManager(), this, null, getClanDb());
        DatabaseUtil db = new DatabaseUtilImpl(DaoManager.getInstancie(), plusManager);
        loader = new Loaders(this, db, plusManager);
        plusManager.setClans(loader.loadClans(getClanDb()));
        arenaManager = new ArenaManager(this, loader.loadArenas(true, locations));
        playerManager = new PlayerManager(this, loader.loadPlayers(getPlayerDb()));
        GuiManager guiManager = new GuiManager(this, arenaManager, lang, plusManager, playerManager);

        pm.registerEvents(new ClickInventoryHandlerEvent(this, arenaManager, guiManager, lang, db, playerManager), this);
        pm.registerEvents(new EventManager(this, guiManager, playerManager, plusManager,lang), this);
        pm.registerEvents(new ClanEventHandler(this), this);

        getCommand("x1admin").setExecutor(new ArenaCommands(this, arenaManager, lang));
        getCommand("x1clan").setExecutor(new ClanCommands(this, getSimpleClans().getClanManager(), db, getLang()));
        getCommand("x1").setExecutor(new PlayerCommands(this, arenaManager, guiManager, lang, playerManager, getSimpleClans().getClanManager() ,db));
    }

    private void setupConfig() {
        try {
            lang = new ConfigHandler(this, "lang.yml");
            playerDb = new ConfigHandler(this, "playerdb.yml");
            clanDb = new ConfigHandler(this, "clandb.yml");
            locations = new ConfigHandler(this, "locations.yml");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        if (!new File(getDataFolder(), "config.yml").exists()) {
            //saveResource("readme.txt", false);
            saveDefaultConfig();
        }
    }

    public Core getMvWorld() {
        try{
            return mvcore.getCore();
        }catch (Exception e1){
            getServer().getLogger().warning("Uma chamada a para o plugin Multiverse-core não pode ser realizada, pois o mesmo não se encontra disponível. ERROS ACONTECERÃO");
        }
        return null;
    }

    public Loaders getLoader() {
        return loader;
    }

    public Economy getEconomy() {
        return economy;
    }

    public static CraftPlus getMain() {
        return main;
    }

    public boolean isMysql() {
        return mysql;
    }

    public void setMysql(boolean bo) {
        this.mysql = bo;
    }

    public FileConfiguration getPConfig() {
        return getConfig();
    }

    public ConfigHandler getLang() {
        return lang;
    }

    public ConfigHandler getPlayerDb() {
        return playerDb;
    }

    public ConfigHandler getClanDb() {
        return clanDb;
    }

    public ConfigHandler getLocations() {
        return locations;
    }

    public SimpleClans getSimpleClans() {
        return sc;
    }

}
