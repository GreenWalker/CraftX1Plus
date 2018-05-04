package me.floydz69.CraftX1Plus.events;

import me.floydz69.CraftX1Plus.CraftPlus;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.events.AllyClanAddEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.CreateClanEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerJoinedClanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gusta on 23/05/2017.
 */
public class ClanEventHandler implements Listener {

    private CraftPlus main;

    public ClanEventHandler(CraftPlus main) {
        this.main = main;
    }

    @EventHandler
    public void onClanJoin(PlayerJoinedClanEvent event) {
        if(main.getPConfig().getBoolean("Extras.Ativar-Limitador-De-Clans")) {
            if (event.getClan().getAllMembers().size() >= main.getPConfig().getInt("Extras.Player-Por-Clan")) {
               event.getClan().removeMember(event.getClanPlayer().getUniqueId());
               event.getClan().removePlayerFromClan(event.getClanPlayer().getUniqueId());
               event.getClanPlayer().toPlayer().sendMessage("§7*§bEste clan está cheio!");
               event.getClan().addBb("§7*§bPlayer " + event.getClanPlayer().getName() + " kickado do clan.");
               event.getClan().addBb("§7*§bSeu clan não possuí espaço para novos membros.");
            }
        }
    }

    @EventHandler
    public void onAllyAdd(AllyClanAddEvent event) {
        if(main.getPConfig().getBoolean("Extras.Ativar-Limitador-De-Clans")) {
            Clan requester = event.getClanFirst();
            Clan inveted = event.getClanSecond();
            List<Clan> clan1 = new ArrayList<>();
            List<Clan> clan2 = new ArrayList<>();
            for (String s : event.getClanFirst().getAllies()){
              clan1.add(main.getSimpleClans().getClanManager().getClan(s));
            }
            for (String s : event.getClanSecond().getAllies()){
              clan2.add(main.getSimpleClans().getClanManager().getClan(s));
            }
            if (clan1.size() > main.getPConfig().getInt("Extras.Ally-Por-Clan") || clan2.size() > main.getPConfig().getInt("Extras.Ally-Por-Clan")) {
                event.getClanFirst().removeAlly(event.getClanSecond());
                event.getClanFirst().addBb("§fClan não pode adcionar novas allies.");
                event.getClanSecond().addBb("§fClan não pode receber novas allies.");
            }
        }
    }
}
