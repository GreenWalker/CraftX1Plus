package me.floydz69.CraftX1Plus.player;

import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerCache {

    private OfflinePlayer player;
    private String player_name;
    private int player_derrotas;
    private int player_vitorias;
    private int player_bau;
    private List<PlayerBattleHandler> batalhas;
    private Inventory inventory;

    public PlayerCache(OfflinePlayer player, int player_vitorias, int player_derrotas, int player_bau, List<PlayerBattleHandler> batalhas) {
        this.player = player;
        this.player_name = player.getName();
        this.player_derrotas = player_derrotas;
        this.player_vitorias = player_vitorias;
        this.player_bau = player_bau;
        this.batalhas = batalhas;
        this.inventory = null;
        if(player_bau != 0) {
            this.inventory = Bukkit.createInventory(player.getPlayer(), Util.convertChestSize(player_bau), "Depósito");
        }
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public List<PlayerBattleHandler> getBatalhas() {
        return batalhas;
    }

    public void setBatalhas(List<PlayerBattleHandler> batalhas) {
        this.batalhas = batalhas;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public void setPlayer_derrotas(int player_derrotas) {
        this.player_derrotas = player_derrotas;
    }

    public void setPlayer_vitorias(int player_vitorias) {
        this.player_vitorias = player_vitorias;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setPlayer_bau(int player_bau) {
        this.player_bau = player_bau;
        updateInventory();
    }

    public int getPlayer_bau() {
        return player_bau;
    }

    public int getPlayer_derrotas() {
        return player_derrotas;
    }

    public int getPlayer_vitorias() {
        return player_vitorias;
    }

    private void updateInventory() {
            if (player_bau != 0) {
                int item = 0;
                if(getInventory() != null) {
                    for (int i = 0; i < getInventory().getSize(); ++i) {
                        if (getInventory().getItem(i) != null) {
                            if (!getInventory().getItem(i).getType().equals(Material.AIR)) {
                                item++;
                            }
                        }
                    }
                }
                if (item > 0) {
                    List<ItemStack> items = Arrays.asList(getInventory().getContents());
                    setInventory(Bukkit.createInventory(player.getPlayer(), Util.convertChestSize(player_bau), "Depósito"));
                    items.stream().filter(Objects::nonNull).forEach(s -> getInventory().addItem(s));
                } else {
                    setInventory(Bukkit.createInventory(player.getPlayer(), Util.convertChestSize(player_bau), "Depósito"));
                }
            }
    }
}
