package me.floydz69.CraftX1Plus.x1;

import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class X1Utils {

    public static void enviaItems(Player player, Inventory inv, List<ItemStack> drops, ConfigHandler lang) {
        boolean cheio = false;
        if (inv == null && inv.getSize() == 0) {
            player.sendMessage(lang.getStringReplaced("Player.SemInventario", "@Prefix", Util.getPrefix()));
        }
        Random r = new Random();
        for (ItemStack stack : drops) {
            if (stack.getType().equals(Material.AIR)) {
                drops.remove(stack);
            }
        }
        if (drops.size() <= 0) {
            player.sendMessage(Util.getMessageFromLanguage("Player.InimigoSemItens", true));
            return;
        }

        for (int i = 0; i < drops.size(); i++) {
            if (i == inv.getSize()) break;
            if (inv.firstEmpty() == -1) {
                player.sendMessage(Util.getMessageFromLanguage("Player.InventarioCheio", true));
                cheio = true;
                break;
            }
            int rr = r.nextInt(drops.size());
            inv.setItem(inv.firstEmpty(), drops.get(rr));
            drops.remove(rr);
        }
        if (!cheio)
            player.sendMessage(lang.getStringReplaced("Player.ItensParaInventario", "@Prefix", Util.getPrefix()));

    }

    public static int isKeyOrValue(Map<?, ?> map, Object value) {
        if (map.containsKey(value)) {
            return 1;
        } else if (map.containsValue(value)) {
            return 2;
        } else {
            return 3;
        }
    }

    public static Object getKeyOrValue(Map<?, ?> map, Object value) {
        if (map.containsKey(value)) {
            for (Object s : map.keySet()) {
                if (s.equals(value)) {
                    return s;
                }
            }
        }
        if (map.containsValue(value)) {
            for (Object s : map.values()) {
                if (s.equals(value)) {
                    return s;
                }
            }
        }
        return null;
    }

}
