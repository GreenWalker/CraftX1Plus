package me.floydz69.CraftX1Plus.clan;

import net.sacredlabyrinth.phaed.simpleclans.Clan;

import java.util.Map;

/**
 * Created by gusta on 18/05/2017.
 */
public class ClanCache {

    private Clan clan;
    private int clan_vitorias;
    private int clan_derrotas;
    private Map<String, Boolean> clan_batalhas;

    public ClanCache(Clan clan, int clan_vitorias, int clan_derrotas, Map<String, Boolean> clan_batalhas) {
        this.clan = clan;
        this.clan_vitorias = clan_vitorias;
        this.clan_derrotas = clan_derrotas;
        this.clan_batalhas = clan_batalhas;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public int getClan_vitorias() {
        return clan_vitorias;
    }

    public void setClan_vitorias(int clan_vitorias) {
        this.clan_vitorias = clan_vitorias;
    }

    public int getClan_derrotas() {
        return clan_derrotas;
    }

    public void setClan_derrotas(int clan_derrotas) {
        this.clan_derrotas = clan_derrotas;
    }

    public Map<String, Boolean> getClan_batalhas() {
        return clan_batalhas;
    }

    public void setClan_batalhas(Map<String, Boolean> clan_batalhas) {
        this.clan_batalhas = clan_batalhas;
    }
}
