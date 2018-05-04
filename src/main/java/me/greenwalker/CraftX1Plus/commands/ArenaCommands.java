package me.floydz69.CraftX1Plus.commands;

import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.arena.ArenaManager;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.player.PlayerCache;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import me.floydz69.CraftX1Plus.util.enums.ArenaType;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Helper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gusta on 02/05/2017.
 */
public class ArenaCommands implements CommandExecutor {

    private final CraftPlus plugin;
    private Arena arena;
    private final ArenaManager arenaManager;
    private final ConfigHandler lang;

    public ArenaCommands(CraftPlus plugin, ArenaManager arenaManager, ConfigHandler lang) {
        this.plugin = plugin;
        this.arenaManager = arenaManager;
        this.lang = lang;
    }


    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("x1admin")) {
            if (!sender.hasPermission("craftx1.admin")) {
                sender.sendMessage(Util.msg(Util.getPrefix() + "&7Você não tem permissão."));
                return true;
            }

            Player player = (Player) sender;
            if (args == null || args.length == 0) {
                List<String> msgs = Arrays.asList("&7/x1admin criar <Nome> <Tipo>", "&7/x1admin setspawn", "&7/x1admin deletar <Nome>", "&7/x1admin <Nome> set", "&7/x1admin list", "&7/x1admin ajuda");
                for (String msg : msgs) {
                    player.sendMessage(Util.msg(Util.getPrefix() + msg));
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("setspawn")){
                Location l = player.getLocation();
                ConfigHandler config = CraftPlus.getMain().getLocations();
                config.set("Bsc.x", l.getBlockX());
                config.set("Bsc.y", l.getBlockY());
                config.set("Bsc.z", l.getBlockZ());
                config.set("Bsc.world", l.getWorld().getName());
                config.trySave();
                player.sendMessage(Util.msg("&aBsc (&cBug Spawn Counter&a) setado com sucesso."));
                return true;
            }

            if(!Util.isBscValidWorld(CraftPlus.getMain().getLocations())){
                player.sendMessage(lang.getStringCorrect("BscNaoValido"));
                player.sendMessage(Util.msg("&aPara setar, utilize &c/x1admin setspawn &aem um local válido (spawn)"));
            }

            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("criar")) {
                if (args.length == 3) {
                    String arena_name = args[1];
                    if (args[2].equalsIgnoreCase("CLAN") || args[2].equalsIgnoreCase("PLAYER") || args[2].equalsIgnoreCase("AMBOS")) {
                        ArenaType arenaType = ArenaType.fromName(args[2].toUpperCase());
                        if(arenaManager.createArena(arena_name)) {
                            arenaManager.getArena(arena_name).setArenaType(arenaType);
                            arenaManager.getArena(arena_name).setArenaStatus(ArenaStatus.DESATIVADA);
                            arenaManager.getArena(arena_name).setNome("CraftX1Plus");
                            arenaManager.getArena(arena_name).generateConfig();
                            player.sendMessage(Util.msg(lang.getStringReplaced("Arenas.ArenaCriada", "@Arena", arena_name, "@Prefix", Util.getPrefix())));
                            return true;
                        }else{
                            player.sendMessage(lang.getStringReplaced("Arenas.ArenaJaExiste", "@Arena", arena_name, "@Prefix", Util.getPrefix()));
                            return true;
                        }
                    } else {
                        player.sendMessage(Util.msg(Util.getPrefix() + "&7Tipos: &aClan&7, &aPlayer&7, &aAmbos"));
                        return true;
                    }
                } else {
                    player.sendMessage(Util.msg(Util.getPrefix() + "&7/x1admin " + args[0] + " <Nome> <tipo>"));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("deletar") || args[0].equalsIgnoreCase("delete")) {
                if (args.length == 2) {
                    String arena_name = args[1];
                    if (arenaManager.removeArena(arena_name)) {
                        player.sendMessage(Util.msg(lang.getStringReplaced("Arenas.ArenaRemovida", "@Arena", arena_name, "@Prefix", Util.getPrefix())));
                        return true;
                    } else {
                        player.sendMessage(Util.msg(Util.getPrefix() + "&a" + args[1] + "&7 não existe."));
                        return true;
                    }
                } else {
                    player.sendMessage(Util.msg(Util.getPrefix() + "&7/X1admin " + args[0] + " <arena>"));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(Util.msg("&7Arena : Nome : Status"));
                for (Arena ar : arenaManager.getArenas()) {
                    player.sendMessage(Util.msg("&a" + ar.getArena() + " &7:&c " + ar.getNome() + " &7: &c" + ar.getArenaStatus().getType()));
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("ajuda")){
                List<String> str = Arrays.asList("Pos1", "Pos2", "Camarote", "Saida", "Espera", "Nome", "Tipo", "Aberta", "Fechada");
                player.sendMessage(Util.msg(Util.getPrefix() + "&7/x1admin <arena> set &cPos1"));
                str.forEach(s -> player.sendMessage(Util.msg(Util.getPrefix() + "&7/x1admin <arena> set &c%s".replaceAll("%s", s))));
                return true;
            }
            if(args[0].equalsIgnoreCase("debugg")){
                for(Player name : PlayerX1Helper.getIniciando()){
                    player.sendMessage("Iniciando -> " + name.getName());
                }
                for(Player p : PlayerX1Helper.getX1Handlers().keySet()){
                    player.sendMessage("X1 -> " + p.getName() + " x " + PlayerX1Helper.getX1Handlers().get(p).getP2().getName());
                }
                for(Player p : PlayerX1Helper.getInGame().keySet()){
                    player.sendMessage("Na Arena -> " + p.getName() + " vs " + PlayerX1Helper.getInGame().get(p).getName());
                }
                for(Player p : PlayerX1Helper.getPendentes().keySet()){
                    player.sendMessage("Pendente -> " + p.getName() + " waiting " + PlayerX1Helper.getPendentes().get(p).getName());
                }
//                for(PlayerCache p : CraftPlus.getMain().playerManager.getPlayerCacheList().values()){
//                    player.sendMessage("registrado -> " + p.getPlayer_name());
//                }
                return true;
            }
            try {
                if (arenaManager.contains(args[0])) {
                    if (args.length == 3 || args.length == 4) {
                        Arena arena = arenaManager.getArena(args[0]);
                        if (args[1].equalsIgnoreCase("set")) {
                            String set_param = args[2].toLowerCase();
                            if (set_param.equalsIgnoreCase("pos1")) {
                                if (arena.getPos2() == null || arena.getPos2().getWorld() == null) { // está com algum mundo?
                                    arena.setPos1(player.getLocation());
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setPos1Config()));
                                    return true;
                                } else if (arena.getPos2().getWorld().getName().equals(player.getWorld().getName())) { // sim, verificar se o atual é o mesmo que está na config
                                    arena.setPos1(player.getLocation());
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setPos1Config()));
                                    return true;
                                } else {
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7O mundo atual para esta arena não corresponde com a outra posição já definida."));
                                    return true;
                                }
                            } else if (set_param.equalsIgnoreCase("pos2")) {
                                if (arena.getPos1() == null || arena.getPos1().getWorld() == null) { // está com algum mundo?
                                    arena.setPos2(player.getLocation());
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setPos2Config()));
                                    return true;
                                } else if (arena.getPos1().getWorld().getName().equals(player.getWorld().getName())) { // sim, verificar se o atual é o mesmo que está na config
                                    arena.setPos2(player.getLocation());
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setPos2Config()));
                                    return true;
                                } else {
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7O mundo atual para esta arena não corresponde com a outra posição já definida."));
                                    return true;
                                }
                            } else if (set_param.equalsIgnoreCase("saida")) {
                                arena.setSaida(player.getLocation());
                                player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setSaidaConfig()));
                                return true;
                            } else if (set_param.equalsIgnoreCase("espera")) {
                                arena.setEspera(player.getLocation());
                                player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setEsperaConfig()));
                                return true;
                            } else if (set_param.equalsIgnoreCase("camarote")) {
                                arena.setCamarote(player.getLocation());
                                player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setCamaroteConfig()));
                                return true;
                            } else if (set_param.equalsIgnoreCase("nome")) {
                                String name_replaced = args[3].replaceAll("&", "§").replaceAll("_", " ");
                                arena.setNome(name_replaced);
                                player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setNomeConfig()));
                                return true;
                            } else if (set_param.equalsIgnoreCase("aberta") || set_param.equalsIgnoreCase("open")) {
                                if(!arena.isFinished()){
                                    player.sendMessage(Util.msg(Util.getPrefix() + "§7Você não pode abrir uma arena não finalizada."));
                                    return true;
                                }
                                if (!(arena.getArenaStatus() == ArenaStatus.ABERTO)) {
                                    arena.setArenaStatus(ArenaStatus.ABERTO);
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setStatusConfig()));
                                    return true;
                                } else {
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&a" + arena.getArena() + " &7Já está aberta!"));
                                    return true;
                                }
                            } else if (set_param.equalsIgnoreCase("fechada") || set_param.equalsIgnoreCase("closed")) {
                                if (!(arena.getArenaStatus() == ArenaStatus.DESATIVADA)) {
                                    arena.setArenaStatus(ArenaStatus.DESATIVADA);
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&7" + arena.setStatusConfig()));
                                    return true;
                                } else {
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&a" + arena.getArena() + " &7Já está fechada!"));
                                    return true;
                                }
                            } else {
                                player.sendMessage(Util.msg(Util.getPrefix() + "&a" + set_param + "&7 não foi reconhecido como um sub-comando!\n&c/x1adm ajuda &7para obter a lista de comandos."));
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage(Util.msg(Util.getPrefix() + "&7/x1admin " + args[0] + " set"));
                        return true;
                    }
                } else {
                    player.sendMessage(Util.msg(Util.getPrefix() + "&a" + args[0] + "&7 não é uma arena."));
                    return true;
                }
            } catch (NullPointerException | ArrayIndexOutOfBoundsException err) {
                player.sendMessage(Util.msg(Util.getPrefix() + "&7Falta de argumentos!, caso ache que é um erro contate Floydz69"));
                return true;
            }
        }
        return false;
    }
}
