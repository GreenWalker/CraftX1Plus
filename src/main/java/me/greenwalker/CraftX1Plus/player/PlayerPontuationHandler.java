package me.floydz69.CraftX1Plus.player;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.util.DataFormat;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by gusta on 20/05/2017.
 */
public abstract class PlayerPontuationHandler extends CraftPlusPlayer {

    private PlayerManager pm;
    private ConfigHandler db;
    protected boolean mysql;
    private DatabaseUtil dbSql;


    protected PlayerPontuationHandler(Player player, CraftPlus plugin, ConfigHandler clanDb, DatabaseUtil dbSql) {
        super(player, plugin);
        this.pm = plugin.playerManager;  // Tentando desacoplar um pouco, sei que isso é muito feio :'(
        this.db = clanDb;
        this.dbSql = dbSql;
        this.mysql = plugin.isMysql();
    }

    public void addVitoria(int i) {
            if (mysql) {
                dbSql.setPlayerWins(getPlayer(), getVitorias() + i);
                pm.getPlayer(getPlayerID()).setPlayer_vitorias(getVitorias() + i);
            } else {
                db.set("Players." + getPlayerID() + ".Vitorias", getVitorias() + i);
                pm.getPlayer(getPlayerID()).setPlayer_vitorias(getVitorias() + i);
                db.trySave();
                return;
            }
    }

    public int getVitorias() {
        return pm.getPlayer(getPlayerID()).getPlayer_vitorias();
    }

    public void addDerrota(int i) {
            if (mysql) {
                dbSql.setPlayerLoses(getPlayer(), getDerrotas() + i);
                pm.getPlayer(getPlayerID()).setPlayer_derrotas(getDerrotas() + i);
            } else {
                db.set("Players." + getPlayerID() + ".Derrotas", getDerrotas() + i);
                pm.getPlayer(getPlayerID()).setPlayer_derrotas(getDerrotas() + i);
                db.trySave();;
            }
    }

    public int getDerrotas() {
        return  pm.getPlayer(getPlayerID()).getPlayer_derrotas();
    }

    public void addBatalha(UUID contra, boolean venceu, String tipo) {
        if(mysql) {
            dbSql.setPlayerBattle(getPlayerID(), contra, venceu, tipo);
            pm.getPlayer(getPlayerID()).getBatalhas().add(new PlayerBattleHandler(Bukkit.getOfflinePlayer(contra), venceu, DataFormat.getData(), tipo));
            pm.getPlayer(contra).getBatalhas().add(new PlayerBattleHandler(Bukkit.getOfflinePlayer(getPlayerID()), !venceu, DataFormat.getData(), tipo));
        }else {
            db.set("Players." + getPlayerID() + ".Batalhas", contra.toString() + "," + venceu + "," + tipo + "," + DataFormat.getData());
            pm.getPlayer(getPlayerID()).getBatalhas().add(new PlayerBattleHandler(Bukkit.getOfflinePlayer(contra), venceu, DataFormat.getData(), tipo));
            db.trySave();
        }
    }

    public void registrar() {
        if (mysql) {
            if(!pm.contains(getPlayerID())) {
                dbSql.setPlayer(getPlayer());
                pm.registerNew(getPlayer());
            }
        } else {
            if (!db.contains("Players." + getPlayerID())) {
                db.set("Players." + getPlayerID() + ".Nome", getPlayerName());
                db.set("Players." + getPlayerID() + ".Vitorias", 0);
                db.set("Players." + getPlayerID() + ".Derrotas", 0);
                db.set("Players." + getPlayerID() + ".Bau", 0);
                db.set("Players." + getPlayerID() + ".Batalhas", "");
                db.trySave();
            }
        }
    }

}
