package com.java.towns;

import com.java.Main;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Citizen {

    private int r; //Rank in town, -1 if no town
    private Player p;
    private String town; //Player's town, current "Randos" if townless
    public static String defaultTownName = "None";

    private String creationStatus;
    private String inviteStatus;
    private String inviteSentStatus;
    private String searchStatus;
    private int areYouSureStatus = -1;


    public Citizen(Player pl){
        this.p = pl; this.r = -1; this.town = defaultTownName;
        pullFiles();
        creationStatus = "Normal"; inviteStatus = "Normal"; inviteSentStatus = "Normal"; searchStatus = "Normal";
    }

    public Citizen(Player pl, int rnk){
        this.p = pl;
        this.r = rnk;

        pullFiles();

        creationStatus = "Normal"; inviteStatus = "Normal"; inviteSentStatus = "Normal"; searchStatus = "Normal";
    }

    public Citizen(Player pl, int rnk, String t){
        this.p = pl;
        this.r = rnk;
        this.town = t;

        pullFiles();

        creationStatus = "Normal"; inviteStatus = "Normal"; inviteSentStatus = "Normal"; searchStatus = "Normal";
    }

    public Player getPlayer(){ return p; }
    public int getRank(){ return r; }
    public String getTown(){ return town; }

    public String getCreationStatus() { return creationStatus; }
    public String getInviteStatus() { return inviteStatus; }
    public String getInviteSentStatus() { return inviteSentStatus; }
    public String getSearchStatus() { return searchStatus; }
    public int getAreYouSureStatus() {
        if(areYouSureStatus == 1) { areYouSureStatus = -1; return 1; }
        else if(areYouSureStatus == 0) { areYouSureStatus = -1; return 0; }
        return -1;
    }

    public void setRank(int i) { r = i; }
    public void setTown(String t) { town = t; }

    public void setCreationStatus(String status) {creationStatus = status; }
    public void setInviteStatus(String status) { inviteStatus = status; }
    public void setInviteSentStatus(String status) { inviteSentStatus = status; }
    public void setSearchStatus(String status) { searchStatus = status; }
    public void setAreYouSureStatus(int status) { areYouSureStatus = status; }

    public void setUsernameFile(FileConfiguration pData, String username) {
        if (pData.contains("Username")) {
            if (pData.getString("Username").equalsIgnoreCase(p.getName())) {
                pData.set("Username", username);
            } else {
                pData.set("PreviousUsername", pData.getString("Username"));
                pData.set("Username", username);
            }
        } else {
            pData.set("Username", username);
        }
    }

    public void setLastSeenFile(FileConfiguration pData, Long millis) {
        pData.set("LastSeen", millis);
    }

    public void pushFiles(){
        File pFile = new File("plugins/Rift/data/towns/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try{
             /*
            Utility
             */
            setUsernameFile(pData, p.getName());

            /*
            Town info
             */

            pData.set("CurrentTown", town);
            pData.set("Rank", r);

            /*
            User Status Info
             */

            pData.set("CreationStatus", creationStatus);
            pData.set("InviteStatus", inviteStatus);
            pData.set("InviteSentStatus", inviteSentStatus);
            pData.set("SearchStatus", searchStatus);

            pData.save(pFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void pullFiles(){
        File pFile = new File("plugins/Rift/data/towns/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if(!pFile.exists()){
            pushFiles(); pullFiles();
        }
        else{
            setUsernameFile(pData, p.getName());
            /*
            Town info
             */
            setRank(pData.getInt("Rank"));
            setTown(pData.getString("CurrentTown"));

            /*
            User Status info
             */
            setCreationStatus(pData.getString("CreationStatus"));
            setInviteStatus(pData.getString("InviteStatus"));
        }
    }

}
