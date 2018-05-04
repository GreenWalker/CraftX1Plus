package me.floydz69.CraftX1Plus.menu;

import com.google.common.base.Strings;
import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.arena.ArenaManager;
import me.floydz69.CraftX1Plus.clan.ClanPlusManager;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.player.PlayerCache;
import me.floydz69.CraftX1Plus.player.PlayerManager;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import me.floydz69.CraftX1Plus.util.ItemCreateUtil;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Handler;
import me.floydz69.CraftX1Plus.x1.player.PlayerX1Helper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gusta on 08/05/2017.
 */
public class GuiManager {

    private CraftPlus plugin;
    private ArenaManager arenaManager;
    private ConfigHandler lang;
    private ClanPlusManager clanUtils;
    private PlayerManager playerManager;

    public GuiManager(CraftPlus pl, ArenaManager arenaManager, ConfigHandler lang, ClanPlusManager plusManager, PlayerManager manager) {
        this.plugin = pl;
        this.arenaManager = arenaManager;
        this.lang = lang;
        this.clanUtils = plusManager;
        this.playerManager = manager;
    }

    public void loadMenuArenas(Player p, String name) {
        Inventory inv = Bukkit.createInventory(null, 36, name);
        for (int i = 0; i < this.arenaManager.getArenas().size(); ++i) {
            Arena arena = this.arenaManager.getArenas().get(i);
            if (arena.getArenaStatus() == ArenaStatus.ABERTO) {
                inv.setItem(i, ItemCreateUtil.add(Material.DIAMOND_SWORD, arena.getNome(), new String[]{"§7Clique para Selecionar a arena", " ", Util.getStatusColor(arena)}));
            } else if (arena.getArenaStatus() == ArenaStatus.DESATIVADA) {
                inv.setItem(i, ItemCreateUtil.add(Material.IRON_SWORD, arena.getNome(), new String[]{"§5Arena em questão não está aberta!", " ", Util.getStatusColor(arena)}, Enchantment.DAMAGE_ALL, 1, true));
            } else {
                for (Player player : PlayerX1Helper.getX1Handlers().keySet()) {
                    inv.setItem(i, ItemCreateUtil.add(Material.DIAMOND_SWORD, arena.getNome(), new String[]{"§7Clique para Detalhes do x1", Util.getStatusColor(arena), " ", "§a" + PlayerX1Helper.getX1Handlers().get(player).getP1().getName() + " §7vs " + "§a" + PlayerX1Helper.getX1Handlers().get(player).getP2().getName(), "§7Aposta: " + Util.formatAnotherString(PlayerX1Helper.getX1Handlers().get(player).getX1Custo())}));

                }
            }
        }
        p.openInventory(inv);
    }

    public void loadConfirmMenu(Player p) {
        if (PlayerX1Helper.containsX1Handler(p)) {
            Player p2 = PlayerX1Helper.getX1Handlers().get(p).getP2();
            String title = lang.getStringReplaced("Menus.Confirmar", "@Player2", PlayerX1Helper.getX1Handlers().get(p).getP2().getName());
            Inventory inv = Bukkit.createInventory(null, 9, title);
            Random r = new Random();
            inv.setItem(3, ItemCreateUtil.paneColor(this.lang.getStringCorrect("ConfirmarX1"), 1, (short) r.nextInt(15), this.lang.getStringReplaced("ConfirmarX1-Lore", "@Player", p2.getName())));
            inv.setItem(5, ItemCreateUtil.paneColor(this.lang.getStringCorrect("CancelarX1"), 1, (short) r.nextInt(15), this.lang.getStringReplaced("CancelarX1-Lore", "@Player", p2.getName())));
            p.openInventory(inv);
        }
    }

