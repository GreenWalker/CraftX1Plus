package me.floydz69.CraftX1Plus.mysql.impls;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.mysql.MySQL;
import me.floydz69.CraftX1Plus.mysql.interfaces.ClanDao;
import net.sacredlabyrinth.phaed.simpleclans.Clan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gusta on 18/05/2017.
 */
public class ClanDaoImpl implements ClanDao{

    @Override
    public void create(Clan tag) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM craftx1_clan WHERE tag = ?");
            ps.setString(1, tag.getTag());
            rs = ps.executeQuery();
            if (!rs.isFirst()) {
                MySQL.getInstance().closeSet(rs);
                MySQL.getInstance().closeStatement(ps);
                ps = cs.prepareStatement("INSERT INTO craftx1_clan VALUES(default, ?, ?, ?)");
                ps.setString(1, tag.getTag());
                ps.setInt(2, 0);
                ps.setInt(3, 0);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public void update(String tag, String colunm, Object value) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("UPDATE craftx1_clan SET `" + colunm + "` = ? WHERE tag = ?");
            ps.setObject(1, value);
            ps.setString(2, tag);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public void delete(String tag){
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("DELETE FROM craftx1_clan WHERE tag = ?");
            ps.setString(1, tag);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public int retrieveClanWins(String clan) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT vitorias FROM craftx1_clan WHERE tag = ?");
            ps.setString(1, clan);
            rs = ps.executeQuery();
            rs.next();
            int i = 0;
            if(rs.first()){
                i = rs.getInt("vitorias");
            }
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
        return 0;
    }

    @Override
    public int retrieveClanLoses(String clan) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT derrotas FROM craftx1_clan WHERE tag = ?");
            ps.setString(1, clan);
            rs = ps.executeQuery();
            rs.next();
            int i = 0;
            if(rs.first()){
               i = rs.getInt("derrotas");
            }
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
        return 0;
    }

    @Override
    public List<Clan> retrieveAllClans() {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        List<Clan> clans = new ArrayList<>();
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT tag FROM craftx1_clan");
            rs = ps.executeQuery();
            while (rs.next()) {
                clans.add(CraftPlus.getMain().getSimpleClans().getClanManager().getClan(rs.getString("tag")));
            }
            return clans;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
        return null;
    }
}
