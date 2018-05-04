package me.floydz69.CraftX1Plus.clan.Object;

import me.floydz69.CraftX1Plus.CraftPlus;
import me.floydz69.CraftX1Plus.clan.ClanPontuation;
import me.floydz69.CraftX1Plus.config.ConfigHandler;
import me.floydz69.CraftX1Plus.util.DatabaseUtil.DatabaseUtil;
import me.floydz69.CraftX1Plus.util.Util;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gusta on 24/05/2017.
 */
public class Desafiador extends ClanPontuation {

    public Desafiador(CraftPlus craftPlus, Clan clan, DatabaseUtil databaseUtil) {
        super(craftPlus, clan, craftPlus.getClanDb(), databaseUtil);
    }

}
