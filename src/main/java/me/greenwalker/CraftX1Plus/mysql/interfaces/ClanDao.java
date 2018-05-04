package me.floydz69.CraftX1Plus.mysql.interfaces;

import net.sacredlabyrinth.phaed.simpleclans.Clan;

import java.util.List;
import java.util.Map;

/**
 * Created by gusta on 18/05/2017.
 */
public interface ClanDao {

    void create(Clan tag);

    void update(String tag, String colunm, Object value);

    void delete(String tag);

    int retrieveClanWins(String tag);

    int retrieveClanLoses(String tag);

    List<Clan> retrieveAllClans();

}
