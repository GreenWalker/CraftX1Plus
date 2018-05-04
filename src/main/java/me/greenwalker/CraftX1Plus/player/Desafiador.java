package me.floydz69.CraftX1Plus.player;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import org.bukkit.entity.Player;

/**
 * Created by gusta on 01/05/2017.
 */
public class Desafiador extends PlayerPontuationHandler {

    public Desafiador(Player player, CraftPlus plugin, DatabaseUtil databaseUtil) {
        super(player, plugin, plugin.getPlayerDb(), databaseUtil);
    }

}
