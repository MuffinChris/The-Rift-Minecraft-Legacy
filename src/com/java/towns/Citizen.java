package com.java.towns;

import com.java.Main;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Citizen extends OfflineCitizen{

    private int r; //Rank in town, -1 if no town
    private Player p;
    private String town; //Player's town, current "Randos" if townless
    public static String defaultTownName = "None";

    private String creationStatus = "Normal";
    private String inviteStatus = "Normal";
    private String inviteSentStatus = "Normal";
    private String searchStatus = "Normal";

    public Citizen() {
        r = -1; p = null; town = defaultTownName;
    }
    public Citizen(Player pl) {
        this.p = pl;
        this.r = -1;
        this.town = defaultTownName;
        pullFiles();
    }

    public Citizen(Player pl, int rnk) {
        this.p = pl;
        this.r = rnk;

        pullFiles();
    }

    public Citizen(Player pl, int rnk, String t) {
        this.p = pl;
        this.r = rnk;
        this.town = t;

        pullFiles();
    }

    public Player getPlayer() {
        return p;
    }

    public int getRank() {
        return r;
    }

    public String getTown() {
        return town;
    }

    public String getCreationStatus() {
        return creationStatus;
    }

    public String getInviteStatus() {
        return inviteStatus;
    }

    public String getInviteSentStatus() {
        return inviteSentStatus;
    }

    public String getSearchStatus() {
        return searchStatus;
    }

    public void setRank(int i) {
        r = i;
        pushFiles();
    }

    public void setTown(String t) {
        town = t;
        pushFiles();
    }

    public void setCreationStatus(String status) {
        creationStatus = status;
    }

    public void setInviteStatus(String status) {
        inviteStatus = status;
    }

    public void setInviteSentStatus(String status) {
        inviteSentStatus = status;
    }

    public void setSearchStatus(String status) {
        searchStatus = status;
    }

    public void setUsernameFile(FileConfiguration pData, String username) {
        if (pData.contains("Username")) {
            if (!pData.getString("Username").equalsIgnoreCase(p.getName())) {
                pData.set("PreviousUsername", pData.getString("Username"));
            }
        }
        pData.set("Username", username);
    }

    public void setLastSeenFile(FileConfiguration pData, Long millis) {
        pData.set("LastSeen", millis);
    }

    public void pushFiles() {
        File pFile = new File("plugins/Rift/data/towns/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
             /*
            Utility
             */
            setUsernameFile(pData, p.getName());

            /*
            Town info
             */

            pData.set("CurrentTown", town);
            pData.set("Rank", r);

            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pullFiles() {
        File pFile = new File("plugins/Rift/data/towns/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (!pFile.exists()) {
            pushFiles();
            pullFiles();
        } else {
            setUsernameFile(pData, p.getName());
            /*
            Town info
             */
            setRank(pData.getInt("Rank"));
            setTown(pData.getString("CurrentTown"));
            /*
            User Status info
             */
        }
    }


}
