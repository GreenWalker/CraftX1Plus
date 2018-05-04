package me.floydz69.CraftX1Plus.mysql.impls;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import me.floydz69.CraftX1Plus.mysql.MySQL;
import me.floydz69.CraftX1Plus.mysql.interfaces.PlayerDao;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by gusta on 02/05/2017.
 */
public class PlayerDaoImpl implements PlayerDao {

    @Override
    public void create(Player player) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT * FROM craftx1_player WHERE id = ?");
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (!rs.first()) {
                MySQL.getInstance().closeSet(rs);
                MySQL.getInstance().closeStatement(ps);
                ps = cs.prepareStatement("INSERT INTO craftx1_player VALUES(?,?,?,?,?)");
                ps.setString(1, player.getUniqueId().toString());
                ps.setString(2, player.getName());
                ps.setInt(3, 0);
                ps.setInt(4, 0);
                ps.setInt(5, 0);
                ps.executeUpdate();
            }
        } catch (MySQLIntegrityConstraintViolationException ignored) {

        } catch (SQLException err) {
            System.out.println("Algum erro ocorreu! " + err.getMessage() + err.getSQLState());
            err.printStackTrace();
        } finally {
            MySQL.getInstance().closeSet(rs);
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public void update(UUID id, String colunm, Object v) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("UPDATE craftx1_player SET `" + colunm + "` = ? WHERE id = ?");
            ps.setObject(1, v);
            ps.setString(2, id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public void delete(UUID player) {
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("DELETE FROM craftx1_batalhas_players WHERE id = ?");
            ps.setString(1, player.toString());
            ps.executeUpdate();

            ps = null;
            ps = cs.prepareStatement("DELETE FROM craftx1_player WHERE id = ?");
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.getInstance().closeStatement(ps);
            MySQL.getInstance().closeConnection(cs);
        }
    }

    @Override
    public int retrievePlayerWins(UUID uuid) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `vitorias` FROM craftx1_player WHERE id = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            rs.next();
            int vitorias = 0;
            if (rs.first()) {
                vitorias = rs.getInt("vitorias");
            }
            return vitorias;
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
    public int retrievePlayerLoses(UUID uuid) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT derrotas FROM craftx1_player WHERE id = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            rs.next();
            int i = 0;
            if (rs.first()) {
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
    public int retrieveChestSize(UUID uuid) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `bau_size` FROM craftx1_player WHERE id = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            rs.next();
            int i = 0;
            if (rs.first()) {
                i = rs.getInt("bau_size");
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
    public List<OfflinePlayer> retrieveAllPlayers() {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection cs = null;
        List<OfflinePlayer> players = new ArrayList<>();
        try {
            cs = MySQL.getInstance().getConnection();
            ps = cs.prepareStatement("SELECT `id` FROM craftx1_player");
            rs = ps.executeQuery();
            while (rs.next()) {
                UUID playerid = UUID.fromString(rs.getString(1));
                if (Bukkit.getOfflinePlayer(playerid) != null) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(playerid);
                    players.add(player);
                }
            }
            return players;
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