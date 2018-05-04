package me.floydz69.CraftX1Plus.arena;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.util.enums.ArenaStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gusta on 07/05/2017.
 */
public class ArenaManager {

    private final CraftPlus plugin;
    private List<Arena> arenas;

    public ArenaManager(CraftPlus plugin, List<Arena> arenas) {
        this.plugin = plugin;
        this.arenas = arenas;
    }

    public void setArenas(List<Arena> arenas) {
        this.arenas = arenas;
    }

    public void addArena(Arena a){
        if(!contains(a.getArena())){
            arenas.add(a);
        }
    }

    public List<Arena> getArenas(){
        return this.arenas;
    }

    public boolean contains(String name) {
        for (Arena ar : arenas) {
            if (ar.getArena().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Arena getArena(String name) {
        if (contains(name)) {
            for (Arena ar : arenas) {
                if (ar.getArena().equals(name)) {
                    return ar;
                }
            }
            return null;
        }
        return null;
    }

    public boolean createArena(String name) {
        if (!contains(name)) {
            this.arenas.add(new Arena(plugin, name, name, null, null, null, null, null, null, null));
            return true;
        } else {
            return false;
        }
    }

    public boolean removeArena(String name) {
        if (contains(name)) {
            for (Arena ar : this.arenas) {
                if (ar.getArena().equals(name)) {
                    this.arenas.remove(ar);
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public Arena getByName(String name){
        for(Arena ar : this.arenas){
            if(ar.getNome().equals(name)){
                return ar;
            }
        }
        return null;
    }

    public List<Arena> getAvaliables(){
        List<Arena> arenalist = new ArrayList<>();
        for(Arena ar : this.arenas){
            if(ar.getArenaStatus() == ArenaStatus.ABERTO){
                arenalist.add(ar);
            }
        }
        return arenalist;
    }

    public void replaceAnArena(Arena a){
        if(contains(a.getArena())){
            removeArena(a.getArena());
            addArena(a);
        }
    }
}
