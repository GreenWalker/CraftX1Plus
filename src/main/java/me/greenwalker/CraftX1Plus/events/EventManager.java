package me.floydz69.CraftX1Plus.events;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.clan.ClanPlusManager;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.events.player.onClickInSkull;
import me.floydz69.CraftX1Plus.menu.GuiManager;
import me.floydz69.CraftX1Plus.menu.MenuHelper;
import me.floydz69.CraftX1Plus.menu.MenuType;
import me.floydz69.CraftX1Plus.player.PlayerCache;
import me.floydz69.CraftX1Plus.player.PlayerManager;
import me.floydz69.CraftX1Plus.x1.clan.ClanX1Handler;
import me.floydz69.CraftX1Plus.x1.clan.ClanX1Helper;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Helper;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Handler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by gusta on 15/05/2017.
 */
public class EventManager implements Listener {

    private CraftPlus main;
    private GuiManager guiManager;
    private PlayerManager playerManager;
    private ClanPlusManager clanManager;
    private ConfigHandler lang;

    public EventManager(CraftPlus pl, GuiManager guiManager, PlayerManager playerManager, ClanPlusManager clanManager, ConfigHandler lang) {
        this.main = pl;
        this.guiManager = guiManager;
        this.playerManager = playerManager;
        this.clanManager = clanManager;
        this.lang = lang;
    }

    @EventHandler
    public void onClickInSkull(onClickInSkull event) {
        ItemStack i = event.getSkull();
        for (PlayerCache players : playerManager.getPlayerCacheList().values()) {
            if (i.hasItemMeta() && i.getItemMeta().hasDisplayName() && !(i.getItemMeta().getDisplayName().equals("null"))) {
                if (i.getItemMeta().getDisplayName().equalsIgnoreCase(players.getPlayer().getName())) {
                    if (MenuHelper.getMenuView(event.getPlayer()).equals(MenuType.ARENA_STATUS) || MenuHelper.getMenuView(event.getPlayer()).equals(MenuType.ONLINE_PLAYERS) || MenuHelper.getMenuView(event.getPlayer()).equals(MenuType.BATALHAS)) {
                        guiManager.loadPerfilJogador(event.getPlayer(), players);
                        MenuHelper.getInventoryType().put(event.getPlayer(), MenuType.PERFIL);
                        return;
                    }
                    if (MenuHelper.getMenuView(event.getPlayer()).equals(MenuType.PERFIL)) {
                        MenuHelper.getInventoryType().put(event.getPlayer(), MenuType.BATALHAS);
                        guiManager.loadPlayerMenuBattles(event.getPlayer(), players.getBatalhas());
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player pl = e.getPlayer();
        try {
            if (PlayerX1Helper.isPendente(pl)) {
                switch (PlayerX1Helper.isKeyOrValue(PlayerX1Helper.getPendentes(), pl)) {
                    case 1:
                        Player p = (Player) PlayerX1Helper.getKeyOrValue(PlayerX1Helper.getPendentes(), pl);
                        PlayerX1Helper.getPendentes().get(p).sendMessage("§cDesafiador deslogou, cancelando pedido de x1...");
                        PlayerX1Helper.cancelX1(p);
                        break;
                    case 2:
                        for (Player player : PlayerX1Helper.getPendentes().keySet()) {
                            if (PlayerX1Helper.getPendentes().get(player).getName().equals(pl.getName())) {
                                player.sendMessage("§cDesafiado deslogou, cancenlando pedido de x1...");
                                PlayerX1Helper.cancelX1(pl);
                            }
                        }
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
            if (PlayerX1Helper.iniciandoContains(pl)) {
                PlayerX1Helper.getIniciando().remove(pl);
                return;
            }
            if (PlayerX1Helper.containsInEspera(pl)) {
                pl.setHealth(0);
                return;
            }
            if (PlayerX1Helper.isInGame(pl)) {
                switch (PlayerX1Helper.isKeyOrValue(PlayerX1Helper.getInGame(), pl)) {
                    case 1:
                        Player p = (Player) PlayerX1Helper.getKeyOrValue(PlayerX1Helper.getInGame(), pl);
                        if (p != null) {
                            PlayerX1Helper.getInGame().get(p).sendMessage("§cPlayer deslogou, cancelando x1.");
                            p.setHealth(0);
                        }
                        break;
                    case 2:
                        for (Player player : PlayerX1Helper.getInGame().keySet()) {
                            if (PlayerX1Helper.getInGame().get(player).getName().equals(pl.getName())) {
                                player.sendMessage("§cPlayer deslogou, cancelando x1.");
                                pl.setHealth(0);
                            }
                        }
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        } catch (NullPointerException erro) {
            erro.printStackTrace();
            System.err.println("Ocorreu um erro (NullPointerException) Contate o desenvolvedor (Floydz69)");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if (!PlayerX1Helper.isInGame(event.getEntity())) {
            return;
        }
        Player killed = event.getEntity();

        for (ClanX1Handler handler : ClanX1Helper.getInstance().getPedidosAbertos().values()) {
            //if(ClanX1Helper.getInstance().getClanParty(handler.getDesafiado().))
        }
        for (PlayerX1Handler value : PlayerX1Helper.getX1Handlers().values()) {
//            if (!value.isIniciou()) {
//                continue;
//            }

            for(Entity i : killed.getNearbyEntities(20, 60, 20)){
                if(i instanceof Item){
                    i.remove();
                }
            }

            if (value.getDesafiador().getPlayerName().equals(killed.getName())) {
                value.killPlayer(killed, event.getDrops(), playerManager.getPlayer(value.getDesafiado().getPlayerID()).getInventory());
            } else if (value.getDesafiado().getPlayerName().equals(killed.getName())) {
                value.killPlayer(killed, event.getDrops(), playerManager.getPlayer(value.getDesafiador().getPlayerID()).getInventory());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (main.getPConfig().getBoolean("Ativar-Bloqueador-De-Comandos") && !(event.getPlayer().isOp() || event.getPlayer().hasPermission("craftpx.admin"))) {
            if (PlayerX1Helper.containsInEspera(event.getPlayer()) || PlayerX1Helper.isInGame(event.getPlayer())) {
                String[] cmd = event.getMessage().replace("/", "").split(" ");
                List<String> stringList = main.getPConfig().getStringList("ComandosPermitidos");
                if (!stringList.contains("/" + cmd[0])) {
                    event.getPlayer().sendMessage(lang.getStringReplaced("Player.ComandoDesativado", "@Comando", cmd[0]));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (main.getPConfig().getBoolean("Ativar-Bloqueador-De-Movimentos")) {
            if (PlayerX1Helper.containsInEspera(e.getPlayer())) {
                Location to = e.getTo(), newLocation = e.getFrom();
                if (to.getX() != newLocation.getX() || to.getZ() != newLocation.getZ()) {
                    e.setCancelled(true);
                    e.getPlayer().teleport(newLocation);
                }
            }
        }
    }

    @EventHandler
    public void onItemPickUp(PlayerPickupItemEvent event) {
        if (PlayerX1Helper.containsPlayerInSaindo(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}