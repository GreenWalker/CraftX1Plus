package me.floydz69.CraftX1Plus.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by gusta on 13/05/2017.
 */
public class MenuHelper {

    private static Map<Player, Integer> page = new HashMap<>();
    private static Map<Player, List<ItemStack>> items = new HashMap<>();
    private static Map<Player, MenuType> inventoryType = new HashMap<Player, MenuType>();

    public static Map<Player, MenuType> getInventoryType() {
        return inventoryType;
    }

    public static void addMenuView(Player p, MenuType type){
        inventoryType.put(p, type);
    }

    public static Map<Player, List<ItemStack>> getItems() {
        return items;
    }

    public static Map<Player, Integer> getPage() {
        return page;
    }

    public static int getPage(Player p){
        return getPage().get(p);
    }

    public static List<ItemStack> getItemPlayer(Player p){
        return getItems().get(p);
    }

    public static MenuType getMenuView(Player p){
        return getInventoryType().get(p);
    }
}
