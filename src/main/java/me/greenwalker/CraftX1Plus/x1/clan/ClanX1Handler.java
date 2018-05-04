package me.floydz69.CraftX1Plus.x1.clan;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.clan.Object.Desafiado;
import me.floydz69.CraftX1Plus.clan.Object.Desafiador;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.party.ClanParty;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.Util;
import net.sacredlabyrinth.phaed.simpleclans.Clan;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


/**
 * Created by gusta on 18/05/2017.
 */
public class ClanX1Handler {

    private CraftPlus main;
    private Desafiado desafiado;
    private Desafiador desafiador;
    private boolean iniciou;
    private ConfigHandler language;
    private BukkitTask taskx1;
    private Arena arena;
    private DatabaseUtil databaseUtil;
    private ClanX1Helper x1;


    public ClanX1Handler(CraftPlus main , ConfigHandler language, Clan desafiador, Clan desafiado, Arena arena, DatabaseUtil databaseUtil) {
        this.main = main;
        this.language = language;
        this.arena = arena;
        this.databaseUtil = databaseUtil;
        this.desafiador = new Desafiador(main, desafiador, databaseUtil);
        this.desafiado = new Desafiado(main, desafiado ,databaseUtil);
        this.x1 = ClanX1Helper.getInstance();
    }


    public void iniciarX1(){
        this.desafiado.registrar();
        this.desafiador.registrar();
        this.x1.getClanParty(desafiado.getClanTag()).getPlayers().forEach(s -> {s.toPlayer().sendMessage(getLanguage().getStringReplaced("Player.X1Iniciou", "@Prefix", Util.getPrefix())); s.toPlayer().teleport(getArena().getPos1());});
        this.x1.getClanParty(desafiador.getClanTag()).getPlayers().forEach(s -> {s.toPlayer().sendMessage(getLanguage().getStringReplaced("Player.X1Iniciou", "@Prefix", Util.getPrefix())); s.toPlayer().teleport(getArena().getPos2());});
        taskx1 = new BukkitRunnable(){
            @Override
            public void run() {
                terminarX1();
            }
        }.runTaskLater(main, 300L);
    }

    public void enviaSalaEspera(){
        boolean iniciarMinPlayer = main.getPConfig().getBoolean("Iniciar-Sem-Max");
        int max = main.getPConfig().getInt("MaxPlayers");
//        if(!iniciarMinPlayer){
//            if(desafiador.getPlayers().size() < max || desafiado.getPlayers().size() < max) {
//                String msg = getLanguage().getStringReplaced("Clan.SemPlayerSuficiente", "@Prefix", Util.getPrefix());
//                desafiador .getPlayers().forEach(s -> s.toPlayer().sendMessage(msg));
//                desafiado.getPlayers().forEach(s -> s.toPlayer().sendMessage(msg));
//                return;
//            }
//        }
    }

    public void terminarX1(){

    }
    /*

     //  @Getters
     //  @Setters

     */

    public boolean isIniciou() {
        return iniciou;
    }

    public void setIniciou(boolean iniciou) {
        this.iniciou = iniciou;
    }

    public ConfigHandler getLanguage() {
        return language;
    }

    public void setLanguage(ConfigHandler language) {
        this.language = language;
    }

    public BukkitTask getTaskx1() {
        return taskx1;
    }

    public void setTaskx1(BukkitTask taskx1) {
        this.taskx1 = taskx1;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public Desafiado getDesafiado() {
        return desafiado;
    }

    public Desafiador getDesafiador() {
        return desafiador;
    }
}
