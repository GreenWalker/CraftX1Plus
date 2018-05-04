package me.floydz69.CraftX1Plus.mysql.impls;

import me.floydz69.CraftX1Plus.mysql.MySQL;
import me.floydz69.CraftX1Plus.mysql.interfaces.PlayerBatalhasDao;
import me.floydz69.CraftX1Plus.util.DataFormat;
import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerBatalhasImpl implements PlayerBatalhasDao {

    @Override
    public void createPlayerBattle(UUID id, UUID contra, boolean venceu, String tipo) {
        PreparedStatement ps = null;
        Connection cs = null;
        try{
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("INSERT INTO craftx1_batalhas_players (`id`,`id_player`,`contra`,`venceu`,`tipo`,`data`) VALUES(default, ?, ?, ?, ?, ?)");
            ps.setString(1, id.toString());
            ps.setString(2, contra.toString());
            ps.setBoolean(3, venceu);
            ps.setString(4, tipo);
            ps.setString(5, DataFormat.getData());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public void deletePlayerBattle(UUID id) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("DELETE FROM craftx1_batalhas_players WHERE id_player = ?");
            ps.setString(1, id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public List<PlayerBattleHandler> retrievePlayerBattle(UUID uuid) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection cs = null;
        List<PlayerBattleHandler> playerBattleHandlers = new ArrayList<>();
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM craftx1_batalhas_players WHERE id_player = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                boolean venceu;
                venceu = rs.getInt("venceu") != 0;
                PlayerBattleHandler bt = new PlayerBattleHandler(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("contra"))), venceu, rs.getString("data"), rs.getString("tipo"));
                bt.setId(rs.getInt("id"));
                playerBattleHandlers.add(bt);
                bt = null;
            }
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeStatement(ps);
            ps = cs.prepareStatement("SELECT * FROM craftx1_batalhas_players WHERE contra = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                boolean venceu;
                venceu = rs.getInt("venceu") != 0;
                PlayerBattleHandler bt = new PlayerBattleHandler(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("id_player"))), !venceu, rs.getString("data"), rs.getString("tipo"));
                bt.setId(rs.getInt("id"));
                playerBattleHandlers.add(bt);
                bt = null;
            }
            return playerBattleHandlers;
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
