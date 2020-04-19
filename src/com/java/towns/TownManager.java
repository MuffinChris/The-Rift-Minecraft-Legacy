package com.java.towns;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TownManager implements Listener {
    private Main main = Main.getInstance();

    public TownManager() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!main.getUUIDCitizenMap().containsKey(e.getPlayer().getUniqueId())) {
            main.getUUIDCitizenMap().put(e.getPlayer().getUniqueId(), new Citizen(e.getPlayer()));

            Citizen c = main.getUUIDCitizenMap().get(e.getPlayer().getUniqueId());

            if(c.getTown().equals("None")) {
                return;
            }

            boolean alreadyFound = false;
            for(Town t: main.getTowns()) {
                if(t.getName().equals(c.getTown())) {
                    alreadyFound = true;
                    break;
                }
            }

            if(!alreadyFound) {
                main.getTowns().add(new Town(c.getTown())); // need to add it back to memory if they are the only online player (just joined)
            }
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
        if (c.getCreationStatus().equals("Prompted")) {
            String townName = e.getMessage();
            e.setCancelled(true);

            c.setCreationStatus("Normal"); // return to normal state
            // check if valid town name
            for (String tn : main.getFullTownList()) {
                if (tn.equalsIgnoreCase(townName)) {
                    Main.msg(sender, Main.color("&4Town name already taken!"));
                    return;
                }
            }

            if (!townName.matches("[a-zA-Z ]+")) {
                Main.msg(sender, Main.color("&4Town names can only contain characters (A-Z) and spaces"));
                return;
            }

            if (townName.equalsIgnoreCase("None")) {
                Main.msg(sender, Main.color("&4That is a protected town name!"));
                return;
            }

            Town nt = new Town(sender, e.getMessage());
            main.getTowns().add(nt);

            List<String> fullTowns = main.getFullTownList();
            fullTowns.add(e.getMessage());
            main.setFullTownList(fullTowns);

            Main.msg(sender, Main.color("&l&aSuccessfully created town: &f" + townName));
        }
        else if(c.getInviteSentStatus().equals("Prompted")) {
            String recieverName = e.getMessage();
            c.setInviteSentStatus("Normal");
            e.setCancelled(true);

            Player r = Bukkit.getPlayer(recieverName);
            if (r == null) {
                Main.msg(sender, Main.color("&4Player not found"));
                return;
            }

            Citizen cr = main.getUUIDCitizenMap().get(r.getUniqueId());
            Town t = null;
            for (Town ct : main.getTowns()) {
                if (ct.getName().equals(c.getTown())) {
                    t = ct;
                    break;
                }
            }

            if (!cr.getTown().equalsIgnoreCase("None")) {
                Main.msg(r, "&4This player is already in another town!");
                return;
            }

            if(!cr.getInviteStatus().equalsIgnoreCase("Normal")) {
                Main.msg(r, "&4This player already has a pending invite!");
                return;
            }

            t.invite(sender, r);
        } else if(c.getPromoteStatus().equals("Prompted")){
            String recieverName = e.getMessage();
            Player r = Bukkit.getPlayer(recieverName);
            if (r == null) {
                Main.msg(sender, Main.color("&4Player not found"));
                return;
            }
            e.setCancelled(true);
            String sr = main.getUUIDCitizenMap().get(sender.getUniqueId()).getTown();
            Town t = null;
            for (Town ct : main.getTowns()) {
                if (ct.getName().equals(c.getTown())) {
                    t = ct;
                    break;
                }
            }
            t.promote(sender, r);
            return;
        }

    }

    //should only need to be ran once
    public void makeFullTownList() {
        try {
            File tFile = new File("plugins/Rift/data/townlist/townlist.yml");
            FileConfiguration tData = YamlConfiguration.loadConfiguration(tFile);
            List<String> fulltown = new ArrayList<String>();
            tData.set("FullTownList", fulltown);

            tData.save(tFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
