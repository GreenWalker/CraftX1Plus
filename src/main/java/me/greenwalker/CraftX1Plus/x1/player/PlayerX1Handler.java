package me.floydz69.CraftX1Plus.x1.player;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.arena.ArenaManager;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.player.Desafiado;
import me.floydz69.CraftX1Plus.player.Desafiador;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.util.enums.X1Type;

import me.floydz69.CraftX1Plus.x1.X1Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;

import static me.floydz69.CraftX1Plus.x1.X1Utils.enviaItems;

public class PlayerX1Handler {

    private Player p1;
    private Player p2;
    private Desafiado desafiado;
    private Desafiador desafiador;
    private Arena arena;
    private CraftPlus main;
    private X1Type x1Type;
    private double x1Custo;
    private ConfigHandler lang;
    private BukkitTask taskX1;
    private DatabaseUtil databaseUtil;
    private boolean iniciou;

    public PlayerX1Handler(Player p1, Player p2, String arena_name, ArenaManager arenaManager, CraftPlus main, X1Type x1Type, ConfigHandler lang, DatabaseUtil databaseUtil) {
        this.p1 = p1;
        this.p2 = p2;
        this.main = main;
        this.arena = arenaManager.getByName(arena_name);
        this.x1Type = x1Type;
        this.lang = lang;
        this.databaseUtil = databaseUtil;
        this.iniciou = false;

    }

    public boolean isIniciou() {
        return iniciou;
    }

    public void setIniciou(boolean iniciou) {
        this.iniciou = iniciou;
    }

    public void iniciarX1() {
        long tempoDuracao = 0;
        if (getX1Type().equals(X1Type.NORMAL)) {
            tempoDuracao = main.getPConfig().getInt("Normal");
        } else if (getX1Type().equals(X1Type.HARD)) {
            tempoDuracao = main.getPConfig().getInt("Hard");
        } else if (getX1Type().equals(X1Type.INSANO)) {
            tempoDuracao = 259200;

        }
        desafiado.registrar();
        desafiador.registrar();
        if (desafiador.removeBalance(x1Custo) && desafiado.removeBalance(x1Custo)) {
            String p = lang.getStringCorrect("Player.X1Iniciou");
            arena.setArenaStatus(ArenaStatus.EM_JOGO);
            new BukkitRunnable() {
                public void run() {
                    PlayerX1Helper.removePlayerToEspera(desafiador);
                    PlayerX1Helper.removePlayerToEspera(desafiado);
                    cancel();
                }
            }.runTaskLater(main, 5 * 20L);
            setIniciou(true);
            desafiado.sendPlayerMessage(p);
            desafiado.teleportPlayer(arena.getPos2());
            desafiador.sendPlayerMessage(p);
            desafiador.teleportPlayer(arena.getPos1());
            taskX1 = new BukkitRunnable() {
                @Override
                public void run() {
                    terminarX1();
                }
            }.runTaskLater(main, tempoDuracao * 20L);
        } else {
            String s = lang.getStringReplaced("Player.Error5", "@Prefix", Util.getPrefix());

            PlayerX1Helper.removePlayerToEspera(desafiador);
            PlayerX1Helper.removePlayerToEspera(desafiado);

            desafiado.sendPlayerMessage(s);
            desafiado.teleportPlayer(arena.getSaida());

            desafiador.sendPlayerMessage(s);
            desafiador.teleportPlayer(arena.getSaida());
            PlayerX1Helper.cancelX1(desafiador.getPlayer());
        }
    }

    public void terminarX1() {
        desafiado.depositPlayer(x1Custo / 2);
        desafiador.depositPlayer(x1Custo / 2);

        desafiado.teleportPlayer(arena.getSaida());
        desafiador.teleportPlayer(arena.getSaida());

        String msg = lang.getStringReplaced("Player.X1SemVencedor", "@Desafiador", desafiador.getPlayerName(), "@Desafiado", desafiado.getPlayerName(), "@Prefix", Util.getPrefix());
        main.getServer().getOnlinePlayers().forEach((Player p) -> p.sendMessage(msg));
        arena.setArenaStatus(ArenaStatus.ABERTO);
        getTaskX1().cancel();
        PlayerX1Helper.cancelX1(desafiador.getPlayer());
    }

