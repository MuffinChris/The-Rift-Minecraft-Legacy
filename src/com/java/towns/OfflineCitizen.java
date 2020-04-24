package com.java.towns;

import com.java.Main;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OfflineCitizen {
    private UUID uuid;
    private int rank;
    private String town;

    public OfflineCitizen() {

    }
    public OfflineCitizen(UUID u) {
        uuid = u;
        pullFiles();
    }
    public OfflineCitizen(OfflinePlayer op){
        uuid = op.getUniqueId();
        pullFiles();
    }
    public int getRank() {
        return rank;
    }

    public String getTown() {
        return town;
    }

    public void setRank(int i) {
        rank = i;
        pushFiles();
    }

    public void setTown(String t){
        town = t;
        pushFiles();
    }
    public void pushFiles() {
        File pFile = new File("plugins/Rift/data/towns/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            /*
            Town info
             */

            pData.set("CurrentTown", getTown());
            pData.set("Rank", getRank());

            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void pullFiles() {
        File pFile = new File("plugins/Rift/data/towns/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (!pFile.exists()) {
            pushFiles();
            pullFiles();
        } else {
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
