package me.floydz69.CraftX1Plus;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.arena.ArenaManager;
import me.floydz69.CraftX1Plus.clan.ClanCache;
import me.floydz69.CraftX1Plus.clan.ClanPlusManager;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.player.PlayerCache;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import me.floydz69.CraftX1Plus.util.enums.ArenaType;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.*;


public class Loaders {

    private CraftPlus plugin;
    private DatabaseUtil databaseUtil;
    private ClanPlusManager plusManager;

    public Loaders(CraftPlus plus, DatabaseUtil databaseUtil, ClanPlusManager plusManager) {
        this.plugin = plus;
        this.databaseUtil = databaseUtil;
        this.plusManager = plusManager;
    }

    public Arena loadArena(String name, boolean writeMessageInScreen, ConfigHandler config) {
        String path = "Arenas." + name;
        if (config.contains(path)) {
            plugin.getServer().getConsoleSender().sendMessage("Carregando a arena " + name);
            String arena_name = config.getString(path + ".Nome");
            Location pos1 = new Location(Bukkit.getWorld(config.getString(path + ".Pos1.World")), config.getDouble(path + ".Pos1.x"), config.getDouble(path + ".Pos1.y"), config.getDouble(path + ".Pos1.z"));
            Location pos2 = new Location(Bukkit.getWorld(config.getString(path + ".Pos2.World")), config.getDouble(path + ".Pos2.x"), config.getDouble(path + ".Pos2.y"), config.getDouble(path + ".Pos2.z"));
            Location saida = new Location(Bukkit.getWorld(config.getString(path + ".Saida.World")), config.getDouble(path + ".Saida.x"), config.getDouble(path + ".Saida.y"), config.getDouble(path + ".Saida.z"));
            Location camarote = new Location(Bukkit.getWorld(config.getString(path + ".Camarote.World")), config.getDouble(path + ".Camarote.x"), config.getDouble(path + ".Camarote.y"), config.getDouble(path + ".Camarote.z"));
            Location espera = new Location(Bukkit.getWorld(config.getString(path + ".Espera.World")), config.getDouble(path + ".Espera.x"), config.getDouble(path + ".Espera.y"), config.getDouble(path + ".Espera.z"));
            ArenaStatus status = ArenaStatus.fromName(config.getString(path + ".Status"));
            ArenaType type = ArenaType.fromName(config.getString(path + ".Tipo"));
            Arena arena = new Arena(plugin, name, arena_name, pos1, pos2, camarote, espera, saida, type, status);
            if(writeMessageInScreen) {
                plugin.getServer().getConsoleSender().sendMessage(new String[]{"Arena " + name + " Carregada",
                        "Pos 1    : x = " + pos1.getX() + " y = " + pos1.getY() + " z = " + pos1.getY() + " World = " + (pos1.getWorld() == null ? "nulo" : pos1.getWorld().getName()),
                        "Pos 2    : x = " + pos2.getX() + " y = " + pos2.getY() + " z = " + pos2.getZ() + " World = " + (pos2.getWorld() == null ? "nulo" : pos2.getWorld().getName()),
                        "Saida    : x = " + saida.getX() + " y = " + saida.getY() + " z = " + saida.getZ() + " World = " + (saida.getWorld() == null ? "nulo" : saida.getWorld().getName()),
                        "Espera   : x = " + espera.getX() + " y = " + espera.getY() + " z = " + espera.getZ() + " World = " + (espera.getWorld() == null ? "nulo" : espera.getWorld().getName()),
                        "Camarote : x = " + camarote.getX() + " y = " + camarote.getY() + " z = " + camarote.getZ() + " World = " + (camarote.getWorld() == null ? "nulo" : camarote.getWorld().getName())});
            }
            return arena;
        } else {
            System.out.println("Arena nao existe na config");
        }
        return null;
    }

