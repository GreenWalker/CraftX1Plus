package me.floydz69.CraftX1Plus.events.player;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by gusta on 11/05/2017.
 */
public class PlayerDeathX1 extends Event{

    private Player player;

    private static final HandlerList handlers = new HandlerList();

    public PlayerDeathX1(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
