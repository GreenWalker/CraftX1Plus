package me.floydz69.CraftX1Plus.x1.player;

import me.floydz69.CraftX1Plus.player.CraftPlusPlayer;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;

import me.floydz69.CraftX1Plus.x1.X1Utils;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by gusta on 08/05/2017.
 */
public class PlayerX1Helper extends X1Utils {

    private static Map<Player, PlayerX1Handler> x1Handlers;
    private static Map<Player, Player> inGame;
    private static Map<Player, Player> pendente;
    private static Set<Player> espera;
    private static Set<Player> saindo;
    private static Set<Player> iniciando;

    /*

        Utils

     */

    // x1 handler

    public static PlayerX1Handler getPlayerHandler(Player player) {
        return getX1Handlers().get(player);
    }

    public static void addX1Handler(Player player, PlayerX1Handler handler) {
        getX1Handlers().put(player, handler);
    }

    public static void removeX1Handler(Player player) {
        getX1Handlers().remove(player);
    }

    public static boolean containsX1Handler(Player handler) {
        return getX1Handlers().containsKey(handler);
    }

    // inGame

    /**
     * @param player  desafiador
     * @param player2 desafiado
     */
    public static void addInGame(Player player, Player player2) {
        getInGame().put(player, player2);
    }

    public static void removeInGame(Player player) {
        getInGame().remove(player);
    }

    public static boolean isInGame(Player player) {
        return getInGame().containsKey(player) || getInGame().containsValue(player);
    }

    //Pendentes

    /**
     * @param player  desafiado
     * @param player2 desafiador
     */

    public static void addInPendente(Player player, Player player2) {
        getPendentes().put(player, player2);
    }

    public static void removePendente(Player player) {
        getPendentes().remove(player);
    }

    public static boolean isPendente(Player player) {
        return getPendentes().containsKey(player) || getPendentes().containsValue(player);
    }

    //Espera

    public static void addPlayerToEspera(CraftPlusPlayer player) {
        getEspera().add(player.getPlayer());
    }

    public static void removePlayerToEspera(CraftPlusPlayer obj) {
        if (!getEspera().contains(obj.getPlayer())) {
            return;
        }
        getEspera().remove(obj.getPlayer());
    }

    public static boolean containsInEspera(Player player) {
        return getEspera().contains(player);
    }

    //Iniciando

    public static void addToIniciando(Player player) {
        getIniciando().add(player);
    }

    public static void removeToIniciando(Player player) {
        getIniciando().remove(player);
    }

    public static boolean iniciandoContains(Player player) {
        return getIniciando().contains(player);
    }

    //Saindo

    public static void addToSaindo(Player player) {
        getSaindo().add(player);
    }

    public static void removeToSaindo(Player player) {
        getSaindo().remove(player);
    }

    public static boolean containsPlayerInSaindo(Player player) {
        return getSaindo().contains(player);
    }

    /*

        Getter's and Setter's

     */

    public static Set<Player> getIniciando() {
        return iniciando;
    }

    public static void setIniciando(Set<Player> iniciando) {
        PlayerX1Helper.iniciando = iniciando;
    }

    public static Map<Player, PlayerX1Handler> getX1Handlers() {
        return x1Handlers;
    }

    public static void setX1Handlers(Map<Player, PlayerX1Handler> x1Handlers) {
        PlayerX1Helper.x1Handlers = x1Handlers;
    }

    public static Map<Player, Player> getInGame() {
        return inGame;
    }

    public static void setInGame(Map<Player, Player> inGame) {
        PlayerX1Helper.inGame = inGame;
    }

    public static Set<Player> getEspera() {
        return espera;
    }

    public static void setEspera(Set<Player> iniciando) {
        PlayerX1Helper.espera = iniciando;
    }

    public static Set<Player> getSaindo() {
        return saindo;
    }

    public static void setSaindo(Set<Player> saindo) {
        PlayerX1Helper.saindo = saindo;
    }

    public static Map<Player, Player> getPendentes() {
        return pendente;
    }

    public static void setPendente(Map<Player, Player> pendente) {
        PlayerX1Helper.pendente = pendente;
    }

    public static void cancelX1(Player player) {
        getX1Handlers().get(player).getArena().setArenaStatus(ArenaStatus.ABERTO);
        Player p2 = getX1Handlers().get(player).getP2();
        getSaindo().remove(player);
        getSaindo().remove(p2);
        getInGame().remove(player);
        getPendentes().remove(player);
        getX1Handlers().remove(player);
    }
}
