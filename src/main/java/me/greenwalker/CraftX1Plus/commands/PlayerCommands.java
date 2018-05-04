package me.floydz69.CraftX1Plus.commands;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.arena.ArenaManager;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.menu.GuiManager;
import me.floydz69.CraftX1Plus.menu.MenuHelper;
import me.floydz69.CraftX1Plus.menu.MenuType;
import me.floydz69.CraftX1Plus.player.PlayerCache;
import me.floydz69.CraftX1Plus.player.PlayerManager;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import me.floydz69.CraftX1Plus.util.enums.X1Type;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Handler;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Helper;

import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommands implements CommandExecutor, Listener {

    private CraftPlus pl;
    private final ArenaManager arenaManager;
    private GuiManager guiManager;
    private ConfigHandler lang;
    private PlayerManager ps;
    private ClanManager cm;
    private DatabaseUtil databaseUtil;

    public PlayerCommands(CraftPlus pl, ArenaManager arenaManager, GuiManager guiManager, ConfigHandler lang, PlayerManager ps, ClanManager cm, DatabaseUtil databaseUtil) {
        this.arenaManager = arenaManager;
        this.pl = pl;
        this.guiManager = guiManager;
        this.lang = lang;
        this.ps = ps;
        this.cm = cm;
        this.databaseUtil = databaseUtil;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        double custo;
        if (cmd.getName().equalsIgnoreCase("x1")) {
            if (args.length == 0) {
                MenuHelper.addMenuView(player, MenuType.ARENA_INFO);
                guiManager.loadMenuArenas(player, lang.getStringCorrect("Menus.ArenasInfo"));
                return true;
            }
        }

        /*
         * comando /x1 desafiar player
         *
         */
        if (args[0].equalsIgnoreCase("desafiar")) {
            if (args.length == 3) {
                custo = pl.getPConfig().getDouble("x1-Custo");
            } else if (args.length == 4) {
                if (Util.isNumber(args[3])) {
                    if (Integer.parseInt(args[3]) > pl.getPConfig().getDouble("x1-Custo") && Integer.parseInt(args[3]) < pl.getPConfig().getInt("x1-ValorMaximo")) {
                        custo = Integer.parseInt(args[3]);
                    } else {
                        player.sendMessage(lang.getStringReplaced("Player.Error1", "@Prefix", Util.getPrefix(), "@Max", Util.formatAnotherString(pl.getPConfig().getInt("x1-ValorMaximo"))));
                        return true;
                    }
                } else {
                    player.sendMessage(Util.msg(Util.getPrefix() + "&a" + args[3] + "&7 não é um número! iniciando valor padrão."));
                    custo = pl.getPConfig().getDouble("x1-Custo");
                }
            } else {
                player.sendMessage(Util.msg(Util.getPrefix() + "&7/x1 desafiar <player> <modo> [aposta]"));
                return true;
            }
            if (PlayerX1Helper.isPendente(player)) {
                player.sendMessage(lang.getStringReplaced("Player.Error4", "@Prefix", Util.getPrefix()));
                return true;
            }
            if (player.getName().equalsIgnoreCase(args[1])) {
                player.sendMessage(lang.getStringReplaced("Player.Error3", "@Prefix", Util.getPrefix()));
                return true;
            }
            if (Util.isPlayer(args[1])) {
                Player playerDesafiado = pl.getServer().getPlayerExact(args[1]);
                if (PlayerX1Helper.isInGame(playerDesafiado) || PlayerX1Helper.isPendente(playerDesafiado)) {
                    player.sendMessage(Util.msg("§cEste player já está em um x1."));
                    return true;
                }
                if (cm.getClanPlayer(playerDesafiado) != null && cm.getClanPlayer(player) != null) {
                    if (cm.getClanPlayer(player).getClan().getMembers().contains(cm.getClanPlayer(playerDesafiado))) {
                        player.sendMessage(Util.msg("&cVocê não pode desafiar um membro do seu clan!"));
                        return true;
                    }
                }
                if (pl.getEconomy().has(player, custo)) {
                    if (pl.getEconomy().has(playerDesafiado, custo)) {
                        if (args[2].equalsIgnoreCase("NORMAL") || args[2].equalsIgnoreCase("HARD") || args[2].equalsIgnoreCase("INSANO")) {
                            X1Type x1Type = X1Type.fromName(args[2]);
                            if (arenaManager.getArenas().size() != 0) {
                                PlayerX1Helper.getX1Handlers().put(player, new PlayerX1Handler(player, playerDesafiado, null, arenaManager, pl, x1Type, lang, databaseUtil));
                                PlayerX1Helper.getX1Handlers().get(player).setX1Custo(custo);
                                MenuHelper.addMenuView(player, MenuType.ARENAS);
                                guiManager.loadMenuArenas(player, lang.getStringCorrect("Menus.Arenas"));
                                return true;
                            } else {
                                player.sendMessage(Util.msg(Util.getPrefix() + "&7Não à nenhuma arena disponível."));
                                return true;
                            }
                        } else {
                            player.sendMessage(Util.msg(Util.getPrefix() + "&7Tipos de x1: &aNormal = 15m&7, &aHard = 30m&7, &aInsano = ☠&7"));
                            return true;
                        }
                    } else {
                        player.sendMessage(Util.msg(Util.getPrefix() + "&7O player desafiado não possui &a$" + Util.formatAnotherString(custo) + "&7 Para aceitar este x1."));
                        return true;
                    }
                } else {
                    player.sendMessage(Util.msg(Util.getPrefix() + "&7Você não possui &a$" + Util.formatAnotherString(custo) + "&7 Para desafiar um player."));
                    return true;
                }
            } else {
                player.sendMessage(Util.msg(Util.getPrefix() + "&a" + args[1] + " &7Não está online."));
                return true;

            }
        }

        /*
         * Comando /x1 aceitar <player>
         *
         */
        if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("aceitar")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!(args.length == 2)) {
                        player.sendMessage(Util.msg(Util.getPrefix() + "&7/x1 aceitar <player> "));
                        return;
                    }
                    if (Bukkit.getPlayerExact(args[1]) == null) {
                        player.sendMessage(Util.msg(Util.getPrefix() + "&a" + args[1] + " &7não está online!"));
                        return;
                    }
                    if (PlayerX1Helper.iniciandoContains(player)) { // Este if...
                        player.sendMessage(Util.msg(Util.getPrefix() + "&7O x1 já está iniciando! paciência."));
                        return;
                    }
                    Player desafiador = Bukkit.getPlayerExact(args[1]);
                    if (PlayerX1Helper.isPendente(player)) {
                        if (PlayerX1Helper.getPendentes().containsKey(desafiador)) {
                            if (PlayerX1Helper.getPendentes().get(desafiador).getUniqueId() == player.getUniqueId()) {
                                PlayerX1Helper.getIniciando().add(player); // Caso redigite o comando, esta linha irá bloquear a ação no if lá em cima..

                                String msg = lang.getStringReplaced("Player.X1Aceito", "@Prefix", Util.getPrefix());
                                desafiador.sendMessage(msg);
                                player.sendMessage(msg);
                                /*
                                 * Configuração final da arena, e dos seus integrantes..
                                 */
                                Arena arena = PlayerX1Helper.getX1Handlers().get(desafiador).getArena();
                                arena.setArenaStatus(ArenaStatus.ENTRANDO);
                                PlayerX1Helper.getX1Handlers().get(desafiador).setDesafiado(player);
                                PlayerX1Helper.getX1Handlers().get(desafiador).setDesafiador(desafiador);

                                Util.sendMessageAllPlayers(lang.getStringReplaced("X1Aceito",
                                        "@Player1", desafiador.getName(),
                                        "@Player2", player.getName(),
                                        "@Arena", arena.getNome())
                                        .replaceAll("%ArenaNome", arena.getArena())
                                        .replaceAll("@Valor", Util.formatAnotherString(PlayerX1Helper.getX1Handlers().get(desafiador).getX1Custo())));

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (PlayerX1Helper.getIniciando().contains(player)) {
                                            if(player != null && player.isOnline() && desafiador != null && desafiador.isOnline()) {
                                                PlayerX1Helper.getPendentes().remove(desafiador); // Player não está mais pendente...
                                                PlayerX1Helper.getIniciando().remove(player);
                                                PlayerX1Helper.getX1Handlers().get(desafiador).enviaSalaEspera();
                                                PlayerX1Helper.getInGame().put(desafiador, player);
                                            }
                                        }
                                        cancel();
                                    }
                                }.runTaskLater(pl, pl.getPConfig().getInt("TempoParaIniciar") * 20L);
                            } else {
                                player.sendMessage(lang.getStringReplaced("Player.Error6", "@Prefix", Util.getPrefix(), "@Target", args[1]));
                                return;
                            }
                        } else {
                            player.sendMessage(lang.getStringReplaced("Player.Error7", "@Prefix", Util.getPrefix(), "@Target", args[1]));
                            return;
                        }
                    } else {
                        player.sendMessage(lang.getStringReplaced("Player.Error8", "@Prefix", Util.getPrefix()));
                        return;
                    }
                    cancel();
                }
            }.runTask(pl);
            return true;
        }

        /*
         * Comando /x1 info [player]
         *
         */

        if (args[0].equalsIgnoreCase("info")) {
            if (!(args.length == 2)) {
                MenuHelper.addMenuView(player, MenuType.ONLINE_PLAYERS);
                guiManager.LoadPlayersHeads(player);
                return true;
            }
            PlayerCache playerCache = null;
            if (Bukkit.getPlayerExact(args[1]) != null) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if (ps.contains(target.getUniqueId())) {
                    playerCache = ps.getPlayer(target.getUniqueId());
                } else {
                    player.sendMessage(lang.getStringReplaced("Player.InfoError", "@Player", args[1], "@Prefix", Util.getPrefix()));
                    return true;
                }
            } else {
                if (ps.getByName(args[1]) != null) {
                    playerCache = ps.getByName(args[1]);
                } else {
                    player.sendMessage(lang.getStringReplaced("Player.InfoError", "@Player", args[1], "@Prefix", Util.getPrefix()));
                    return true;
                }
            }
            MenuHelper.addMenuView(player, MenuType.PERFIL);
            guiManager.loadPerfilJogador(player, playerCache);
            return true;
        }

        /*
         * Comando /x1 bau
         *
         */
        if (args[0].equalsIgnoreCase("bau")) {
            PlayerCache playerCache = ps.getPlayer(player.getUniqueId());
            if (playerCache == null) {
                player.sendMessage(Util.msg(Util.getPrefix() + " &cVocê precisa ir x1 ao menos uma vez."));
                return true;
            }
            Inventory inventory = playerCache.getInventory();
            if (args.length == 1) {
                if (playerCache.getInventory() != null && playerCache.getInventory().getSize() != 0) {
                    MenuHelper.addMenuView(player, MenuType.CHEST);
                    player.openInventory(playerCache.getInventory());
                    return true;
                } else {
                    player.sendMessage(lang.getStringReplaced("Player.SemInventario", "@Prefix", Util.getPrefix()));
                    return true;
                }
            }
            if (args[1].equalsIgnoreCase("comprar")) {
                if (playerCache.getPlayer_bau() < 6) {
                    MenuHelper.addMenuView(player, MenuType.CHEST_BUY);
                    guiManager.buyChestConfirm(player, inventory, playerCache.getPlayer_bau());
                    return true;
                } else {
                    player.sendMessage("§cVocê já fez upgrade máximo em seu baú!");
                    return true;
                }
            }
        }

        /*
         * Comando /x1 rejeitar [player]
         *
         */

        if (args[0].equalsIgnoreCase("rejeitar") || args[0].equalsIgnoreCase("reject") || args[0].equalsIgnoreCase("negar")) {
            if (!(args.length == 2)) {
                player.sendMessage(Util.msg(Util.getPrefix() + "&7/x1 rejeitar <player> "));
            }
            if (Bukkit.getPlayerExact(args[1]) == null) {
                player.sendMessage(Util.msg(Util.getPrefix() + "&a" + args[1] + " &7não está online!"));
                return true;
            }
            Player desafiador = Bukkit.getPlayerExact(args[1]);
            if (PlayerX1Helper.isPendente(player)) {
                if (PlayerX1Helper.getPendentes().containsKey(desafiador)) {
                    if (PlayerX1Helper.getPendentes().get(desafiador).getUniqueId() == player.getUniqueId()) {
                        PlayerX1Helper.cancelX1(desafiador);
                        player.sendMessage(lang.getStringReplaced("Player.X1Recusado", "@Prefix", Util.getPrefix()));
                        desafiador.sendMessage(lang.getStringReplaced("Player.X1Recusado", "@Prefix", Util.getPrefix()));
                        return true;
                    } else {
                        player.sendMessage(lang.getStringReplaced("Player.Error6", "@Prefix", Util.getPrefix(), "@Target", args[1]));
                        return true;
                    }
                } else {
                    player.sendMessage(lang.getStringReplaced("Player.Error7", "@Prefix", Util.getPrefix(), "@Target", args[1]));
                    return true;
                }
            } else {
                player.sendMessage(lang.getStringReplaced("Player.Error8", "@Prefix", Util.getPrefix()));
                return true;
            }

        }
        if (args[0].equalsIgnoreCase("camarote")) {
            if (args.length == 1) {
                List<String> inX1 = new ArrayList<>();
                List<String> avaliabe = new ArrayList<>();
                for (Arena a : arenaManager.getArenas()) {
                    if (a.getArenaStatus() != ArenaStatus.ABERTO && a.getArenaStatus() != ArenaStatus.DESATIVADA) {
                        inX1.add(a.getArena());
                    } else if (a.getArenaStatus() == ArenaStatus.ABERTO) {
                        avaliabe.add(a.getArena());
                    }
                }
                if (inX1.size() > 0) {
                    player.sendMessage("§cArenas em X1: ");
                    int i = 1;
                    StringBuilder b = new StringBuilder();
                    if (inX1.size() == 1) {
                        b.append(inX1.get(0));
                    } else {
                        for (String s : inX1) {
                            if (i == inX1.size()) {
                                b.append(s);
                            } else {
                                b.append(s).append("§7, §f");
                            }
                            i++;
                        }
                    }
                    player.sendMessage(b.toString());
                }
                if (avaliabe.size() > 0) {
                    player.sendMessage("§cArenas disponíveis: ");
                    int i = 1;
                    StringBuilder b = new StringBuilder();
                    if (avaliabe.size() == 1) {
                        b.append(avaliabe.get(0));
                    } else {
                        for (String s : avaliabe) {
                            if (i == avaliabe.size()) {
                                b.append(s);
                            } else {
                                b.append(s).append("§7, §f");
                            }
                            i++;
                        }
                    }
                    player.sendMessage(b.toString());
                }
                player.sendMessage("§e/x1 camarote <nome> §7- para ir para o camarote");
            }
            if (args.length >= 2) {
                Arena s = arenaManager.getArena(args[1]);
                if (s != null && s.getCamarote() != null && s.getCamarote().getWorld() != null) {
                    player.teleport(s.getCamarote());
                    player.sendMessage(lang.getStringReplaced("Teleportado", "@Prefix", Util.getPrefix()));
                } else {
                    player.sendMessage("§eArena §f" + args[1] + "§e não existe ou esta incompleta!");
                    return true;
                }
            }
        }
        return false;
    }

}
