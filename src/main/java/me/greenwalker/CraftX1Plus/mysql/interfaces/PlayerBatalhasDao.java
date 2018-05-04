package me.floydz69.CraftX1Plus.mysql.interfaces;

import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;

import java.util.List;
import java.util.UUID;

/**
 * Created by gusta on 18/05/2017.
 */
public interface PlayerBatalhasDao {

    void createPlayerBattle(UUID tag, UUID contra, boolean venceu, String tipo);

    void deletePlayerBattle(UUID id);

    List<PlayerBattleHandler> retrievePlayerBattle(UUID uuid);

}
