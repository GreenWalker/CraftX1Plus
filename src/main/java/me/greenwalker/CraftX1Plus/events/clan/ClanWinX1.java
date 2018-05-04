package me.floydz69.CraftX1Plus.events.clan;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by gusta on 11/05/2017.
 */
public class ClanWinX1 extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;

    public ClanWinX1(Clan clan) {
        this.clan = clan;
    }

    public Clan getClan() {
        return clan;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
