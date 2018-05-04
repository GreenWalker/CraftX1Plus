package me.floydz69.CraftX1Plus.util.DatabaseUtil;

import me.floydz69.CraftX1Plus.clan.ClanCache;
import me.floydz69.CraftX1Plus.player.PlayerCache;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * Created by gusta on 17/05/2017.
 */
public interface DatabaseUtil {


    void setPlayer(Player player);

    void setPlayerWins(Player p, int n);

    void setPlayerLoses(Player p, int n);

    void setPlayerBauSize(Player p, int n);

    void setPlayerBattle(UUID player, UUID contra, boolean venceu, String tipo);

    int getPlayerWins(UUID player);

    int getPlayerLoses(UUID player);

    int getPlayerBauSize(UUID player);

    PlayerCache getPlayer(UUID player);

    Map<UUID, PlayerCache> getPlayers();

    //Clan


    void setClan(Clan clan);

    void setClanWins(String tag, int n);

    void setClanLoses(String tag, int n);

    int getClanWins(String tag);

    int getClanLoses(String tag);

    ClanCache getClan(String tag);

    Map<String, ClanCache> getClans();

}
