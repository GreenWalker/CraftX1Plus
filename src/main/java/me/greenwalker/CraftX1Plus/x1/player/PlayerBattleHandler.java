package me.floydz69.CraftX1Plus.x1.player;

import org.bukkit.OfflinePlayer;

/**
 * Created by gusta on 20/05/2017.
 */
public class PlayerBattleHandler {

    private OfflinePlayer contra;
    private Boolean venceu;
    private String data;
    private int id;
    private String tipo;

    public PlayerBattleHandler(OfflinePlayer contra, Boolean venceu, String data, String tipo) {
        this.contra = contra;
        this.venceu = venceu;
        this.data = data;
        this.tipo = tipo;
    }

    public OfflinePlayer getContra() {
        return contra;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContra(OfflinePlayer contra) {
        this.contra = contra;
    }

    public Boolean getVenceu() {
        return venceu;
    }

    public void setVenceu(Boolean venceu) {
        this.venceu = venceu;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
