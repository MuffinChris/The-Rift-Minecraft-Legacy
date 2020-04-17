package com.java.towns;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TownManager implements Listener {
    private Main main = Main.getInstance();

    private ArrayList<Town> towns;

    public TownManager() {
        towns = new ArrayList<Town>();
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        if (!main.getUUIDCitizenMap().containsKey(e.getPlayer().getUniqueId())) {
            main.getUUIDCitizenMap().put(e.getPlayer().getUniqueId(), new Citizen(e.getPlayer())); // needs to pull
        }
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        if(main.getUUIDCitizenMap().containsKey(e.getPlayer().getUniqueId()))
        {
            Citizen c = main.getUUIDCitizenMap().get(e.getPlayer().getUniqueId());
            c.pushFiles(); // not yet implemented

            new BukkitRunnable() {
                public void run() {
                    main.getUUIDCitizenMap().remove(e.getPlayer().getUniqueId());
                }
            }.runTaskLater(Main.getInstance(), 10L);

            for(Town t : towns) // check to remove towns
            {
                boolean cont = true;
                for(Player tc: t.getCitizenList().citimap.keySet())
                {
                    if(main.getUUIDCitizenMap().containsKey(tc.getUniqueId())) {
                        cont = false;
                        break;
                    }
                }

                if(cont) {
                    t.pushFiles();
                    new BukkitRunnable() {
                        public void run() {
                            towns.remove(t); // if there are no remaining online players of this town
                        }
                    }.runTaskLater(Main.getInstance(), 10L);
                }
            }

        }

        if (main.getPC().containsKey(e.getPlayer().getUniqueId())) {
            main.getPC().get(e.getPlayer().getUniqueId()).pushFiles();
            new BukkitRunnable() {
                public void run() {
                    main.getPC().remove(e.getPlayer().getUniqueId());
                }
            }.runTaskLater(Main.getInstance(), 10L);
        }
    }
}
