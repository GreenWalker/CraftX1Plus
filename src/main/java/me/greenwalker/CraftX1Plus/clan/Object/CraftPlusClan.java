package me.floydz69.CraftX1Plus.clan.Object;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.Util;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by gusta on 02/05/2017.
 */
public abstract class CraftPlusClan {

    protected CraftPlus plugin;
    private Clan clan;
    private List<ClanPlayer> clanPlayers;
    private int clanSize;
    private String clanTag;

    protected CraftPlusClan(CraftPlus craftPlus, Clan clan) {
        this.clan = clan;
        this.plugin = craftPlus;
        this.clanPlayers = clan.getAllMembers();
        this.clanSize = clan.getSize();
        this.clanTag = clan.getTag();
    }

    public Clan getClan() {
        return this.clan;
    }

    public List<ClanPlayer> getClanAllPlayers() {
        return this.clanPlayers;
    }

    public ClanPlayer getClanPlayer(UUID uuid) {
        return plugin.getSimpleClans().getClanManager().getClanPlayer(uuid);
    }

    public int getClanSize() {
        return this.clanSize;
    }

    public String getClanTag() {
        return clanTag;
    }

    public boolean InThisClan(ClanPlayer player) {
       return this.clanPlayers.contains(player);
    }

}
