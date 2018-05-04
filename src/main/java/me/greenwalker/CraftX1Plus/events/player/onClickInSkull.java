package me.floydz69.CraftX1Plus.events.player;

import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Created by gusta on 12/05/2017.
 */
public class onClickInSkull extends Event {

    private Player player;
    private ItemStack itemSkull;

    private static final HandlerList handlers = new HandlerList();

    public onClickInSkull(Player p, ItemStack itemSkull) throws Exception {
        if (itemSkull.getType() == Material.SKULL || itemSkull.getType() == Material.SKULL_ITEM) {
            this.itemSkull = itemSkull;
            this.player = p;
        } else {
            throw new Exception("Object passed as parameter itemSkull is not a Skull");
        }
    }

    public ItemStack getSkull() {
        return itemSkull;
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
