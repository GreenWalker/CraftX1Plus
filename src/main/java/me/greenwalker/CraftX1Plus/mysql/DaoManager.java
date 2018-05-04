package me.floydz69.CraftX1Plus.mysql;

import me.floydz69.CraftX1Plus.mysql.impls.ClanBatalhasImpl;
import me.floydz69.CraftX1Plus.mysql.impls.ClanDaoImpl;
import me.floydz69.CraftX1Plus.mysql.impls.PlayerBatalhasImpl;
import me.floydz69.CraftX1Plus.mysql.impls.PlayerDaoImpl;
import me.floydz69.CraftX1Plus.mysql.interfaces.ClanBatalhasDao;
import me.floydz69.CraftX1Plus.mysql.interfaces.ClanDao;
import me.floydz69.CraftX1Plus.mysql.interfaces.PlayerBatalhasDao;
import me.floydz69.CraftX1Plus.mysql.interfaces.PlayerDao;

/**
 * Created by gusta on 18/05/2017.
 */
public class DaoManager {

    private static DaoManager instancie;
    private ClanDao clanDao;
    private PlayerDao playerDao;
    private PlayerBatalhasDao playerBatalhasDao;
    private ClanBatalhasDao clanBatalhasDao;

    public static DaoManager getInstancie() {
        if(null == instancie){
            instancie = new DaoManager();
        }
        return instancie;

    }

    public ClanDao getClanDao() {
        if(clanDao == null){
            clanDao = new ClanDaoImpl();
        }
        return clanDao;
    }

    public PlayerDao getPlayerDao() {
        if(playerDao == null){
            playerDao = new PlayerDaoImpl();
        }
        return playerDao;
    }

    public PlayerBatalhasDao getPlayerBatalhasDao() {
        if(playerBatalhasDao == null){
            playerBatalhasDao = new PlayerBatalhasImpl();
        }
        return playerBatalhasDao;
    }

    public ClanBatalhasDao getClanBatalhasDao() {
        if(clanBatalhasDao == null){
            clanBatalhasDao = new ClanBatalhasImpl();
        }
        return clanBatalhasDao;
    }
}
