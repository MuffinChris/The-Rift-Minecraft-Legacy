package com.java.towns;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
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
        if (!main.getPC().containsKey(e.getPlayer().getUniqueId())) {
            main.getPC().put(e.getPlayer().getUniqueId(), new RPGPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        if (main.getPC().containsKey(e.getPlayer().getUniqueId())) {
            main.getPC().get(e.getPlayer().getUniqueId()).pushFiles();
            main.getPC().get(e.getPlayer().getUniqueId()).scrub();
            new BukkitRunnable() {
                public void run() {
                    main.getPC().remove(e.getPlayer().getUniqueId());
                }
            }.runTaskLater(Main.getInstance(), 10L);
        }
    }
}
