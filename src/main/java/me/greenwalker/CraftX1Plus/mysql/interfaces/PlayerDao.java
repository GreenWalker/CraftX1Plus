package me.floydz69.CraftX1Plus.mysql.interfaces;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gusta on 18/05/2017.
 */
public interface PlayerDao {

    void create(Player player);

    void update(UUID player, String colunm, Object value);

    void delete(UUID player);

    int retrievePlayerWins(UUID uuid);

    int retrievePlayerLoses(UUID uuid);

    int retrieveChestSize(UUID uuid);

    List<OfflinePlayer> retrieveAllPlayers();

}
