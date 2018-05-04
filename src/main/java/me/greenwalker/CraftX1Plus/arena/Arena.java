package me.floydz69.CraftX1Plus.arena;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.sun.org.apache.xpath.internal.operations.Bool;
import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.exception.ArenaCorrupted;
import me.floydz69.CraftX1Plus.util.Util;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;
import me.floydz69.CraftX1Plus.util.enums.ArenaType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gusta on 07/05/2017.
 */
public class Arena {

    private String arena;
    private String nome;
    private Location pos1;
    private Location pos2;
    private Location camarote;
    private Location espera;
    private Location saida;
    private ArenaType arenaType;
    private ArenaStatus arenaStatus;
    private final CraftPlus plugin;
    private final ConfigHandler config;
    private final String path;

    public Arena(CraftPlus plugin, String arena, String nome, Location pos1, Location pos2, Location camarote, Location espera, Location saida, ArenaType arenaType, ArenaStatus arenaStatus) {
        this.arena = arena;
        this.plugin = plugin;
        this.nome = nome;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.camarote = camarote;
        this.espera = espera;
        this.saida = saida;
        this.arenaType = arenaType;
        this.arenaStatus = arenaStatus;
        this.config = plugin.getLocations();
        this.path = "Arenas." + getArena();
    }

    public boolean inUse() throws ArenaCorrupted {
        if (getArenaStatus() == ArenaStatus.EM_JOGO || getArenaStatus() == ArenaStatus.ENTRANDO || getArenaStatus() == ArenaStatus.PENDENTE) {
            return true;
        } else if (getArenaStatus() == ArenaStatus.ABERTO) {
            return false;
        } else {
            throw new ArenaCorrupted("Esta arena esta fechada!");
        }
    }

    public boolean isFinished() {
        return pos1 != null && pos2 != null && espera != null && saida != null;
    }

    public String setPos1Config() {
        config.set(path + ".Pos1.World", getPos1().getWorld().getName());
        config.set(path + ".Pos1.x", Math.round(getPos1().getX()));
        config.set(path + ".Pos1.y", Math.round(getPos1().getY()));
        config.set(path + ".Pos1.z", Math.round(getPos1().getZ()));
        config.trySave();
        return "Pos1 setada com sucesso para a arena &a" + getArena() + "&7.";
    }

    public String setPos2Config() {
        config.set(path + ".Pos2.World", getPos2().getWorld().getName());
        config.set(path + ".Pos2.x", Math.round(getPos2().getX()));
        config.set(path + ".Pos2.y", Math.round(getPos2().getY()));
        config.set(path + ".Pos2.z", Math.round(getPos2().getZ()));
        config.trySave();
        return "Pos2 setada com sucesso para a arena &a" + getArena() + "&7.";
    }

    public String setCamaroteConfig() {
        config.set(path + ".Camarote.World", getCamarote().getWorld().getName());
        config.set(path + ".Camarote.x", Math.round(getCamarote().getX()));
        config.set(path + ".Camarote.y", Math.round(getCamarote().getY()));
        config.set(path + ".Camarote.z", Math.round(getCamarote().getZ()));
        config.trySave();
        return "Camarote setado com sucesso para a arena &a" + getArena() + "&7.";
    }

    public String setSaidaConfig() {
        config.set(path + ".Saida.World", getSaida().getWorld().getName());
        config.set(path + ".Saida.x", Math.round(getSaida().getX()));
        config.set(path + ".Saida.y", Math.round(getSaida().getY()));
        config.set(path + ".Saida.z", Math.round(getSaida().getZ()));
        config.trySave();
        return "Saida setada com sucesso para a arena &a" + getArena() + "&7.";
    }

    public String setEsperaConfig() {
        config.set(path + ".Espera.World", getEspera().getWorld().getName());
        config.set(path + ".Espera.x", Math.round(getEspera().getX()));
        config.set(path + ".Espera.y", Math.round(getEspera().getY()));
        config.set(path + ".Espera.z", Math.round(getEspera().getZ()));
        config.trySave();
        return "Sala de espera setada com sucesso para a arena &a" + getArena() + "&7.";
    }

    public String setNomeConfig() {
        config.set(path + ".Nome", getNome());
        config.trySave();
        return "Nome " + getNome() + " &7setado para a arena &a" + getArena() + "&7.";
    }

    public String setTypeConfig() {
        config.set(path + ".Tipo", getArenaType().toString());
        config.trySave();
        return "Tipo &a" + getArenaType().toString() + " &7setado para a arena &a" + getArena() + "&7.";
    }

    public String setStatusConfig() {
        config.set(path + ".Status", getArenaStatus().toString());
        config.trySave();
        return "Status &a" + getArenaStatus().toString() + " &7setado para a arena &a" + getArena() + "&7.";
    }