    public void LoadPlayersHeads(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, lang.getStringCorrect("Menus.PlayersOnline"));
        List<ItemStack> players = new ArrayList<>();
        for (PlayerCache P : playerManager.getPlayerCacheList().values()) {
            players.add(ItemCreateUtil.skull(P.getPlayer_name(), P.getPlayer_name()));
        }
        for (int i = 45; i < 54; ++i) {
            if (i == 46 || i == 48 || i == 50) {
                continue;
            }
            inv.setItem(i, ItemCreateUtil.add(Material.STAINED_GLASS_PANE, " ", 15));
        }
        nextPageInventory(1, inv, p, players);
        String s[] = {};
        int siz = (int) Math.round((players.size() / 45) + 0.5D);
        inv.setItem(46, ItemCreateUtil.addMaterialLore(Material.NETHER_STAR, "§aMeu Perfil"));
        inv.setItem(48, ItemCreateUtil.add(Material.PAPER, lang.getStringCorrect("PaginaAnterior") + " §a" + 1 + "§7/§c" + (siz == 0 ? 1 : siz), s, Enchantment.DURABILITY, 1, true));
        inv.setItem(50, ItemCreateUtil.add(Material.PAPER, lang.getStringCorrect("ProximaPagina") + " §a" + 1 + "§7/§c" + (siz == 0 ? 1 : siz), s, Enchantment.DURABILITY, 1, true));
        p.openInventory(inv);
    }


    public void loadArenaStatus(Player p, Arena arena, PlayerX1Handler playerX1Handler) {
        if (arena != null) {
            Inventory inv = Bukkit.createInventory(null, 9, lang.getStringCorrect("Menus.StatusArena").replace("@Arena", arena.getNome()));
            inv.setItem(0, ItemCreateUtil.add(Material.DIAMOND_SWORD, "§aIr para o §6Camarote", new String[]{"§7Clique para ir ao camarote"}));
            if (arena.getArenaStatus() != ArenaStatus.ABERTO && arena.getArenaStatus() != ArenaStatus.DESATIVADA) {
                if (playerX1Handler != null) {
                    Player p1 = playerX1Handler.getP1();
                    Player p2 = playerX1Handler.getP2();
                    try {
                        inv.setItem(3, ItemCreateUtil.skull(p1.getName(), p1.getName(), "§7Info do player: ", "§7Vitorias: §c" + playerManager.getPlayer(p1.getUniqueId()).getPlayer_vitorias(), "§7Derrotas: §c" + playerManager.getPlayer(p1.getUniqueId()).getPlayer_derrotas()));
                        inv.setItem(5, ItemCreateUtil.skull(p2.getName(), p2.getName(), "§7Info do player: ", "§7Vitorias: §c" + playerManager.getPlayer(p2.getUniqueId()).getPlayer_vitorias(), "§7Derrotas: §c" + playerManager.getPlayer(p2.getUniqueId()).getPlayer_derrotas()));
                    } catch (NullPointerException err) {
                        Bukkit.getLogger().warning(Strings.repeat("=", 53));
                        Bukkit.getLogger().warning("Um Erro ocorreu ao carregar o status de uma arena! veja abaixo as causas");
                        Bukkit.getLogger().warning("* Player 1 is null? " + (p1 == null ? "Sim" : "Não"));
                        Bukkit.getLogger().warning("* Player 2 is null? " + (p2 == null ? "Sim" : "Não"));
                        Bukkit.getLogger().warning("* Desafiador is null? " + (playerX1Handler.getDesafiador() == null ? "Sim" : "Não"));
                        Bukkit.getLogger().warning("* Desafiado is null? " + (playerX1Handler.getDesafiado() == null ? "Sim" : "Não"));
                        Bukkit.getLogger().warning("* Status do x1 da arena " + (playerX1Handler.getArena() == null ? "Sem status" : playerX1Handler.getArena().getArenaStatus().toString()));
                        Bukkit.getLogger().warning("* Causa do erro -> " + err.getCause());
                        Bukkit.getLogger().warning("StackTrace do erro");
                        err.printStackTrace();
                        Bukkit.getLogger().warning(Strings.repeat("=", 53));
                    }
                }else{
                    p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
                    p.closeInventory();
                    p.sendMessage("§cNão foi possível carregar o status desta arena.");
                    return;
                }
            } else if (arena.getArenaStatus() == ArenaStatus.ABERTO) {
                inv.setItem(3, ItemCreateUtil.add(Material.BEDROCK, "§4Livre", new String[]{"§7Nenhum player indo x1 nesta arena"}, Enchantment.DURABILITY, 1, true));
                inv.setItem(5, ItemCreateUtil.add(Material.BEDROCK, "§4Livre", new String[]{"§7Nenhum player indo x1 nesta arena"}, Enchantment.DURABILITY, 1, true));
            }
            p.openInventory(inv);
        }
    }

    /**
     * @param p1 player que irá olhar o inventário
     * @param p  player no qual deverá pegar os dados
     */
    public void loadPerfilJogador(Player p1, PlayerCache p) {
        Inventory inv = Bukkit.createInventory(null, 9, lang.getStringCorrect("Menus.PlayerPerfil").replace("@Player", p.getPlayer_name()));
        inv.setItem(3, ItemCreateUtil.paneColor("§aVitorias", 1, (short) 5, "§7Numero de vitorias:§c " + p.getPlayer_vitorias()));
        inv.setItem(5, ItemCreateUtil.paneColor("§cDerrotas", 1, (short) 14, "§7Numero de derrotas:§c " + p.getPlayer_derrotas()));
        inv.setItem(4, ItemCreateUtil.skull(p.getPlayer_name(), p.getPlayer_name(), "§7Lista de jogadores", "§7No qual o player já batalhou"));
        if (clanUtils.getClanPlayer(p.getPlayer()) != null) {
            inv.setItem(0, ItemCreateUtil.add(Material.GOLDEN_APPLE, "§7Player do Clan " + clanUtils.getClanPlayer(p.getPlayer()).getClan().getTag(), new String[]{"§7Player faz parte do clan " + clanUtils.getClanPlayer(p.getPlayer()).getClan().getColorTag() + " §7- §b" + clanUtils.getClanPlayer(p.getPlayer()).getClan().getName()}));
        } else {
            inv.setItem(0, ItemCreateUtil.add(Material.BEDROCK, "§7Player sem clan", new String[]{}, Enchantment.DURABILITY, 1, true));
        }
        inv.setItem(8, ItemCreateUtil.add(Material.WOOD_DOOR, "§cSair"));
        p1.openInventory(inv);
    }

    public void loadPlayerMenuBattles(Player p1, List<PlayerBattleHandler> playerBattleHandlers) {
        Inventory inv = Bukkit.createInventory(null, 54, lang.getStringCorrect("Menus.PlayerBattles"));
        List<ItemStack> skulls = new ArrayList<>();
        if (playerBattleHandlers != null) {
            for (PlayerBattleHandler s : playerBattleHandlers) {
                skulls.add(ItemCreateUtil.skull("§c" + s.getContra().getName(), s.getContra().getName(),
                        "§2► §bId da batalha:§a " + (s.getId() == 0 ? "batalha não atualizada" : s.getId()),
                        "§2► §bStatus da batalha: " + (s.getVenceu() ? "§aVenceu" : "§cPerdeu"),
                        "§2► §bTipo:§a " + s.getTipo().toLowerCase(),
                        "§2► §bData da batalha: §a" + s.getData()));
            }
            nextPageInventory(1, inv, p1, skulls);
            int siz = (int) Math.round((playerBattleHandlers.size() / 45) + 0.5D);
            String[] d = {};
            inv.setItem(48, ItemCreateUtil.add(Material.PAPER, lang.getStringCorrect("PaginaAnterior") + " §a" + 1 + "§7/§c" + (siz == 0 ? 1 : siz), d, Enchantment.DURABILITY, 1, true));
            inv.setItem(50, ItemCreateUtil.add(Material.PAPER, lang.getStringCorrect("ProximaPagina") + " §a" + 1 + "§7/§c" + (siz == 0 ? 1 : siz), d, Enchantment.DURABILITY, 1, true));
            p1.openInventory(inv);
        }
    }

    public void buyChestConfirm(Player player, Inventory chest, int lvl) {
        Inventory inv = plugin.getServer().createInventory(null, 9, lang.getStringCorrect("Menus.ConfirmarCompra"));
        int size = (chest == null ? 0 : chest.getSize());
        inv.setItem(2, ItemCreateUtil.add1(Material.STAINED_GLASS, "§aConfirmar Compra", (lvl == 0 ? 1 : lvl + 1), 5));
        inv.setItem(4, ItemCreateUtil.addMaterialLore(Material.ENDER_CHEST, "§bDetalhes", "§7Aumente o nível do seu baú em 1", " ",
                "§cAtual:§e " + lvl,
                "§cPróximo: §e" + (lvl == 5 ? (lvl + 1) + "§7 (Nível Máximo)" : (lvl + 1)),
                "§cValor: §a$§e " + Util.formatAnotherString(plugin.getPConfig().getDouble("upgrade-" + (lvl + 1))), "§cQuantidade de Slots Atual:§e " + size, "§cQuantidade de Slots depois da compra:§e " + (size + 9)));
        inv.setItem(6, ItemCreateUtil.add1(Material.STAINED_GLASS, "§cCancelar Compra", (lvl == 0 ? 1 : lvl + 1), 14));
        player.openInventory(inv);
    }

    public void nextPageInventory(int page, Inventory inventory, Player player, List<ItemStack> list) {
        // if page is minor are 1, revert list and get first index
        if (page < 1) {
            final int size = list.size();
            page = size / 45;
            if (size % 45 > 0) {
                ++page;
            }
        }
        // iterate this item list whit 45, because inventory size is 45
        int size2 = list.size();
        int n2 = (page - 1) * 45;
        int n3 = (n2 + 44 >= size2) ? (size2 - 1) : (page * 45 - 1);
        if (n3 - n2 + 1 < 1 && page != 1) {
            nextPageInventory(1, inventory, player, list);
            return;
        }

        int n4 = 0;
        for (int i = 0; i < 45; ++i) {
            inventory.setItem(i, (ItemStack) null);
        }
        // populate inventory
        for (int j = n2; j <= n3; ++j) {
            inventory.setItem(n4, list.get(j));
            ++n4;
        }
        MenuHelper.getPage().put(player, page);
        MenuHelper.getItems().put(player, list);
        int pagination = (int) Math.round((list.size() / 45) + 0.5D);
        String[] s = {};
        inventory.setItem(48, ItemCreateUtil.add(Material.PAPER, lang.getStringCorrect("PaginaAnterior") + " §a" + MenuHelper.getPage().get(player) + "§7/§c" + (pagination == 0 ? 1 : pagination), s, Enchantment.DURABILITY, 1, true));
        inventory.setItem(50, ItemCreateUtil.add(Material.PAPER, lang.getStringCorrect("ProximaPagina") + " §a" + MenuHelper.getPage().get(player) + "§7/§c" + (pagination == 0 ? 1 : pagination), s, Enchantment.DURABILITY, 1, true));
        player.updateInventory();
    }

}
