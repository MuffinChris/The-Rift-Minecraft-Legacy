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

    public TownManager() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Main.msg(e.getPlayer(), "Join Event");
        if (!main.getUUIDCitizenMap().containsKey(e.getPlayer().getUniqueId())) {
            Main.msg(e.getPlayer(), "Added to UUIDCitizen Map");
            main.getUUIDCitizenMap().put(e.getPlayer().getUniqueId(), new Citizen(e.getPlayer()));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (main.getUUIDCitizenMap().containsKey(e.getPlayer().getUniqueId())) {
            Citizen c = main.getUUIDCitizenMap().get(e.getPlayer().getUniqueId());
            c.pushFiles();

            new BukkitRunnable() {
                public void run() {
                    main.getUUIDCitizenMap().remove(e.getPlayer().getUniqueId());
                }
            }.runTaskLater(Main.getInstance(), 10L);

            for (Town t : main.getTowns()) { // check to remove towns
                boolean cont = true;
                for (Player tc : t.getCitizenList().citimap.keySet()) {
                    if (main.getUUIDCitizenMap().containsKey(tc.getUniqueId())) {
                        cont = false;
                        break;
                    }

                }

                if (cont) {
                    t.pushFiles();
                    new BukkitRunnable() {
                        public void run() {
                            main.getTowns().remove(t); // if there are no remaining online players of this town
                        }
                    }.runTaskLater(Main.getInstance(), 10L);
                }
            }

        }
    }
}
