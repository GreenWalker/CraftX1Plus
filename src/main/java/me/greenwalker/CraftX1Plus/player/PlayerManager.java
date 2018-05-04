package me.floydz69.CraftX1Plus.player;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.x1.player.PlayerBattleHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gusta on 15/05/2017.
 */

public class PlayerManager {

    private CraftPlus craftPlus;
    private Map<UUID, PlayerCache> playerCacheList;

    public PlayerManager(CraftPlus craftPlus, Map<UUID ,PlayerCache> playerCacheLists) {
        this.craftPlus = craftPlus;
        this.playerCacheList = playerCacheLists;
    }

    public Map<UUID ,PlayerCache> getPlayerCacheList() {
        return this.playerCacheList;
    }

    public PlayerCache getPlayer(UUID uuid){
        if(contains(uuid)){
            return this.playerCacheList.get(uuid);
        }
        return null;
    }

    public boolean contains(UUID id){
        if(playerCacheList != null){
            for(UUID ids : playerCacheList.keySet()) {
               if(ids.equals(id)){
                return true;
               }
            }
            return false;
        }
        return false;
    }

    public PlayerCache getByName(String name){
        for(PlayerCache s : playerCacheList.values()){
            if(s.getPlayer_name().equalsIgnoreCase(name)){
                return s;
            }
        }
        return null;
    }

    public void registerNew(Player player){
        if(contains(player.getUniqueId())){
            return;
        }
        this.getPlayerCacheList().put(player.getUniqueId(), new PlayerCache(player, 0 ,0 ,0, new ArrayList<PlayerBattleHandler>()));
    }
}
