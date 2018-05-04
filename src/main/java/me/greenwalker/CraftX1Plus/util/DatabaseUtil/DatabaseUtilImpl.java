package me.floydz69.CraftX1Plus.util.DatabaseUtil;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.clan.ClanCache;
import me.floydz69.CraftX1Plus.clan.ClanPlusManager;
import me.floydz69.CraftX1Plus.mysql.DaoManager;
import me.floydz69.CraftX1Plus.player.PlayerCache;
import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gusta on 17/05/2017.
 */
public class DatabaseUtilImpl implements DatabaseUtil {

    private DaoManager dm;
    private ClanPlusManager clanManager;

    public DatabaseUtilImpl(DaoManager dm, ClanPlusManager clanManager) {
        this.dm = dm;
        this.clanManager = clanManager;
    }

    @Override
    public void setPlayer(Player player) {
        dm.getPlayerDao().create(player);
    }

    @Override
    public void setPlayerWins(Player p, int n) {
        dm.getPlayerDao().update(p.getUniqueId(), "vitorias", n);
    }

    @Override
    public void setPlayerLoses(Player p, int n) {
        dm.getPlayerDao().update(p.getUniqueId(), "derrotas", n);
    }

    @Override
    public void setPlayerBauSize(Player p, int n) {
        dm.getPlayerDao().update(p.getUniqueId(), "bau_size", n);
    }

    @Override
    public void setPlayerBattle(UUID player, UUID contra, boolean venceu, String tipo) {
        dm.getPlayerBatalhasDao().createPlayerBattle(player, contra, venceu, tipo);
    }

    @Override
    public int getPlayerWins(UUID player) {
        return dm.getPlayerDao().retrievePlayerWins(player);
    }

    @Override
    public int getPlayerLoses(UUID player) {
        return dm.getPlayerDao().retrievePlayerLoses(player);
    }

    @Override
    public int getPlayerBauSize(UUID player) {
        return dm.getPlayerDao().retrieveChestSize(player);
    }

    @Override
    public PlayerCache getPlayer(UUID player) {
        OfflinePlayer player1 = Bukkit.getOfflinePlayer(player);
        int vitorias = dm.getPlayerDao().retrievePlayerWins(player);
        int derrotas = dm.getPlayerDao().retrievePlayerLoses(player);
        int bau = dm.getPlayerDao().retrieveChestSize(player);
        List<PlayerBattleHandler> batalhas = dm.getPlayerBatalhasDao().retrievePlayerBattle(player);
        PlayerCache playerCache = new PlayerCache(player1, vitorias, derrotas, bau, batalhas);
       return playerCache;
    }

    @Override
    public Map<UUID, PlayerCache> getPlayers() {
        Map<UUID, PlayerCache> playerCacheMap = new HashMap<>();
        for (OfflinePlayer s : dm.getPlayerDao().retrieveAllPlayers()) {
            if (s != null) {
                playerCacheMap.put(s.getUniqueId(), getPlayer(s.getUniqueId()));
            }
        }
        System.out.println("Players Registrados " + playerCacheMap.size());
        return playerCacheMap;
    }

    @Override
    public void setClan(Clan clan) {
        dm.getClanDao().create(clan);
    }

    @Override
    public void setClanWins(String tag, int n) {
        dm.getClanDao().update(tag, "vitorias", n);
    }

    @Override
    public void setClanLoses(String tag, int n) {
        dm.getClanDao().update(tag, "derrotas", n);
    }

    @Override
    public int getClanWins(String tag) {
        return dm.getClanDao().retrieveClanWins(tag);
    }

    @Override
    public int getClanLoses(String tag) {
        return dm.getClanDao().retrieveClanLoses(tag);
    }

    @Override
    public ClanCache getClan(String tag) {
        final ClanCache[] clanCache = {null};
        Bukkit.getScheduler().runTaskAsynchronously(CraftPlus.getMain(), new Runnable() {
            @Override
            public void run() {
                Clan clan = clanManager.getByTag(tag);
                int vitorias = dm.getClanDao().retrieveClanWins(tag);
                int derrotas = dm.getClanDao().retrieveClanLoses(tag);
                Map<String, Boolean> batalhas = dm.getClanBatalhasDao().retrieveClanBattle(tag);
                clanCache[0] = new ClanCache(clan, vitorias, derrotas, batalhas);
            }
        });
        return clanCache[0];
    }

    @Override
    public Map<String, ClanCache> getClans() {
        Map<String, ClanCache> clans = new HashMap<>();
        for (Clan s : dm.getClanDao().retrieveAllClans()) {
            if (s != null) {
                clans.put(s.getTag(), getClan(s.getTag()));
            } else continue;
        }
        System.out.println("Clans Registrados " + clans.size());
        return clans;
    }
}