    public String generateConfig() {
        if (plugin.getLocations().contains(path)) {
            return "Arena " + getArena() + " já existe.";
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    config.set(path + ".Nome", "");
                    config.set(path + ".Pos1", "");
                    config.set(path + ".Pos1.World", "");
                    config.set(path + ".Pos1.x", 0);
                    config.set(path + ".Pos1.y", 0);
                    config.set(path + ".Pos1.z", 0);
                    config.set(path + ".Pos2", "");
                    config.set(path + ".Pos2.World", "");
                    config.set(path + ".Pos2.x", 0);
                    config.set(path + ".Pos2.y", 0);
                    config.set(path + ".Pos2.z", 0);
                    config.set(path + ".Camarote", "");
                    config.set(path + ".Camarote.World", "");
                    config.set(path + ".Camarote.x", 0);
                    config.set(path + ".Camarote.y", 0);
                    config.set(path + ".Camarote.z", 0);
                    config.set(path + ".Espera", "");
                    config.set(path + ".Espera.World", "");
                    config.set(path + ".Espera.x", 0);
                    config.set(path + ".Espera.y", 0);
                    config.set(path + ".Espera.z", 0);
                    config.set(path + ".Saida", "");
                    config.set(path + ".Saida.World", "");
                    config.set(path + ".Saida.x", 0);
                    config.set(path + ".Saida.y", 0);
                    config.set(path + ".Saida.z", 0);
                    config.set(path + ".Tipo", getArenaType().toString());
                    config.set(path + ".Status", getArenaStatus().toString());
                    config.trySave();
                    cancel();
                }
            }.runTask(CraftPlus.getMain());
            return "Arena " + getArena() + " criada na config.";
        }
    }

    public void saveArena() {
        if (!config.isConfigurationSection(path)) {
            return;
        }
        config.set(path + ".Nome", getNome());
        config.set(path + ".Pos1", "");
        config.set(path + ".Pos1.World", (pos1.getWorld() != null ? pos1.getWorld() : ""));
        config.set(path + ".Pos1.x", (pos1 != null ? pos1.getX() : 0));
        config.set(path + ".Pos1.y", (pos1 != null ? pos1.getY() : 0));
        config.set(path + ".Pos1.z", (pos1 != null ? pos1.getZ() : 0));
        config.set(path + ".Pos2", "");
        config.set(path + ".Pos2.World", (pos2.getWorld() != null ? pos2.getWorld() : ""));
        config.set(path + ".Pos2.x", (pos2 != null ? pos2.getX() : 0));
        config.set(path + ".Pos2.y", (pos2 != null ? pos2.getY() : 0));
        config.set(path + ".Pos2.z", (pos2 != null ? pos2.getZ() : 0));
        config.set(path + ".Camarote", "");
        config.set(path + ".Camarote.World", (camarote.getWorld() != null ? camarote.getWorld() : ""));
        config.set(path + ".Camarote.x", (camarote != null ? camarote.getX() : 0));
        config.set(path + ".Camarote.y", (camarote != null ? camarote.getY() : 0));
        config.set(path + ".Camarote.z", (camarote != null ? camarote.getZ() : 0));
        config.set(path + ".Espera", "");
        config.set(path + ".Espera.World", (espera.getWorld() != null ? espera.getWorld() : ""));
        config.set(path + ".Espera.x", (espera != null ? espera.getX() : 0));
        config.set(path + ".Espera.y", (espera != null ? espera.getY() : 0));
        config.set(path + ".Espera.z", (espera != null ? espera.getZ() : 0));
        config.set(path + ".Saida", "");
        config.set(path + ".Saida.World", (saida.getWorld() != null ? saida.getWorld() : ""));
        config.set(path + ".Saida.x", (saida != null ? saida.getX() : 0));
        config.set(path + ".Saida.y", (saida != null ? saida.getY() : 0));
        config.set(path + ".Saida.z", (saida != null ? saida.getZ() : 0));
        config.set(path + ".Tipo", getArenaType().toString());
        config.set(path + ".Status", getArenaStatus().toString());
        config.trySave();
    }

    public boolean isValidAllArenaWorlds() {
        List<Boolean> valids = new ArrayList<>();
        try {
            if (Bukkit.getWorld(getPos1().getWorld().getName()) == null) {
                valids.add(true);
            } else {
                valids.add(false);
            }
            if (Bukkit.getWorld(getPos2().getWorld().getName()) == null) {
                valids.add(true);
            } else {
                valids.add(false);
            }
            if (Bukkit.getWorld(getSaida().getWorld().getName()) == null) {
                valids.add(true);
            } else {
                valids.add(false);
            }
            if (Bukkit.getWorld(getCamarote().getWorld().getName()) == null) {
                valids.add(true);
            } else {
                valids.add(false);
            }
            if (Bukkit.getWorld(getEspera().getWorld().getName()) == null) {
                valids.add(true);
            } else {
                valids.add(false);
            }
        } catch (Exception er) {
            valids.add(true);
        }
        boolean allValids = false;
        for (boolean b : valids) {
            if (b) {
                allValids = false;
                break;
            } else {
                allValids = true;
            }
        }
        return allValids;
    }


    // GETTER'S AND SETTER'S

    public String getArena() {
        return arena;
    }

    public void setArena(String arena) {
        this.arena = arena;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getCamarote() {
        if (Util.isNullWorld(this, config, "Camarote")) {
            return Util.getBscLocation(config);
        }
        return camarote;
    }

    public void setCamarote(Location camarote) {
        this.camarote = camarote;
    }

    public Location getEspera() {
        if (Util.isNullWorld(this, config, "Espera")) {
            return Util.getBscLocation(config);
        }
        return espera;
    }

    public void setEspera(Location espera) {
        this.espera = espera;
    }

    public Location getSaida() {
        if (Util.isNullWorld(this, config, "Saida")) {
            return Util.getBscLocation(config);
        }
        return saida;
    }

    public void setSaida(Location saida) {
        this.saida = saida;
    }

    public ArenaType getArenaType() {
        return arenaType;
    }

    public void setArenaType(ArenaType arenaType) {
        this.arenaType = arenaType;
    }

    public ArenaStatus getArenaStatus() {
        return arenaStatus;
    }

    public void setArenaStatus(ArenaStatus arenaStatus) {
        this.arenaStatus = arenaStatus;
    }
}
