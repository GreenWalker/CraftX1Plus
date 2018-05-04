package me.floydz69.CraftX1Plus.util;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.arena.Arena;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class Util {

    public static String getPrefix() {
        return Prefix.getPrefix();
    }

    public static String msg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendMessageAllPlayers(String msg) {
        for (Player s : Bukkit.getOnlinePlayers()) {
            s.sendMessage(msg);
        }
    }

    public static boolean isPlayer(String args) {
        return Bukkit.getPlayerExact(args) != null;
    }

    public static String formatAnotherString(double valor) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        return format.format(valor);
    }

    public static boolean isNumber(String s) {
        try {
            int i = Integer.parseInt(s);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    public static int convertChestSize(int n) {
        if (n <= 6) {
            return n * 9;
        }
        return 0;
    }

    public static String getStatusColor(Arena arena) {
        if (arena.getArenaStatus() == ArenaStatus.DESATIVADA) {
            return "§bStatus da arena: §c" + ArenaStatus.DESATIVADA.toString().toLowerCase();
        }
        if (arena.getArenaStatus() == ArenaStatus.ABERTO) {
            return "§bStatus da arena: §a" + ArenaStatus.ABERTO.toString().toLowerCase();
        }
        if (arena.getArenaStatus() == ArenaStatus.EM_JOGO) {
            return "§bStatus da arena: §c" + ArenaStatus.EM_JOGO.toString().toLowerCase().replace("_", " ");
        }
        if (arena.getArenaStatus() == ArenaStatus.PENDENTE) {
            return "§bStatus da arena: §7" + ArenaStatus.PENDENTE.toString().toLowerCase();
        }
        if (arena.getArenaStatus() == ArenaStatus.ENTRANDO) {
            return "§bStatus da arena: §7" + ArenaStatus.ENTRANDO.toString().toLowerCase();
        }
        return "§bStatus da arena: Arena bugada, contate algum administrador.";
    }

    public static String getMessageFromLanguage(String path, boolean prefix) {
        if (prefix) {
            return CraftPlus.getMain().getLang().getStringReplaced(path, "@Prefix", getPrefix());
        } else {
            return CraftPlus.getMain().getLang().getStringCorrect(path);
        }
    }

    public static Location getLocationByString(String pos, String arena, ConfigHandler config) {
        String path = "Arenas." + arena + "." + pos;
        World world = Bukkit.getWorld(config.getString(path + ".World"));
        double x = Double.valueOf(config.getString(path + ".x"));
        double y = Double.valueOf(config.getString(path + ".y"));
        double z = Double.valueOf(config.getString(path + ".z"));
        Location l = new Location(world, x, y, z);
        return l;
    }

    public static void saveArenas(List<Arena> arenas) {
        ConfigHandler l = CraftPlus.getMain().getLocations();
        if (l.isConfigurationSection("Arenas")) {
            l.set("Arenas", "");
            arenas.forEach(Arena::saveArena);
        }
    }

    public static boolean isBscValidWorld(ConfigHandler location) {
        if (!location.isConfigurationSection("Bsc") || location.getString("Bsc.world") == null) {
            location.set("Bsc.x", 0);
            location.set("Bsc.y", 0);
            location.set("Bsc.z", 0);
            location.set("Bsc.world", "null");
            return false;
        }
        if (location.getString("Bsc.world").equalsIgnoreCase("null")) {
            return false;
        }
        String w = location.getStringCorrect("Bsc.world");
        World world = null;
        if ((world = Bukkit.getWorld(w)) == null) {
            return false;
        }
        int x = location.getInt("Bsc.x");
        int y = location.getInt("Bsc.y");
        int z = location.getInt("Bsc.z");
        return world.getBlockAt(x, y - 1, z).getType() != Material.AIR;
    }

    public static Location getBscLocation(ConfigHandler location) {
        if (isBscValidWorld(location)) {
            return new Location(Bukkit.getWorld(location.getString("Bsc.world")), location.getInt("Bsc.x"), location.getInt("Bsc.y"), location.getInt("Bsc.z"));
        }
        int rbound = Bukkit.getWorlds().size();
        World nworld = Bukkit.getWorlds().get(new Random().nextInt(rbound));
        Location l = new Location(nworld, location.getInt("Bsc.x"), location.getInt("Bsc.y") - 2, location.getInt("Bsc.z"));
        if (l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).getType() != Material.AIR) {
            return l;
        } else {
            l.getWorld().getBlockAt(l).setType(Material.BEDROCK);
            int rb = 5;
            for (int x = l.getBlockX() - rb; x <= l.getBlockX() + rb; x++) {
                for (int z = l.getBlockZ() - rb; z <= l.getBlockZ() + rb; z++) {
                    double bound = ((l.getBlockX() - x) * (l.getBlockX() - x) + (l.getBlockZ() - z) * (l.getBlockZ() - z));
                    if (rb < bound * bound) {
                        Location loc = new Location(l.getWorld(), x, 0, z);
                        loc.getWorld().getBlockAt(loc).setType(Material.GRASS);
                    }
                }
            }
        }
        return l;
    }

    public static boolean isNullWorld(Arena a, ConfigHandler config, String pos) {
        if (a == null) {
            return true;
        }
        if (!a.isFinished()) {
            return true;
        }
        Location l = Util.getLocationByString(pos, a.getArena(), config);
        if (l != null) {
            World w = l.getWorld();
            if (w != null) {
                return false;
            } else {
                String s = config.getString("Arenas." + a.getArena() + "." + pos + ".World");
                MultiverseWorld mw = CraftPlus.getMain().getMvWorld().getMVWorldManager().getMVWorld(s);
                if (mw != null) {
                    if (!CraftPlus.getMain().getLoader().loadArena(a.getArena(), false, config).isValidAllArenaWorlds()) {
                        CraftPlus.getMain().getServer().getLogger().warning("Não foi possível carregar a arena '" + a.getArena() + "':'" + a.getNome() + "'");
                        return true;
                    }
                    return false;
                } else {
                    CraftPlus.getMain().getServer().getLogger().warning("O mundo '" + s + "' não foi localizado, remova-o da configuração.");
                }
            }
        }
        return true;
    }
}
