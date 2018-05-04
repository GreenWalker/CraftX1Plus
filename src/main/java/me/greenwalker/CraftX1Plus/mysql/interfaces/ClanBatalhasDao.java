package me.floydz69.CraftX1Plus.mysql.interfaces;

import java.util.Map;
import java.util.UUID;

/**
 * Created by gusta on 18/05/2017.
 */
public interface ClanBatalhasDao {

    void createClanBattle(String tag, String contra, boolean venceu, String date);

    void deleteClanBattle(String tag);

    Map<String, Boolean> retrieveClanBattle(String tag);
}