    /**
     * @param p     matar este jogador
     * @param drops items que serão dropados apos a morte do jogador
     * @param inv   inventario para qual irá os itens dropados
     */

    public void killPlayer(Player p, List<ItemStack> drops, Inventory inv) {
        if (p.getName().equals(desafiador.getPlayerName())) {
            PlayerX1Helper.addToSaindo(desafiado.getPlayer());
            getDesafiado().addVitoria(1);
            getDesafiador().addDerrota(1);
            getDesafiador().addBatalha(getDesafiado().getPlayerID(), false, getX1Type().toString());

            getDesafiado().depositPlayer(getX1Custo() * 2);
            getDesafiado().sendPlayerMessage(lang.getStringCorrect("Player.PremioRecebido"));
            getDesafiado().teleportPlayer(getArena().getSaida());

            enviaItems(getDesafiador().getPlayer(), inv, drops, lang);

            String msg = lang.getStringReplaced("GanhouX1", "@Winner", getDesafiado().getPlayerName(), "@Loser", getDesafiador().getPlayerName(), "@Quantia", Util.formatAnotherString(getX1Custo()));
            main.getServer().getOnlinePlayers().forEach(d -> d.sendMessage(msg));
            arena.setArenaStatus(ArenaStatus.ABERTO);

            getTaskX1().cancel();
            PlayerX1Helper.cancelX1(getDesafiador().getPlayer());
        } else if (p.getName().equals(desafiado.getPlayerName())) {
            PlayerX1Helper.addToSaindo(desafiador.getPlayer());
            getDesafiador().addVitoria(1);

            getDesafiado().addDerrota(1);

            getDesafiador().addBatalha(getDesafiado().getPlayerID(), true, getX1Type().toString());

            getDesafiador().depositPlayer(getX1Custo() * 2);
            getDesafiador().sendPlayerMessage(lang.getStringCorrect("Player.PremioRecebido"));
            getDesafiador().teleportPlayer(getArena().getSaida());

            enviaItems(getDesafiador().getPlayer(), inv, drops, lang);

            String msg = lang.getStringReplaced("GanhouX1", "@Winner", getDesafiador().getPlayerName(), "@Loser", getDesafiado().getPlayerName(), "@Quantia", Util.formatAnotherString(getX1Custo()));
            main.getServer().getOnlinePlayers().forEach(d -> d.sendMessage(msg));
            arena.setArenaStatus(ArenaStatus.ABERTO);

            getTaskX1().cancel();
            PlayerX1Helper.cancelX1(getDesafiador().getPlayer());
        }
    }

    public void enviaSalaEspera() {
        PlayerX1Helper.addPlayerToEspera(desafiador);
        PlayerX1Helper.addPlayerToEspera(desafiado);
        desafiado.sendPlayerMessage(lang.getStringReplaced("Player.SalaDeEspera", "@Prefix", Util.getPrefix()));
        desafiador.sendPlayerMessage(lang.getStringReplaced("Player.SalaDeEspera", "@Prefix", Util.getPrefix()));
        desafiado.teleportPlayer(arena.getEspera());
        desafiador.teleportPlayer(arena.getEspera());
        new BukkitRunnable() {
            @Override
            public void run() {
                iniciarX1();
                cancel();
            }
        }.runTaskLater(main, main.getPConfig().getInt("TempoEspera") * 20L);
    }

    // Getter's and Setter's

    public BukkitTask getTaskX1() {
        return taskX1;
    }

    public X1Type getX1Type() {
        return x1Type;
    }

    public void setX1Type(X1Type x1Type) {
        this.x1Type = x1Type;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public Desafiado getDesafiado() {
        return desafiado;
    }

    public void setDesafiado(Player desafiado) {
        this.desafiado = new Desafiado(desafiado, main, databaseUtil);
    }

    public Desafiador getDesafiador() {
        return desafiador;
    }

    public void setDesafiador(Player desafiador) {
        this.desafiador = new Desafiador(desafiador, main, databaseUtil);
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public double getX1Custo() {
        return x1Custo;
    }

    public void setX1Custo(double x1Custo) {
        this.x1Custo = x1Custo;
    }
}
