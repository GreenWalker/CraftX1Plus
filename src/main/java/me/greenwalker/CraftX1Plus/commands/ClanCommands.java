package me.floydz69.CraftX1Plus.commands;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.party.ClanParty;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.x1.clan.ClanX1Helper;
import me.floydz69.CraftX1Plus.x1.clan.ClanX1Handler;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by gusta on 02/05/2017.
 */
public class ClanCommands implements CommandExecutor {

    private CraftPlus main;
    private ClanManager cm;
    private DatabaseUtil dbutil;
    private ConfigHandler lang;
    private ClanX1Helper x1;
    private Map<String, ClanPlayer> pendente;

    public ClanCommands(CraftPlus plugin, ClanManager cm, DatabaseUtil dbutil, ConfigHandler lang) {
        this.main = plugin;
        ;
        this.cm = cm;
        this.dbutil = dbutil;
        this.lang = lang;
        this.x1 = ClanX1Helper.getInstance();
        this.pendente = new HashMap<>();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (!cmd.getName().equalsIgnoreCase("x1clan")) {
            return true;
        }
        // has clan
        if (cm.getClanPlayer(player) == null || cm.getClanPlayer(player).getClan() == null) {
            player.sendMessage(lang.getStringReplaced("Clan.SemClan", "@Prefix", Util.getPrefix()));
            return true;
        }

        if(args == null || args.length == 0){
            return true;
        }

        /*
            /x1clan desafiar

         */
        if (args[0].equalsIgnoreCase("desafiar") || args[0].equalsIgnoreCase("duel")) {
            if (args.length != 2) {
                player.sendMessage(Util.msg("&c/x1clan desafiar {tag}"));
                return true;
            }
            Clan desafiador = cm.getClanPlayer(player).getClan();
            Clan desafiado = cm.getClan(args[1]);
            String tag = desafiador.getTag();
            if(desafiado == null){
                player.sendMessage(Util.msg("&cTag '&7" + args[1] + "&c' não está relacionada a nenhum clan (&acase-sensitive&c)"));
                return true;
            }
            if (desafiado.getMembers().size() < main.getPConfig().getInt("MaxPlayers")) {
                player.sendMessage(Util.msg("&cEste clan não possui o número minimo de players para iniciar um x1"));
                return true;
            }
            if (x1.getClanParty(tag) == null) {
                player.sendMessage(Util.msg("&cSeu clan não possui uma party."));
                return true;
            }
            if(x1.getClanParty(desafiado.getTag()) == null){
                player.sendMessage(Util.msg("&cO clan adversário não possui uma party."));
                return true;
            }
            x1.addHandler(new ClanX1Handler(main, lang, desafiador, desafiado, null, dbutil));
            ClanX1Handler handler = x1.getHandler(tag);
            if (handler.getDesafiado() == null) {
                player.sendMessage(lang.getStringReplaced("Clan.NaoExiste", "@Prefix", Util.getPrefix(), "@Clan", args[1]));
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("aceitar") || args[0].equalsIgnoreCase("accept")) {

        }

        if (args[0].equalsIgnoreCase("rejeitar") || args[0].equalsIgnoreCase("reject")) {

        }

        if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {

        }
        if (args[0].equalsIgnoreCase("party")) {
            if (args.length == 1){
                player.sendMessage(Util.msg("&3=---------------- &2&lLista de Comandos &3----------------="));
                player.sendMessage(Util.msg(" &b - /x1clan party aceitar"));
                player.sendMessage(Util.msg(" &b - /x1clan party sair"));
                if(cm.getClanPlayer(player).isLeader()) {
                    player.sendMessage(Util.msg(" &b - /x1clan party list"));
                    player.sendMessage(Util.msg(" &b - /x1clan party add <player>"));
                    player.sendMessage(Util.msg(" &b - /x1clan party remove <player>"));
                }
                player.sendMessage(Util.msg("&3----------------------------------------------------- "));
                return true;
            }
            if (args[1].equalsIgnoreCase("listar") || args[1].equalsIgnoreCase("list")) {
                if (args.length != 2) {
                    player.sendMessage(Util.msg("&c/x1clan party listar"));
                    return true;
                }

                Clan clan = cm.getClanPlayer(player).getClan();
                if (x1.getClanParty(clan.getTag()) == null) {
                    player.sendMessage(Util.msg("&cSeu clan ainda não possui nenhuma party."));
                    return true;
                }
                Set<ClanPlayer> pl = x1.getClanParty(clan.getTag()).getPlayers();
                int i = x1.getClanParty(clan.getTag()).getMaxMembers();

                if (!cm.getClanPlayer(player).isLeader()) {
                    player.sendMessage(lang.getStringReplaced("Clan.PlayerNaoELider", "@Prefix", Util.getPrefix()));
                    return true;
                }
                if (!x1.containsInClanParty(clan.getTag())) {
                    player.sendMessage(Util.msg("&cSeu clan não tem nenhuma party."));
                    return true;
                }
                player.sendMessage(Util.msg("&3=--------------[&6-&3] &b&lLista De Membros &3[&6-&3]----------------="));
                player.sendMessage(Util.msg("&bMembros: &7" + (pl.size() == i ? "&b" + pl.size() : "&7" + pl.size()) + " &4/ &b" + i));
                pl.forEach(s ->
                    player.sendMessage(Util.msg("&3    [&7-&3] &b " + s.getName()))
                );
                player.sendMessage(Util.msg("&3----------------------------------------------------- "));
                return true;
            }
            if (args[1].equalsIgnoreCase("aceitar") || args[1].equalsIgnoreCase("accept")) {
                if (args.length != 2) {
                    player.sendMessage(Util.msg("&c/x1clan party aceitar"));
                    return true;
                }
                ClanPlayer player2 = cm.getClanPlayer(player);
                if (!pendente.containsValue(player2)) {
                    player.sendMessage(Util.msg("&cNinguém te convidou para uma party."));
                    return true;
                }
                Clan clan = player2.getClan();
                if (!x1.containsInClanParty(clan.getTag())) {
                    player.sendMessage(Util.msg("&cSeu clan não tem nenhuma party."));
                    return true;
                }
                player.sendMessage(x1.getClanParty(clan.getTag()).adcionaPlayer(cm.getClanPlayer(player)));
                return true;
            }
            if (args[1].equalsIgnoreCase("sair") || args[1].equalsIgnoreCase("quit")) {
                if (args.length != 2) {
                    player.sendMessage(Util.msg("&c/x1clan party sair"));
                    return true;
                }
                ClanPlayer player2 = cm.getClanPlayer(player);
                Clan clan = player2.getClan();

                if (!x1.containsInClanParty(clan.getTag())) {
                    player.sendMessage(Util.msg("&cSeu clan não tem nenhuma party."));
                    return true;
                }

                if (!pendente.containsValue(player2)) {
                    player.sendMessage(Util.msg("&cVocê não está em nenhuma party."));
                    return true;
                }
                player.sendMessage(x1.getClanParty(clan.getTag()).removePlayer(cm.getClanPlayer(player)));
                return true;
            }
        }

        if (args[1].equalsIgnoreCase("adcionar") || args[1].equalsIgnoreCase("add")) {
            if (args.length == 3) {
                if (Util.isPlayer(args[2])) {
                    Player target = Bukkit.getPlayerExact(args[2]);
                    if (!cm.getClanPlayer(player).isLeader()) {
                        player.sendMessage(lang.getStringReplaced("Clan.PlayerNaoELider", "@Prefix", Util.getPrefix()));
                        return true;
                    }
                    Clan clan = cm.getClanPlayer(player).getClan();
                    if (!x1.containsInClanParty(clan.getTag())) {
                        x1.addClanParty(new ClanParty(main, clan));
                    }
                    if (target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(x1.getClanParty(clan.getTag()).adcionaPlayer(cm.getClanPlayer(player)));
                        return true;
                    }
                    if (pendente.containsKey(clan.getTag())) {
                        player.sendMessage(Util.msg("&cJá possui um convite pendente! aguarde até que seja aceito ou cancelado."));
                        return true;
                    }
                    ClanPlayer ctarget = cm.getClanPlayer(target);
                    if(ctarget == null || ctarget.getClan() == null){
                        player.sendMessage(Util.msg("&f" + target.getName() + "&e não é membro de nenhum clan."));
                        return true;
                    }
                    if(!clan.isMember(ctarget.getUniqueId())){
                        player.sendMessage(Util.msg("&f" + target.getName() + "&e não é membro do seu clan."));
                        return true;
                    }
                    pendente.put(clan.getTag(), cm.getClanPlayer(target));
                    target.playSound(target.getLocation(), Sound.NOTE_PLING, 1F, 1.5F);
                    target.sendMessage(Util.msg(Util.getPrefix() + "&cVocê recebeu um convite para entrar na party do clan! utilize &e/x1clan party aceitar&c. você tem &e30&c segundos para aceitar."));
                    new BukkitRunnable() {
                        public void run() {
                            if (pendente.containsKey(clan.getTag())) {
                                pendente.remove(clan.getTag());
                            }
                            cancel();
                        }
                    }.runTaskLater(main, 30 * 20L);
                } else {
                    player.sendMessage(lang.getStringReplaced("Clan.PlayerOffline", "@Prefix", Util.getPrefix()));
                }
                return true;
            }
        }
        if (args[1].equalsIgnoreCase("remover") || args[1].equalsIgnoreCase("remove")) {
            if (args.length != 3) {
                player.sendMessage(Util.msg("&c/x1clan party " + args[1] + " <player>"));
                return true;
            }
                if (!Util.isPlayer(args[2])) {
                    player.sendMessage(Util.msg("&ePlayer &f" + args[2] + "&e não existe ou não está online."));
                    return true;
                }
                    Player target = Bukkit.getPlayerExact(args[2]);
                    if (!cm.getClanPlayer(player).isLeader()) {
                        player.sendMessage(lang.getStringReplaced("Clan.PlayerNaoELider", "@Prefix", Util.getPrefix()));
                        return true;
                    }
                    Clan clan = cm.getClanPlayer(target).getClan();
                    if (!x1.containsInClanParty(clan.getTag())) {
                        player.sendMessage(Util.msg("&cSeu clan não tem nenhuma party."));
                        return true;
                    }
                    player.sendMessage(x1.getClanParty(clan.getTag()).removePlayer(cm.getClanPlayer(target)));
                    return true;
            }
        return false;
    }
}
