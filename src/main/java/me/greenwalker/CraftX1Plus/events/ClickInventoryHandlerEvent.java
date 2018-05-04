package me.floydz69.CraftX1Plus.events;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.arena.ArenaManager;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.events.player.onClickInSkull;
import me.floydz69.CraftX1Plus.menu.GuiManager;
import me.floydz69.CraftX1Plus.menu.MenuHelper;
import me.floydz69.CraftX1Plus.menu.MenuType;
import me.floydz69.CraftX1Plus.player.PlayerManager;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Handler;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Helper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class ClickInventoryHandlerEvent implements Listener {
    private CraftPlus pl;
    private ArenaManager arenaManager;
    private ConfigHandler lang;
    private GuiManager guiManager;
    private DatabaseUtil dbutil;
    private PlayerManager pm;
    //private ReflectionUtil reflectionUtil;

    public ClickInventoryHandlerEvent(CraftPlus pl, ArenaManager arenaManager, GuiManager guiManager, ConfigHandler lang, DatabaseUtil databaseUtil, PlayerManager playerManager) {
        this.pl = pl;
        this.arenaManager = arenaManager;
        this.lang = lang;
        this.guiManager = guiManager;
        this.dbutil = databaseUtil;
        this.pm = playerManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!MenuHelper.getInventoryType().containsKey(player)) return;
        if (MenuHelper.getMenuView(player) == MenuType.CHEST) return;
        if (event.getInventory().getType() == InventoryType.CRAFTING) return;
        if (event.getClickedInventory() != null) {
            if (event.getCurrentItem().getItemMeta() == null) return;
            if (event.getCurrentItem().getType() == Material.AIR) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            switch (MenuHelper.getMenuView(player)) {
                case ARENAS:
                    String name = event.getCurrentItem().getItemMeta().getDisplayName();
                    if (arenaManager.getByName(name) != null) {
                        Arena arena = arenaManager.getByName(name);
                            if(arena.getEspera().getWorld() == null){
                                arena = arenaManager.getByName(name);
                                if(arena.getEspera().getWorld() == null){
                                    player.closeInventory();
                                    player.sendMessage("§cUm erro ocorreu ao selecionar esta arena, por favor, escolha outra ou contate algum staff informando qual a arena em questão.");
                                    return;
                                }
                            }
//                        if(!arena.getArenaType().equals(PlayerX1Helper.getX1Handlers().get(player).getX1Type())){
//                            player.closeInventory();
//                            player.sendMessage("A arena precisa ser o tipo " + arena.getArenaType().toString());
//                        }
                        if (arena.getArenaStatus() == ArenaStatus.ABERTO) {
                            PlayerX1Helper.getX1Handlers().get(player).setArena(arena);
                            player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1.5F, 1.5F);
                            MenuHelper.addMenuView(player, MenuType.CONFIRMAR);
                            guiManager.loadConfirmMenu(player);
                            return;
                        }
                        if (arena.getArenaStatus() == ArenaStatus.DESATIVADA) {
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.5F, 1.5F);
                            return;
                        }
                        if (arena.getArenaStatus() != ArenaStatus.ABERTO && arena.getArenaStatus() != ArenaStatus.DESATIVADA) {
                            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.5F, 1.5F);
//                          MenuHelper.addMenuView(player, MenuType.ARENA_STATUS);
//                          guiManager.loadArenaStatus(player, arena, PlayerX1Helper.getX1Handlers().get(player));
                            return;
                        }
                    }
                    break;
                case CONFIRMAR:
                    if (PlayerX1Helper.getX1Handlers().containsKey(player)) {
                        PlayerX1Handler playerX1Handler = PlayerX1Helper.getX1Handlers().get(player);
                        Player p2 = playerX1Handler.getP2();
                        if (item.getItemMeta().getDisplayName().equals(lang.getStringCorrect("ConfirmarX1"))) {
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.5F, 1.5F);
                            player.sendMessage(Util.msg(lang.getStringReplaced("PedidoEnviado", "@Prefix", Util.getPrefix())));
                            PlayerX1Helper.getPendentes().put(player, p2);
                            playerX1Handler.getArena().setArenaStatus(ArenaStatus.PENDENTE);
                            for (String s : lang.getStringList("PedidoX1")) {
                                p2.sendMessage(s.replace("@Player1", player.getName()).replaceAll("&", "§").replace("@Arena", playerX1Handler.getArena().getNome()).replace("@Modo", playerX1Handler.getX1Type().toString().toLowerCase()).replace("@Valor", Util.formatAnotherString(playerX1Handler.getX1Custo())).replace("@Tempo", String.valueOf(pl.getPConfig().getInt("CancelarPedido") / 60)));
                            }
                            player.closeInventory();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (PlayerX1Helper.getPendentes().containsKey(player)) {
                                        p2.sendMessage(lang.getStringReplaced("Player.Error2", "@Prefix", Util.getPrefix()));
                                        player.sendMessage(lang.getStringReplaced("Player.Error2", "@Prefix", Util.getPrefix()));
                                        PlayerX1Helper.cancelX1(player);
                                    }
                                    cancel();
                                }
                            }.runTaskLater(pl, pl.getPConfig().getInt("CancelarPedido") * 20L);
                            return;
                        }
                        if (item.getItemMeta().getDisplayName().equals(lang.getStringCorrect("CancelarX1"))) {
                            player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.5F, 1.5F);
                            PlayerX1Helper.getX1Handlers().remove(player);
                            player.closeInventory();
                            return;
                        }
                    }
                    break;
                case ONLINE_PLAYERS:
                    if (item.getType() == Material.PAPER) {
                        if (item.getItemMeta() != null) {
                            if (item.getItemMeta().getDisplayName().contains(lang.getStringCorrect("ProximaPagina"))) {
                                guiManager.nextPageInventory(MenuHelper.getPage(player) + 1, event.getInventory(), player, MenuHelper.getItemPlayer(player));
                                player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                return;
                            } else if (item.getItemMeta().getDisplayName().contains(lang.getStringCorrect("PaginaAnterior"))) {
                                guiManager.nextPageInventory(MenuHelper.getPage(player) - 1, event.getInventory(), player, MenuHelper.getItemPlayer(player));
                                player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                return;
                            }
                        }
                    }
                    if (item.getType() == Material.NETHER_STAR) {
                        MenuHelper.addMenuView(player, MenuType.PERFIL);
                        if (pm.getPlayer(player.getUniqueId()) == null) {
                            player.closeInventory();
                            player.sendMessage(Util.msg(Util.getPrefix() + " &cVocê precisa ir x1 ao menos uma vez.")); // mandar title
                            return;
                        }
                        guiManager.loadPerfilJogador(player, pm.getPlayer(player.getUniqueId()));
                        return;
                    }
                    if (item.getType() == Material.SKULL_ITEM) {
                        try {
                            Bukkit.getServer().getPluginManager().callEvent(new onClickInSkull(player, item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case PERFIL:
                    if (item.getType() == Material.WOOD_DOOR) {
                        player.closeInventory();
                        return;
                    }
                    if (item.getType() == Material.SKULL_ITEM) {
                        try {
                            Bukkit.getServer().getPluginManager().callEvent(new onClickInSkull(player, item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case ARENA_STATUS:
                    String names = event.getInventory().getTitle();
                    String la = lang.getStringCorrect("Menus.StatusArena").replace("@Arena", "");
                    name = names.replace(la, "");
                    if (arenaManager.getByName(name) != null) {
                        Arena arena = arenaManager.getByName(name);
                        if (item.getItemMeta().getDisplayName().equals("§aIr para o §6Camarote")) {
                            if (arena.getCamarote() != null) {
                                player.closeInventory();
                                player.teleport(arena.getCamarote());
                                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
                                player.sendMessage(Util.msg(lang.getStringReplaced("Teleportado", "@Prefix", Util.getPrefix())));
                            } else {
                                player.sendMessage(Util.msg(lang.getStringReplaced("Locations.CamaroteError", "@Prefix", Util.getPrefix())));
                            }
                        }
                    }
                    if (item.getType() == Material.SKULL_ITEM) {
                        try {
                            Bukkit.getServer().getPluginManager().callEvent(new onClickInSkull(player, item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case ARENA_INFO:
                    if (arenaManager.getByName(item.getItemMeta().getDisplayName()) != null) {
                        Arena s = arenaManager.getByName(item.getItemMeta().getDisplayName());

                        if(s.getArenaStatus() == ArenaStatus.DESATIVADA){
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.5F, 1.5F);
                            return;
                        }

                        PlayerX1Handler px1 = null;
                        for (PlayerX1Handler px : PlayerX1Helper.getX1Handlers().values()) {
                            if (px != null && px.getArena().getNome().equalsIgnoreCase(s.getNome())) {
                                px1 = px;
                            }
                        }
                        MenuHelper.addMenuView(player, MenuType.ARENA_STATUS);
                        guiManager.loadArenaStatus(player, s, px1);
                    }
                    break;
                case BATALHAS:
                    if (item.getType() == Material.PAPER) {
                        if (item.getItemMeta() != null) {
                            if (item.getItemMeta().getDisplayName().contains(lang.getStringCorrect("ProximaPagina"))) {
                                guiManager.nextPageInventory(MenuHelper.getPage(player) + 1, event.getInventory(), player, MenuHelper.getItemPlayer(player));
                                player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            } else if (item.getItemMeta().getDisplayName().contains(lang.getStringCorrect("PaginaAnterior"))) {
                                guiManager.nextPageInventory(MenuHelper.getPage(player) - 1, event.getInventory(), player, MenuHelper.getItemPlayer(player));
                                player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            }
                        }
                    }
                    if (item.getType() == Material.SKULL_ITEM) {
                        try {
                            Bukkit.getServer().getPluginManager().callEvent(new onClickInSkull(player, item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CHEST_BUY:
                    if (item.getType() == Material.STAINED_GLASS) {
                        if (item.getItemMeta() != null) {
                            if (item.getItemMeta().getDisplayName().equals("§aConfirmar Compra")) {
                                int amount = pm.getPlayer(player.getUniqueId()).getPlayer_bau() + 1;
                                double valor = pl.getPConfig().getDouble("upgrade-" + amount);
                                if (pl.getEconomy().has(player, valor)) {
                                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2F, 1F);
                                    pl.getEconomy().withdrawPlayer(player, valor);
                                    dbutil.setPlayerBauSize(player, amount);
                                    pm.getPlayer(player.getUniqueId()).setPlayer_bau(amount);
                                    player.closeInventory();
                                    String msg = amount == 1 ? "&eVocê comprou seu &cprimeiro&e baú. Agora itens dropados por inimigos mortos no seu x1 iram para este inventário." : "&eVocê comprou seu &c" + amount + "º&e baú, agora seu tamanho total e de &c" + amount * 9 + "&e slots.";
                                    player.sendMessage(Util.msg(Util.getPrefix() + msg));
                                    player.sendMessage(Util.msg(Util.getPrefix() + "&eVocê gastou &a$&c" + Util.formatAnotherString(valor) + "&e para comprar este bau."));
                                } else {
                                    player.closeInventory();
                                    player.sendMessage(Util.msg("&cVocê não possuí &e" + Util.formatAnotherString(valor) + " &c para comprar este bau."));
                                }
                            } else if (item.getItemMeta().getDisplayName().equals("§cCancelar Compra")) {
                                MenuHelper.getInventoryType().remove(player);
                                player.closeInventory();
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("oi meu chapa");
                    break;
            }
        }
    }
}
