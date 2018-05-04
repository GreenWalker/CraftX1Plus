package me.floydz69.CraftX1Plus.clan;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.clan.Object.CraftPlusClan;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by gusta on 24/05/2017.
 */
public abstract class ClanPontuation extends CraftPlusClan {

    private ConfigHandler db;
    private DatabaseUtil dbSql;
    private boolean mysql;
    private ClanPlusManager cm;

    protected ClanPontuation(CraftPlus craftPlus, Clan clan, ConfigHandler clandb, DatabaseUtil databaseUtil) {
        super(craftPlus, clan);
        this.db = clandb;
        this.dbSql = databaseUtil;
        this.mysql = craftPlus.isMysql();
    }

    public void addVitoria(int i) {
        if (i > 0) {
            if (mysql) {
                dbSql.setClanWins(getClanTag(), getVitorias() + i);
            } else {
                db.set("Clans." + getClanTag() + ".Vitorias", getVitorias() + i);
                db.trySave();
                return;
            }
        } else {
            return;
        }
    }

    public int getVitorias() {
        if (mysql) {
            final int[] vitorias = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    vitorias[0] = dbSql.getClanWins(getClanTag());
                    cancel();
                }
            }.runTaskAsynchronously(plugin);
            return vitorias[0];
        } else {
            return db.getInt("Clans." + getClanTag() + ".Vitorias");
        }
    }

    public void addDerrota(int i) {
        if (i > 0) {
            if (mysql) {
                dbSql.setClanLoses(getClanTag(), getDerrotas() + i);
            } else {
                db.set("Clans." + getClanTag() + ".Derrotas", getDerrotas() + i);
                db.trySave();
                return;
            }
        } else {
            return;
        }
    }

    public int getDerrotas() {
        if (mysql) {
            final int[] derrotas = {-1};
            new BukkitRunnable() {
                @Override
                public void run() {
                    derrotas[0] = dbSql.getClanWins(getClanTag());
                    cancel();
                }
            }.runTaskAsynchronously(plugin);
            return derrotas[0];
        } else {
            return db.getInt("Clans." + getClanTag() + ".Derrotas");
        }
    }

    public void registrar() {
        if (mysql) {
            if(!cm.hasClanMemory(getClanTag())){
                dbSql.setClan(getClan());
            }
        } else {
            if (!db.contains("Clans." + getClanTag())) {
                db.set("Clans." + getClanTag() + ".Nome", getClan().getName());
                db.set("Clans." + getClanTag() + ".Vitorias", 0);
                db.set("Clans." + getClanTag() + ".Derrotas", 0);
                db.set("Clans." + getClanTag() + ".Batalhas", "");
                db.trySave();
            }
        }
    }
}
