package me.floydz69.CraftX1Plus.player;

import com.sun.istack.internal.NotNull;
import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.mysql.impls.PlayerDaoImpl;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by gusta on 01/05/2017.
 */
public abstract class CraftPlusPlayer {

    private Player player;
    private String name;
    private UUID uuid;
    private CraftPlus plugin;

    protected CraftPlusPlayer(Player player, CraftPlus plugin) {
        this.player = player;
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.plugin = plugin;
    }

    public void depositPlayer(double quantia){
        plugin.getEconomy().depositPlayer(player, quantia);
    }

    public boolean removeBalance(double quantia){
        if(plugin.getEconomy().has(player, quantia)) {
            plugin.getEconomy().withdrawPlayer(player, quantia);
            return true;
        }else{
            return false;
        }
    }

    public String getPlayerName() {
        return name;
    }

    public Player getPlayer(){return player; }

    public Location getPlayerLocation() {
        return player.getLocation();
    }

    public void sendPlayerMessage(String msg) {
        player.sendMessage(Util.msg(msg));
    }

    public void teleportPlayer(World world, double x, double y, double z) {
        player.teleport(new Location(world, x, y, z));
    }

    public void teleportPlayer(Location l) {
        player.teleport(l);
    }

    public String getPlayerDisplayName() {
        return player.getDisplayName();
    }

    public void setDisplayName(@NotNull String name) {
        this.player.setDisplayName(Util.msg(name));
    }

    public UUID getPlayerID() {
        return uuid;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