    public List<Arena> loadArenas(boolean w, ConfigHandler config) {
        List<Arena> arenas = new ArrayList<>();
        if (config.contains("Arenas")) {
            if (config.isConfigurationSection("Arenas")) {
                for (String s : config.getConfigurationSection("Arenas").getKeys(false)) {
                    try {
                        arenas.add(loadArena(s, w, config));
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            }
        }
        if(CraftPlus.ALREADY_LOAD_ARENAS == 0){
            CraftPlus.ALREADY_LOAD_ARENAS++;
        }
        return arenas;
    }

    public PlayerCache loadPlayer(UUID playerid, ConfigHandler playerdb) {
        if (!plugin.isMysql()) {
            if (playerdb != null) {
                String path = "Players." + playerid.toString();
                if (playerdb.contains(path)) {
                    OfflinePlayer pl = Bukkit.getOfflinePlayer(playerid);
                    int vitorias = playerdb.getInt(path + ".Vitorias");
                    int derrotas = playerdb.getInt(path + ".Derrotas");
                    int bau = playerdb.getInt(path + ".Bau");
                    List<PlayerBattleHandler> batalhas = null;
                    if (playerdb.isList(path + ".Batalhas")) {
                        batalhas = new ArrayList<>();
                        for (String s : playerdb.getStringList(path + ".Batalhas")) {
                            String[] formatted = s.split(",");
                            boolean venceu = Boolean.valueOf(formatted[1]);
                            batalhas.add(new PlayerBattleHandler(Bukkit.getOfflinePlayer(UUID.fromString(formatted[0])), venceu, formatted[2], formatted[3]));
                        }
                    }
                    return new PlayerCache(pl, vitorias, derrotas, bau, batalhas);
                }
            }
            return null;
        } else {
            return databaseUtil.getPlayer(playerid);
        }
    }

    public Map<UUID, PlayerCache> loadPlayers(ConfigHandler playerdb) {
        if (!plugin.isMysql()) {
            Map<UUID, PlayerCache> playerCaches = new HashMap<>();
            if (playerdb.contains("Players")) {
                if (playerdb.isConfigurationSection("Players")) {
                    for (String playerIds : playerdb.getConfigurationSection("Players").getKeys(false)) {
                        UUID playerId = UUID.fromString(playerIds);
                        try {
                            playerCaches.put(playerId, loadPlayer(playerId, playerdb));
                        } catch (Exception err) {
                            System.err.println("Contate o desenvolvedor (Floydz69) sobre o erro adjacente");
                            err.printStackTrace();
                        }
                    }
                }
            }
            return playerCaches;
        } else {
            return databaseUtil.getPlayers();
        }
    }

    public ClanCache loadClan(String tag, ConfigHandler clandb) {
        if (!plugin.isMysql()) {
            if (clandb != null) {
                String path = "Clans." + clandb;
                if (clandb.contains(path)) {
                    Clan clan = plusManager.getByTag(tag);
                    int vitorias = clandb.getInt(path + ".Vitorias");
                    int derrotas = clandb.getInt(path + ".Derrotas");
                    HashMap<String, Boolean> batalhas;
                    if (clandb.isList(path + ".Batalhas")) {
                        batalhas = new HashMap<>();
                        for (String s : clandb.getStringList(path + ".Batalhas")) {
                            String[] formatted = s.split(",");
                            boolean venceu = Boolean.valueOf(formatted[1]);
                            batalhas.put(formatted[0], venceu);
                        }
                    } else {
                        batalhas = new HashMap<>();
                        batalhas.put("SemBatalhas", false);
                    }
                    return new ClanCache(clan, vitorias, derrotas, batalhas);
                }
            }
            return null;
        } else {
            return databaseUtil.getClan(tag);
        }
    }

    public Map<String, ClanCache> loadClans(ConfigHandler clandb) {
        if (!plugin.isMysql()) {
            Map<String, ClanCache> clanCacheHashMap = new HashMap<>();
            if (clandb.contains("Clans")) {
                if (clandb.isConfigurationSection("Clans")) {
                    for (String clan : clandb.getConfigurationSection("Clans").getKeys(false)) {
                        try {
                            clanCacheHashMap.put(clan, loadClan(clan, clandb));
                        } catch (Exception err) {
                            System.err.println("Contate o desenvolvedor (Floydz69) sobre o erro adjacente");
                            err.printStackTrace();
                        }
                    }
                }
                return clanCacheHashMap;
            }
            return null;
        } else {
            return databaseUtil.getClans();
        }
    }
}
