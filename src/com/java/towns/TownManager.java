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

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TownManager implements Listener {
    private Main main = Main.getInstance();
    private TownCoreCommand tcc = new TownCoreCommand();

    public TownManager() {

    }

    public Town findTown(Citizen c) {
        for (Town t : main.getTowns()) {
            if (t.getName() == c.getTown()) {
                return t;
            }
        }
        return null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!main.getUUIDCitizenMap().containsKey(e.getPlayer().getUniqueId())) {
            main.getUUIDCitizenMap().put(e.getPlayer().getUniqueId(), new Citizen(e.getPlayer()));

            Citizen c = main.getUUIDCitizenMap().get(e.getPlayer().getUniqueId());

            if (c.getTown().equalsIgnoreCase(Citizen.defaultTownName)) {
                return;
            }

            boolean alreadyFound = false;
            for (Town t : main.getTowns()) {
                if (t.getName().equals(c.getTown())) {
                    alreadyFound = true;
                    break;
                }
            }

            if (!alreadyFound) {
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
                    for (Town t : main.getTowns()) { // check to remove towns
                        boolean cont = true;
                        for (UUID tc : t.getCitizenList()) {
                            if (main.getUUIDCitizenMap().containsKey(tc)) {
                                cont = false;
                                break;
                            }
                        }

                        if (cont) {
                            new BukkitRunnable() {
                                public void run() {
                                    main.getTowns().remove(t); // if there are no remaining online players of this town
                                    t.pushFiles();
                                }
                            }.runTaskLater(Main.getInstance(), 10L);
                        }
                    }
                }
            }.runTaskLater(Main.getInstance(), 10L);


        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player sender = e.getPlayer();
        if (!main.getUUIDCitizenMap().containsKey(sender.getUniqueId())) return; // cant be false pepelaugh

        Citizen c = main.getUUIDCitizenMap().get(sender.getUniqueId());
        if (c.getCreationStatus().equals("Prompted")) { // creating town
            String townName = e.getMessage();
            e.setCancelled(true);
            c.setCreationStatus("Normal"); // return to normal state

            tcc.createNewTown(sender, townName);

        } else if (c.getInviteSentStatus().equals("Prompted")) { // invite prompt
            String receiverName = e.getMessage();
            c.setInviteSentStatus("Normal");
            e.setCancelled(true);

            tcc.sendInvite(e.getPlayer(), receiverName);
        } else if (c.getSearchStatus().equals("Prompted")) { // search prompt
            String query = e.getMessage();
            e.setCancelled(true);
            c.setSearchStatus("Normal");

            tcc.searchTown(sender, query, 0);
        }


    }

    public int getRankOffline(UUID uuid){
        File pFile = new File("plugins/Rift/data/towns/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if(!pFile.exists()){
            return -1;
        }
        return pData.getInt("Rank");
    }

    public String getTownOffline(UUID uuid){
        File pFile = new File("plugins/Rift/data/towns/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if(!pFile.exists()){
            return "None";
        }
        return pData.getString("CurrentTown");
    }
    //should only need to be ran once
    /*public void makeFullTownList() {
        try {
            File tFile = new File("plugins/Rift/data/townlist/townlist.yml");
            FileConfiguration tData = YamlConfiguration.loadConfiguration(tFile);
            List<String> fulltown = new ArrayList<String>();
            tData.set("FullTownList", fulltown);

            tData.save(tFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
