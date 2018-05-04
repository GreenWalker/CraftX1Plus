package me.floydz69.CraftX1Plus.clan;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * Created by gusta on 15/05/2017.
 */
public class ClanPlusManager {

    private ClanManager clanManager;
    private CraftPlus craftPlus;
    private ConfigHandler clandb;
    private boolean mysql;
    private Map<String, ClanCache> clans;

    public ClanPlusManager(ClanManager clanManager, CraftPlus craftPlus, Map<String, ClanCache> clans, ConfigHandler clandb) {
        this.craftPlus = craftPlus;
        this.clanManager = clanManager;
        this.clandb = clandb;
        this.mysql = craftPlus.isMysql();
        this.clans = clans;
    }

    public void setClans(Map<String, ClanCache> clans) {
        this.clans = clans;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public ClanPlayer getClanPlayer(OfflinePlayer p) {
        return clanManager.getClanPlayer(p.getUniqueId());
    }

    public Clan getClanByPlayer(Player p){
        return clanManager.getClanPlayer(p).getClan();
    }

    public List<ClanPlayer> getLeaders(Clan clan){
        return clan.getLeaders();
    }

    public Clan getByTag(String tag){
        return clanManager.getClan(tag);
    }

    public Map<String, ClanCache> getClans() {
        return clans;
    }

    public boolean hasClanMemory(String tag){
        return getClans().containsKey(tag);
    }

    public Clan getClanMemory(String tag){
        if(hasClanMemory(tag)){
            return getClans().get(tag).getClan();
        }
        return null;
    }
}
