package me.floydz69.CraftX1Plus.mysql.impls;

import me.floydz69.CraftX1Plus.mysql.MySQL;
import me.floydz69.CraftX1Plus.mysql.interfaces.ClanBatalhasDao;
import me.floydz69.CraftX1Plus.util.DataFormat;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gusta on 18/05/2017.
 */
public class ClanBatalhasImpl implements ClanBatalhasDao{

    @Override
    public void createClanBattle(String tag, String contra, boolean venceu, String date) {
         PreparedStatement ps = null;
         Connection cs = null;
         try{
             cs = MySQL.getInstance().getConnection();
             ps = cs.prepareStatement("INSERT INTO craftx1_batalhas_clans VALUES(default, ?, ?, ?, ?)");
             ps.setString(1, tag);
             ps.setString(2, contra);
             ps.setBoolean(3, venceu);
             ps.setString(4, DataFormat.getData());
             ps.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
         }finally {
             MySQL.getInstance().closeStatement(ps);
             MySQL.getInstance().closeConnection(cs);
         }
    }

    @Override
    public void deleteClanBattle(String tag) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("DELETE FROM craftx1_batalhas_clans WHERE tag = ?");
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
    public Map<String, Boolean> retrieveClanBattle(String tag) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        Map<String, Boolean> clan_wars = new HashMap<String, Boolean>();
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM craftx1_batalhas_clans WHERE tag = ?");
            ps.setString(1, tag);
            rs = ps.executeQuery();
            while (rs.next()) {
                boolean venceu = false;
                if (rs.getInt("venceu") == 0) {
                    venceu = false;
                } else {
                    venceu = true;
                }
                String clan_contra = rs.getString("contra");
                clan_wars.put(clan_contra, venceu);
            }
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeStatement(ps);
            return clan_wars;
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
