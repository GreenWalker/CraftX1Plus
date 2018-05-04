package me.floydz69.CraftX1Plus.x1.clan;

import me.floydz69.CraftX1Plus.party.ClanParty;
import me.floydz69.CraftX1Plus.x1.X1Utils;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by gusta on 05/06/2017.
 */
public class ClanX1Helper extends X1Utils {

    private static ClanX1Helper instance;

    private ClanX1Helper(){

    }
    public static ClanX1Helper getInstance(){
        if(null == instance){
            instance = new ClanX1Helper();
        }
        return instance;
    }

    /*
     * String = tag
     */
    private Map<String, ClanX1Handler> pedidosAbertos;

    /*
     * Clans que execuratam o comando /x1 convidar..
     */
    private Map<String, ClanParty> clansEmParty;

    private Map<String, Set<ClanPlayer>> naArena;


    // Métodos
    public ClanParty getClanParty(String tag){
        return getClansEmParty().get(tag);
    }

    public Map<String, ClanParty> getClansEmParty() {
        return clansEmParty;
    }

    public void setClansEmParty(Map<String, ClanParty> clansEmParty) {
        this.clansEmParty = clansEmParty;
    }

    public void addClanParty(ClanParty clan){
        getClansEmParty().put(clan.getClan().getTag(), clan);
    }

    public void removeClanParty(String tag){
        if(getClansEmParty().containsKey(tag)){
            getClansEmParty().remove(tag);
        }
    }

    public boolean containsInClanParty(String tag){
        return getClansEmParty().containsKey(tag);
    }

    public boolean containsPlayerInAnywhereParty(ClanPlayer player){
        for(Map.Entry<String, ClanParty> parties : getClansEmParty().entrySet()){
            if(parties.getValue().getPlayers().contains(player)){
                return true;
            }
        }
        return false;
    }



    public Map<String, ClanX1Handler> getPedidosAbertos() {
        return pedidosAbertos;
    }

    public void setPedidosAbertos(Map<String, ClanX1Handler> pedidosAbertos) {
        this.pedidosAbertos = pedidosAbertos;
    }

    public void addHandler(ClanX1Handler clan){
        getPedidosAbertos().put(clan.getDesafiador().getClanTag(), clan);
    }

    public void removeHandler(String tag){
        if(getPedidosAbertos().containsKey(tag)){
            getPedidosAbertos().remove(tag);
        }
    }

    public ClanX1Handler getHandler(String tag){
        return getPedidosAbertos().get(tag);
    }

    public ClanX1Handler getByDesafiado(String tag){
        for(ClanX1Handler h : getPedidosAbertos().values()){
            if(h.getDesafiado().getClanTag().equalsIgnoreCase(tag)){
                return h;
            }
        }
        return null;
    }

    public void addNaArena(String clanTag, ClanPlayer player){
        if(!this.naArena.containsKey(clanTag)){
            this.naArena.put(clanTag, new HashSet<>());
        }
        this.naArena.get(clanTag).add(player);
    }

    public String containsInArena(ClanPlayer player){
            for(Map.Entry<String,  Set<ClanPlayer>> playerr : this.naArena.entrySet()){
                if(playerr.getValue().contains(player)){
                    return playerr.getKey();
                }
            }
        return null;
    }

    public Set<ClanPlayer> getPlayersNaArena(String tag){
        if(this.naArena.containsKey(tag)){
            return this.naArena.get(tag);
        }
        return null;
    }

    public void addAllNaArena(String clanTag, Set<ClanPlayer> players){
        this.naArena.put(clanTag, players);
    }

    public Map<String, Set<ClanPlayer>> getNaArena() {
        return naArena;
    }

    public void setNaArena(Map<String, Set<ClanPlayer>> naArena) {
        this.naArena = naArena;
    }
}
