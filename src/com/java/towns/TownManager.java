package com.java.towns;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
        if (!main.getUUIDCitizenMap().containsKey(e.getPlayer().getUniqueId())) {
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player sender = e.getPlayer();
        if (!main.getUUIDCitizenMap().containsKey(sender.getUniqueId())) return; // idk if this can ever even be false

        Citizen c = main.getUUIDCitizenMap().get(sender.getUniqueId());
        if (!c.getCreationStatus().equals("Prompted")) return; // they are not being asked for a town creation name

        // checks passed : create new town

        String townName = e.getMessage();
        c.setCreationStatus("Normal"); // return to normal state

        // check if valid town name

        for (Town t : main.getTowns()) {
            if (t.getName().equalsIgnoreCase(townName)) {
                Main.msg(sender, Main.color("&4Town name already taken!"));
                return;
            }
        }

        if(!townName.toLowerCase().matches("[a-z]"))
        {
            Main.msg(sender, Main.color("&4Town names can only contain characters (A-Z)"));
            return;
        }

        Town nt = new Town(sender, e.getMessage());
        main.getTowns().add(nt);

    }
}
