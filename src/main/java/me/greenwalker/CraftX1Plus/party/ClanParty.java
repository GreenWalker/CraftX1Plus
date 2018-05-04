package me.floydz69.CraftX1Plus.party;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.exception.NotFound;
import me.floydz69.CraftX1Plus.util.Util;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gusta on 06/06/2017.
 */
public class ClanParty {

    private CraftPlus plugin;
    private Clan clan;
    private Set<ClanPlayer> players;
    private ConfigHandler lang;
    private int maxMembers;

    public ClanParty(CraftPlus plugin, Clan clan){
        this.plugin = plugin;
        this.clan = clan;
        this.lang = plugin.getLang();
        this.players = new HashSet<>();
        this.maxMembers = plugin.getPConfig().getInt("MaxPlayers");
    }

    public String adcionaPlayer(ClanPlayer player){
        if(getPlayers().size() >= getMaxMembers()){
            return lang.getStringReplaced("Clan.PartyCheia", "@Prefix", Util.getPrefix());
        }
        if(getPlayers().contains(player)){
            return lang.getStringReplaced("Clan.PlayerJaAdcionado", "@Prefix", Util.getPrefix());
        }
        if(!player.getClan().equals(getClan())){
            return lang.getStringReplaced("Clan.PlayerRival", "@Prefix", Util.getPrefix());
        }
        getPlayers().add(player);
        sendMessageForAll(lang.getStringReplaced("Clan.PlayerEntrou", "@Prefix", Util.getPrefix(), "@Player", player.getName()));
        return lang.getStringReplaced("Clan.PlayerAdcionado", "@Prefix", Util.getPrefix(), "@Player", player.getName());
    }

    public String removePlayer(ClanPlayer player){
        if(!getPlayers().contains(player)){
            return lang.getStringReplaced("Clan.PlayerNaoEstaNaParty", "@Prefix", Util.getPrefix());
        }
        if(!player.toPlayer().isOnline()){
            return lang.getStringReplaced("Clan.PlayerOffline", "@Prefix", Util.getPrefix());
        }
        if(!player.getClan().equals(getClan())){
            return lang.getStringReplaced("Clan.PlayerRival", "@Prefix", Util.getPrefix());
        }
        getPlayers().remove(player);
        getPlayers().forEach(s -> s.toPlayer().sendMessage(lang.getStringReplaced("Clan.PlayerSaiu", "@Prefix", Util.getPrefix(), "@Player", player.getName())));
        return lang.getStringReplaced("Clan.PlayerRemovido", "@Prefix", Util.getPrefix(), "@Player", player.getName());
    }

    public void sendMessageForAll(String ... msg){
        getPlayers().forEach(s -> s.toPlayer().sendMessage(msg));
    }

    public void teleportAll(Location location){
        getPlayers().forEach(s -> s.toPlayer().teleport(location));
    }

    public ClanPlayer getPlayer(String name) throws NotFound{
        for(ClanPlayer player : getPlayers()){
            if(player.getName().equals(name)){
                return player;
            }
        }
        throw new NotFound("");
    }

    public Set<ClanPlayer> getPlayers() {
        return players;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        maxMembers = maxMembers;
    }
}
